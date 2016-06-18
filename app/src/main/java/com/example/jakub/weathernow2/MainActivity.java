package com.example.jakub.weathernow2;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.NavigationView;
import android.support.multidex.MultiDex;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Timer;
import java.util.TimerTask;

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
    private NavigationView mNavigationView;
    private DrawerLayout mDrawerLayout;
    private Toolbar toolbar;
    private ActionBarDrawerToggle mDrawerToggle;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        MultiDex.install(getApplicationContext());
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initDrawerNavigation();

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

    public void initDrawerNavigation()
    {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle(R.string.app_name);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        //getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_drawer);
        //getSupportActionBar().setDisplayShowHomeEnabled(true);

        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mNavigationView = (NavigationView) findViewById(R.id.navigation_view);
        mNavigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {

            @Override
            public boolean onNavigationItemSelected(MenuItem item) {
                item.setChecked(true);
                switch (item.getItemId()) {
                    case R.id.nav_dyn_gps:
                        break;

                    case R.id.nav_city:
                        break;

                    case R.id.nav_provided_gps:
                        break;
                }
                mDrawerLayout.closeDrawers();
                return true;
            }
        });

        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout, toolbar, R.string.drawer_open,
                R.string.drawer_close)
        {
            // Called when a drawer has settled in a completely closed state.
            public void onDrawerClosed(View view)
            {
                super.onDrawerClosed(view);
            }

            // Called when a drawer has settled in a completely open state.
            public void onDrawerOpened(View drawerView)
            {
                super.onDrawerOpened(drawerView);
            }
        };

        mDrawerLayout.addDrawerListener(mDrawerToggle);
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState)
    {
        super.onPostCreate(savedInstanceState);
        mDrawerToggle.syncState();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){

        switch(item.getItemId())
        {
            case android.R.id.home:
                inform("Home button tapped");
                //mDrawerLayout.openDrawer(GravityCompat.START);  // OPEN DRAWER
                return true;
        }
        return super.onOptionsItemSelected(item);
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
}
