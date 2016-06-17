package com.example.jakub.weathernow2;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.os.Handler;
import android.support.multidex.MultiDex;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Timer;
import java.util.TimerTask;

import Engine.FlyOutContainer;
import Engine.GPSLocalisation;
import Engine.TaskParams;
import Engine.WeatherService;
import data.Parameters;

public class MainActivity extends AppCompatActivity implements WeatherServiceCallback
{
    FlyOutContainer flyOutContainer;
    private GPSLocalisation GPS;
    private TaskParams taskParams;
    private WeatherService weatherService;
    private ProgressDialog progressDialog;

    private TextView GPSTextView;
    private TextView locationTextView;
    private TextView temperatureTextView;
    private TextView pressureTextView, humidityTextView, tempMaxTextView, tempMinTextView,
    groupDescTextView, descriptionTextView, cloudsTextView, rainTextView, snowTextView,
            windSpeedTextView, windAngleTextView, GPSUpdate, AsyncTaskUpdate;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        MultiDex.install(getApplicationContext());
        super.onCreate(savedInstanceState);
        flyOutContainer = new FlyOutContainer(this);
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
        GPSUpdate = (TextView) findViewById(R.id.GPSUpdate);
        AsyncTaskUpdate = (TextView) findViewById(R.id.AsyncTaskUpdate);

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Acquiring data...");
        progressDialog.show();
        GPS = new GPSLocalisation(this, this);
        taskParams = new TaskParams(GPS.getLatitude(), GPS.getLongitude());

        try
        {
            callTask(this);
        }

        catch (RuntimeException e)
        {
            serviceFailure(e);
        }
    }

    public boolean onCreateOptionsMenu(Menu menu)
    {
        getMenuInflater().inflate(R.menu.slidingmenu, menu);
        return true;
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
        Toast.makeText(this, exception.getMessage(), Toast.LENGTH_LONG).show();
    }

    @Override
    public void inform(String message)
    {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    public void callTask(final WeatherServiceCallback weatherCallback)
    {
        final Handler handler = new Handler();
        Timer timer = new Timer();
        TimerTask doAsyncTask = new TimerTask()
        {
            @Override
            public void run()
            {
                handler.post(new Runnable()
                {
                    @Override
                    public void run()
                    {
                        try
                        {
                            weatherService = new WeatherService(weatherCallback);
                            taskParams.setLat(GPS.getLatitude());
                            taskParams.setLon(GPS.getLongitude());
                            weatherService.execute(taskParams);
                        }

                        catch (Exception e)
                        {
                            e.printStackTrace();
                        }
                    }
                });
            }
        };

        timer.schedule(doAsyncTask, 0, 5000);
    }

    public void toggleMenu(View v)
    {
        this.flyOutContainer.toggleMenu();
    }

}
