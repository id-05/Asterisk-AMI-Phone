package com.id05.asteriskcallmedisa.util;

import java.io.BufferedInputStream;
import java.io.OutputStream;
import java.io.IOException;
import java.net.SocketException;
import android.util.Log;
import org.apache.commons.net.telnet.TelnetClient;

public class MyTelnetConnection {
    private final TelnetClient client;
    private final String SERVER_IP;
    private final int SERVERPORT;

    public MyTelnetConnection(String ip, int port) {
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
}
