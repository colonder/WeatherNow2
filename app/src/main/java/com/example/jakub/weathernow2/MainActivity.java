package com.example.jakub.weathernow2;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.multidex.MultiDex;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        MultiDex.install(getApplicationContext());
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }
}
