package com.id05.asteriskcallmedisa;

import android.util.Log;

import org.apache.commons.io.input.TeeInputStream;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PipedInputStream;
import java.io.PipedOutputStream;
import java.util.LinkedList;
import java.util.Locale;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.concurrent.atomic.AtomicInteger;

import static java.lang.Thread.sleep;


public class MyTelnetClient {
    private MyTelnetConnection client;
    private OutputStream outstream;
    private org.apache.commons.net.telnet.TelnetClient rawConnection;
    private InputStream instream;
    AtomicInteger guy;
    private LinkedList<Thread> threads = new LinkedList();
    private PipedInputStream spyReader;

    public MyTelnetClient(String ip, int port) throws IOException {
        client = new MyTelnetConnection(ip, port);
        client.connect();
        rawConnection = client.getConnection();
        outstream = client.getOutput();
        instream = client.getReader();
    }

    public void close() throws IOException {
        rawConnection.disconnect();
    }

     /**
     * @param cmd the string of message you want to send
     * @return true if message was sent successfully
     */
    public boolean sendCommand(String cmd) {
        if (client == null || !client.isConnected()) {
            return false;
        }
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(cmd);
        stringBuilder.append("\n\r");

        byte[] cmdbyte = stringBuilder.toString().getBytes();

        try {
            outstream.write(cmdbyte, 0, cmdbyte.length);
            outstream.flush();

            return true;
        } catch (Exception e1) {
            return false;
        }
    }

    public String getResponse(String cmd) throws IOException, InterruptedException {

        if (client == null || !client.isConnected()) {
            throw new IOException("Unable to send message to disconnected client");
        }

        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(cmd);
        stringBuilder.append("\n");

        byte[] cmdbyte = stringBuilder.toString().getBytes();
        //Log.d("aster","STRINGBUILDER: "+stringBuilder.toString());

        InputStreamReader a = spawnSpy();//new InputStreamReader(instream);//
        BufferedReader buf = new BufferedReader(a);
        outstream.write(cmdbyte, 0, cmdbyte.length);
        outstream.flush();
        while(buf.ready())
        { buf.read();

        }
        StringBuilder result = null;
        result = new StringBuilder();
        String bufstr;
        Boolean done = false;
        while((!(bufstr = buf.readLine()).equals(""))){
            result.append(bufstr);
        }
        //Log.d("aster", "result.toString() = "+result.toString());
       return result.toString();
    }
    public InputStreamReader spawnSpy() throws InterruptedException, IOException {
        PipedInputStream in = new PipedInputStream();
        PipedOutputStream out = new PipedOutputStream();
        in.connect(out);
        if(spyReader!=null) {
            return spawnSpy(spyReader, out);
        } else {
            spyReader = in;
            return spawnSpy(instream, out);
        }
    }

    private InputStreamReader spawnSpy(InputStream in, PipedOutputStream pipeout) throws InterruptedException {
        return new InputStreamReader(new TeeInputStream(in,pipeout));
    }

    public boolean isConnected() {
        return client.isConnected();
    }

    public boolean disconnect() {
        spyReader = null;
        return client.disconnect();
    }
}
