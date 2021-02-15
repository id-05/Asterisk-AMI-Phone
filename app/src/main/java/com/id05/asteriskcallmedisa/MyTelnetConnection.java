package com.id05.asteriskcallmedisa;

import java.io.BufferedInputStream;
import java.io.OutputStream;
import java.io.IOException;
import java.net.SocketException;
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
        } catch (SocketException ex) {
            Log.d("aster",ex.toString());
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
