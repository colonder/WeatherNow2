package com.example.jakub.weathernow2;

import android.app.ProgressDialog;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.Handler;
import android.support.multidex.MultiDex;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
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
    private String[] drawerOptions;
    private DrawerLayout mDrawerLayout;
    private ListView mDrawerList;
    private CharSequence mDrawerTitle;
    private CharSequence mTitle;
    private ActionBarDrawerToggle mDrawerToggle;


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        MultiDex.install(getApplicationContext());
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);

        mTitle = mDrawerTitle = getTitle();

        drawerOptions = getResources().getStringArray(R.array.drawerOptions);
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerList = (ListView) findViewById(R.id.left_drawer);

        mDrawerList.setAdapter(new ArrayAdapter<>(this,
                R.layout.drawer_list_item, drawerOptions));
        mDrawerList.setOnItemClickListener(new DrawerItemClickListener());

        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout, myToolbar, R.string.drawer_open,
                R.string.drawer_close)
        {
            /** Called when a drawer has settled in a completely closed state. */
            public void onDrawerClosed(View view) {
                super.onDrawerClosed(view);
                getActionBar().setTitle(mTitle);
                invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()

            }

            /** Called when a drawer has settled in a completely open state. */
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                getActionBar().setTitle(mDrawerTitle);
                invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
            }

        };

        // Set the drawer toggle as the DrawerListener
        mDrawerLayout.addDrawerListener(mDrawerToggle);

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

    /* Called whenever we call invalidateOptionsMenu() */
    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        // If the nav drawer is open, hide action items related to the content view
        boolean drawerOpen = mDrawerLayout.isDrawerOpen(mDrawerList);
        //menu.findItem(R.id.action_websearch).setVisible(!drawerOpen);
        return super.onPrepareOptionsMenu(menu);
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

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        // Sync the toggle state after onRestoreInstanceState has occurred.
        mDrawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        mDrawerToggle.onConfigurationChanged(newConfig);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Pass the event to ActionBarDrawerToggle, if it returns
        // true, then it has handled the app icon touch event
        if (mDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
        // Handle your other action bar items...

        return super.onOptionsItemSelected(item);
    }


    private class DrawerItemClickListener implements ListView.OnItemClickListener
    {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id)
        {

        }
    }
}
