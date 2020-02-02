package com.example.banedroid;

import android.content.ComponentName;
import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import com.example.banedroid.bane.BaneService;

public class MainActivity extends AppCompatActivity {

    public static ComponentName compName;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        compName = getComponentName();
        Intent i = new Intent(this, BaneService.class);
        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startService(i);
        finish();
    }

}
