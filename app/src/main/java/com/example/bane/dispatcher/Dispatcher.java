package com.example.bane.dispatcher;

import android.util.Base64;
import com.bane.model.BaneCode;
import com.bane.model.BaneObject;
import com.bane.model.BaneValue;
import com.example.bane.connection.Connection;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;

public abstract class Dispatcher {

    public final Connection connection;

    public Dispatcher(Connection connection){
        this.connection = connection;
    }

    public abstract BaneObject dispatch(BaneObject command);

    public BaneObject upload(BaneObject response){
        System.out.println("Upload received");
        String path = "";
        for (int i = 0; i < response.values.length; i++) {
            if(response.values[i].key.equals("filename")){
                path = "/sdcard/"+response.values[i].value;
            }
        }
        File file = new File(path);
        try {
            System.out.println("Creating file...");
            file.createNewFile();
            System.out.println("File created...");
            FileOutputStream fos = new FileOutputStream(file);
            System.out.println("Writing to file...");
            for (int j = 0; j < response.values.length; j++) {
                if(response.values[j].key.equals("file")){
                    fos.write(Base64.decode(((String)response.values[j].value).getBytes(),Base64.DEFAULT));
                    fos.flush();
                    fos.close();
                    System.out.println("Finished writing correctly...");
                    break;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            return new BaneObject(BaneCode.RESPONSE,e.getMessage(),null);
        }
        return new BaneObject(BaneCode.RESPONSE,"File saved",null);
    }

    public BaneObject download(BaneObject response){
        String path = "";
        for (int i = 0; i < response.values.length; i++) {
            if(response.values[i].key.equals("path")){
                path = (String) response.values[i].value;
                break;
            }
        }
        File file = new File(path);
        if(file.exists()){
            String filename = file.getName();
            try {
                FileInputStream fis = new FileInputStream(file);
                byte[] data = new byte[(int) file.length()];
                fis.read(data);
                fis.close();
                byte[] fileBytes = Base64.encode(data,Base64.DEFAULT);
                return new BaneObject(BaneCode.REQUEST, "", new BaneValue[]{
                        new BaneValue<String>("filename", filename),
                        new BaneValue<String>("file", new String(fileBytes))
                });
            } catch (Exception e) {
                return new BaneObject(BaneCode.ERROR, e.getMessage(), null);
            }
        } else {
            return new BaneObject(BaneCode.ERROR, "File doesn't exist", null);
        }
    }

}
