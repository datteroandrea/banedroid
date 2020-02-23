package com.example.banedroid.bane.notifications;

import android.content.Intent;
import com.example.banedroid.service.BaneService;

import java.util.ArrayList;

public class NotificationManager {

    private static NotificationManager notificationManager;
    private ArrayList<String> notifications = new ArrayList<>();

    private NotificationManager(){
        BaneService.context.startService(new Intent(BaneService.context, NotificationService.class));
    }

    public static NotificationManager NotificationManager(){
        if (notificationManager == null)
            notificationManager = new NotificationManager();
        return notificationManager;
    }

    public void push(ArrayList<String> notifications){
        if(this.notifications.size() >= 1000){
            for(int i = 0; i < notifications.size(); i++){
                this.notifications.remove(0);
                this.notifications.add(notifications.get(i));
            }
        } else {
            this.notifications.addAll(notifications);
        }
    }

    public String flush(){
        String response = "Current notifications: ";
        for (int i = 0; i < this.notifications.size(); i++) {
            response += notifications.get(i);
            notifications.remove(i);
        }
        return response;
    }

}
