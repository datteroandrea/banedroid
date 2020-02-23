package com.example.banedroid.connection;

import com.bane.model.*;
import com.example.banedroid.dispatcher.*;
import java.io.*;
import java.net.*;

// TODO: Keep-Alive Connection, Change output and input stream into Object streams so you can share BaneObjects
public class Connection extends Thread {

    private static Connection connection;
    private Socket socket;
    private ObjectInputStream input;
    private ObjectOutputStream output;
    private Dispatcher dispatcher;
    private final String name;
    private final String address;
    private final int port;

    private Connection(String name, String address, int port){
        this.name = name;
        this.address = address;
        this.port = port;
        this.dispatcher = new DefaultDispatcher(this);
    }

    public static  Connection Connection(String name, String address, int port) {
        if(connection == null)
            connection = new Connection(name, address, port);
        return connection;
    }

    @Override
    public void run() {
        try {
            System.out.println("Initializing connection");
            // initializes the socket and the streams
            initConnection();
            System.out.println("Connection established");
            // sends the id of the device
            sendIdentification();
            System.out.println("Connected to Bane Service");
            while(true){
                // listens for command from the bane service and dispatches them
                BaneObject request = dispatcher.dispatch(read());
                // sends the response to the bane server
                this.send(request);
            }
        } catch (Exception e) {
            // on connection failed tries reconnecting
            System.out.println("Trying connecting to Bane Service please wait...");
            new Thread(new Connection(name,address,port)).start();
        }
    }

    // init the connection
    private void initConnection() throws Exception {
        this.socket = new Socket(address, port);
        this.input = new ObjectInputStream(this.socket.getInputStream());
        this.output = new ObjectOutputStream(this.socket.getOutputStream());
    }

    // sends the id of the device
    private void sendIdentification() throws Exception {
        this.output.writeObject(new BaneObject(BaneCode.IDENTIFIER,"",new BaneValue[]{new BaneValue<String>("name",this.name)}));
        this.output.flush();
    }

    // sends responses to the server
    private void send(BaneObject response) throws Exception {
        this.output.writeObject(response);
        this.output.flush();
    }

    // reads data from the server
    private BaneObject read() throws Exception {
        return (BaneObject) this.input.readObject();
    }

    public void setDispatcher(Dispatcher dispatcher){
        this.dispatcher = dispatcher;
    }

}
