package com.id05.asteriskcallmedisa;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.IOException;
import java.net.SocketException;
import java.util.Locale;

import android.util.Log;

import org.apache.commons.net.telnet.TelnetClient;

public class MyTelnetConnection {
    private TelnetClient client = null;
    private final String SERVER_IP;
    private final int SERVERPORT;

    public MyTelnetConnection(String ip, int port) throws IOException{
        SERVER_IP = ip;
        SERVERPORT = port;
        client = new TelnetClient();
    }

    public void connect() throws IOException{
        try {
            client.connect(SERVER_IP, SERVERPORT);
/*
            InputStream instream = client.getInputStream();
            InputStreamReader a = new InputStreamReader(instream);//spawnSpy();
            BufferedReader buf = new BufferedReader(a);
            while(buf.ready())
            { buf.read();}
            StringBuilder result = null;
            result = new StringBuilder();
            String bufstr;
            while((bufstr = buf.readLine()) != null){
                Log.d("aster", "con bufstr = "+bufstr);
                result.append(bufstr);
            }
            Log.d("aster", "con bufstr = "+bufstr);
            Log.d("aster", "con result = "+result);
*/
        } catch (SocketException ex) {
            Log.d("aster",ex.toString());
          //  throw new SocketException("Connection error...");
        }
    }

    public BufferedInputStream getReader(){
        return new BufferedInputStream(client.getInputStream());
    }

    public OutputStream getOutput(){
        return client.getOutputStream();
    }

    public boolean isConnected() {
        return client.isConnected();
    }

//    public boolean sendCommand(String cmd){
//        if(client==null || !client.isConnected()){
//            return false;
//        }
//
//        StringBuilder stringBuilder = new StringBuilder();
//
//        stringBuilder.append(cmd);
//        stringBuilder.append("\n\r");
//
//        byte[] cmdbyte = stringBuilder.toString().getBytes();
//
//        OutputStream outstream = client.getOutputStream();
//        //Log.d("aster",(new String(cmdbyte, 0, cmdbyte.length)));
//
//        try {
//            outstream.write(cmdbyte, 0, cmdbyte.length);
//            outstream.flush();
//            return true;
//        } catch (Exception e1) {
//            Log.e("Error writing to output", e1.getMessage());
//            return false;
//        }
//    }

    //exits telnet session and cleans up the telnet console
    public boolean disconnect() {
        try {
            client.disconnect();
        } catch (IOException e) {
            Log.e("Couldn't disconnect",e.getMessage());
            return false;
        }
        return true;
    }

    public TelnetClient getConnection(){
        return client;
    }
}
