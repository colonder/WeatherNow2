package com.example.jakub.weathernow2;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import java.util.Timer;
import java.util.TimerTask;

import Engine.GPSLocalisation;
import Engine.TaskParams;
import Engine.WeatherService;
import Pages.ForecastPage;
import Pages.PollutionPage;
import data.Parameters;

/**
 * Created by Jakub on 22.06.2016.
 */
public class FirstMenu extends FragmentActivity implements WeatherServiceCallback
{
    private ViewPager mPager;
    private PagerAdapter mPagerAdapter;
    private static final int NUM_PAGES = 2;
    private PollutionPage pollutionPage;
    private ForecastPage forecastPage;
    private Handler handler;
    private Timer timer;
    private ProgressDialog progressDialog;
    private GPSLocalisation GPS;
    private TaskParams taskParams;
    private WeatherService weatherService;

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        mPager = (ViewPager) findViewById(R.id.pager);
        mPagerAdapter = new ScreenSlidePagerAdapter(getSupportFragmentManager());
        mPager.setAdapter(mPagerAdapter);
        mPager.addOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                invalidateOptionsMenu();
            }
        });

        handler = new Handler();
        timer = new Timer();

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Acquiring data...");
        progressDialog.show();
        GPS = new GPSLocalisation(this, this);
        taskParams = new TaskParams(GPS.getLatitude(), GPS.getLongitude(), this);

        try
        {
            callTask(this);
        }

        catch (RuntimeException e)
        {
            serviceFailure(e);
        }
    }

    @Override
    public void onBackPressed()
    {
        if (mPager.getCurrentItem() == 0)
        {
            // If the user is currently looking at the first step, allow the system to handle the
            // Back button. This calls finish() on this activity and pops the back stack.
            super.onBackPressed();
        }

        else
        {
            // Otherwise, select the previous step.
            mPager.setCurrentItem(mPager.getCurrentItem() - 1);
        }
    }

    private class ScreenSlidePagerAdapter extends FragmentStatePagerAdapter
    {
        public ScreenSlidePagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position)
        {
            switch(position)
            {
                case 0:
                    return forecastPage = new ForecastPage();
                case 1:
                    return pollutionPage = new PollutionPage();
                default:
                    return forecastPage = new ForecastPage();
            }
        }

        @Override
        public int getCount()
        {
            return NUM_PAGES;
        }
    }

    public void callTask(final WeatherServiceCallback weatherCallback)
    {
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
    public void serviceSuccess(Parameters parameters)
    {
        progressDialog.hide();
        forecastPage.updateLabels(parameters);
    }

    @Override
    public void serviceFailure(Exception exception)
    {
        progressDialog.hide();
        progressDialog.setMessage(exception.getMessage());
        progressDialog.show();
    }

    @Override
    public void inform(String message)
    {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }
}
