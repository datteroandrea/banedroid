package com.example.banedroid.connection;

import com.example.banedroid.connection.dispatcher.DefaultDispatcher;
import com.example.banedroid.connection.dispatcher.Dispatcher;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.PrintWriter;
import java.net.Socket;

// TODO: Keep-Alive Connection, Send Identification and Receive data
public class Connection implements Runnable {

    private Socket connection;
    private DataOutputStream writer;
    private DataInputStream reader;
    private final String name;
    private final String address;
    private final int port;
    private Dispatcher dispatcher;

    public Connection(String name, String address, int port) {
        this.name = name;
        this.address = address;
        this.port = port;
        this.dispatcher = new DefaultDispatcher(this);
    }

    @Override
    public void run() {
        System.out.println("Inititiating connection to Bane Service...");
        try {
            initConnection();
            System.out.println("Sending ID");
            sendIdentification();
            System.out.println("Connected to Bane Service");
            while(true){
                String response = dispatcher.dispatch(read());
                this.send(response);
            }
        } catch (Exception e) {
            onConnectionFailed();
        }
    }

    private void onConnectionFailed(){
        new ConnectionInitializer(this.name).start();
    }

    private void initConnection() throws Exception {
        this.connection = new Socket(address, port);
        this.reader = new DataInputStream(this.connection.getInputStream());
        this.writer = new DataOutputStream(this.connection.getOutputStream());
    }

    private void sendIdentification() throws Exception {
        writer.writeUTF(this.name);
        writer.flush();
    }

    private String read() throws Exception {
        return this.reader.readUTF();
    }

    private void send(String response) throws Exception {
        this.writer.writeUTF(response);
    }

}
