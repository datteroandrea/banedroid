package com.example.banedroid.connection;

import com.example.banedroid.R;
import com.example.banedroid.bane.BaneService;
import java.util.concurrent.TimeUnit;

public class ConnectionInitializer extends Thread {

    private final String name;

    public ConnectionInitializer(String name){
        this.name = name;
    }

    @Override
    public void run() {
        System.out.println("Trying connecting to Bane Service please wait...");
        try {
            TimeUnit.SECONDS.sleep(3);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        String address = BaneService.context.getResources().getString(R.string.address);
        int port = Integer.parseInt(BaneService.context.getResources().getString(R.string.port));
        new Thread(new Connection(name,address,port)).start();
    }

}
