package com.example.jakub.weathernow2;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.multidex.MultiDex;

import Engine.GPSLocalisation;
import Engine.TaskParams;
import Engine.WeatherService;
import data.Parameters;

public class MainActivity extends AppCompatActivity implements WeatherServiceCallback
{

    private GPSLocalisation GPS;
    private TaskParams taskParams;
    private WeatherService weatherService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        MultiDex.install(getApplicationContext());
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        GPS = new GPSLocalisation(this);
        taskParams = new TaskParams(GPS.getLatitude(), GPS.getLongitude());
        weatherService = new WeatherService(this);
        weatherService.execute(taskParams);
    }

    @Override
    public void serviceSuccess(Parameters parameters)
    {

    }

    @Override
    public void serviceFailure(Exception exception)
    {

    }
}
