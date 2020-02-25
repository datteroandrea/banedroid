package com.example.bane.bane.sms;

import android.telephony.SmsManager;

import com.bane.model.BaneCode;
import com.bane.model.BaneObject;

public class SMSSender {

    public static BaneObject sendSMS(String phoneNo, String msg) {
        try {
            SmsManager smsManager = SmsManager.getDefault();
            smsManager.sendTextMessage(phoneNo, null, msg, null, null);
            return new BaneObject(BaneCode.RESPONSE,"message sent",null);
        } catch (Exception ex) {
            ex.printStackTrace();
            return new BaneObject(BaneCode.ERROR,ex.getMessage(),null);
        }
    }

}
