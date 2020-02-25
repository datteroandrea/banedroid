package com.example.bane.bane.storage;

import android.content.Context;
import android.content.SharedPreferences;

public class Storage {

    private final Context context;
    private static Storage storage;
    private static SharedPreferences preferencces;

    public Storage(Context context) {
        this.context = context;
        this.preferencces = this.context.getSharedPreferences("",0);
    }

    public static Storage Storage(Context context){
        if(storage == null)
            storage = new Storage(context);
        return storage;
    }

    public boolean isStored(String value){
        return this.preferencces.contains(value);
    }

    public String get(String value,String defaultValue) {
        if(isStored(value))
            return this.preferencces.getString(value, null);
        else
            return defaultValue;
    }

    public void set(String key, String value) {
        this.preferencces.edit().putString(key, value).apply();
    }

    public void remove(String key) {
        if(isStored(key))
            this.preferencces.edit().remove(key).apply();
    }

}

