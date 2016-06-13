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
    private TextView pressureTextView, humidityTextView, tempMaxTextView, tempMinTextView,
    groupDescTextView, descriptionTextView, cloudsTextView, rainTextView, snowTextView,
            windSpeedTextView, windAngleTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        MultiDex.install(getApplicationContext());
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        GPSTextView = (TextView) findViewById(R.id.GPSTextView);
        locationTextView = (TextView) findViewById(R.id.locationTextView);
        temperatureTextView = (TextView) findViewById(R.id.temperatureTextView);
        pressureTextView = (TextView) findViewById(R.id.pressureTextView);
        humidityTextView = (TextView) findViewById(R.id.humidityTextView);
        tempMaxTextView = (TextView) findViewById(R.id.tempMaxTextView);
        tempMinTextView = (TextView) findViewById(R.id.tempMinTextView);
        groupDescTextView = (TextView) findViewById(R.id.groupDescTextView);
        descriptionTextView = (TextView) findViewById(R.id.descriptionTextView);
        cloudsTextView = (TextView) findViewById(R.id.cloudsTextView);
        rainTextView = (TextView) findViewById(R.id.rainTextView);
        snowTextView = (TextView) findViewById(R.id.snowTextView);
        windSpeedTextView = (TextView) findViewById(R.id.windSpeedTextView);
        windAngleTextView = (TextView) findViewById(R.id.windAngleTextView);

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
        temperatureTextView.setText("Temperature: " + parameters.getMain().getTemperature() + " C");
        pressureTextView.setText("Pressure: " + parameters.getMain().getPressure() + " hPa");
        humidityTextView.setText("Humidity: " + parameters.getMain().getHumidity() + "%");
        tempMaxTextView.setText("Max temp: " + parameters.getMain().getTemp_max() + " C");
        tempMinTextView.setText("Min temp: " + parameters.getMain().getTemp_min() + " C");
        groupDescTextView.setText(parameters.getWeather().getGroupParameters());
        descriptionTextView.setText(parameters.getWeather().getDescription());
        cloudsTextView.setText("Cloudiness: " + parameters.getClouds().getCloudiness());
        rainTextView.setText("Rain in last 3 hours: " + parameters.getRain().getLast3H());
        snowTextView.setText("Snow in last 3 hours: " + parameters.getSnow().getLast3H());
        windSpeedTextView.setText("Wind speed: " + parameters.getWind().getSpeed() + " m/s");
        windAngleTextView.setText("Wind angle: " + parameters.getWind().getDegrees() + " degrees");
    }

    @Override
    public void serviceFailure(Exception exception)
    {
        progressDialog.hide();
        Toast.makeText(this, exception.getMessage(), Toast.LENGTH_LONG).show();
    }
}
