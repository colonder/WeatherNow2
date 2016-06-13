package com.example.jakub.weathernow2;

import android.app.ProgressDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.multidex.MultiDex;
import android.widget.TextView;
import android.widget.Toast;

import Engine.GPSLocalisation;
import Engine.TaskParams;
import Engine.WeatherService;
import data.Parameters;

public class MainActivity extends AppCompatActivity implements WeatherServiceCallback
{
    private GPSLocalisation GPS;
    private TaskParams taskParams;
    private WeatherService weatherService;
    private ProgressDialog progressDialog;

    private TextView GPSTextView;
    private TextView locationTextView;
    private TextView temperatureTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        MultiDex.install(getApplicationContext());
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        GPSTextView = (TextView) findViewById(R.id.GPSTextView);
        locationTextView = (TextView) findViewById(R.id.locationTextView);
        temperatureTextView = (TextView) findViewById(R.id.temperatureTextView);

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Acquiring data...");
        progressDialog.show();

        GPS = new GPSLocalisation(this);
        taskParams = new TaskParams(GPS.getLatitude(), GPS.getLongitude());
        weatherService = new WeatherService(this);
        weatherService.execute(taskParams);
    }

    @Override
    public void serviceSuccess(Parameters parameters)
    {
        progressDialog.hide();
        GPSTextView.setText(GPS.getLatitude() + ", " + GPS.getLongitude());
        locationTextView.setText(parameters.getCityName());
        temperatureTextView.setText(parameters.getMain().getTemperature() + " C");
    }

    @Override
    public void serviceFailure(Exception exception)
    {
        progressDialog.hide();
        Toast.makeText(this, exception.getMessage(), Toast.LENGTH_LONG).show();
    }
}
