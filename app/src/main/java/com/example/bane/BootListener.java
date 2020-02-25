package com.example.bane;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import androidx.annotation.RequiresApi;

import com.example.bane.BaneService;

import java.util.Objects;

public class BootListener extends BroadcastReceiver {

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
