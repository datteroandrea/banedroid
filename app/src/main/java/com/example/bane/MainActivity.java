package com.example.bane;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    public static ComponentName compName;
    public static Activity activity;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activity = this;
        compName = getComponentName();
        Intent i = new Intent(this, BaneService.class);
        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startService(i);
        finish();
    }

}
