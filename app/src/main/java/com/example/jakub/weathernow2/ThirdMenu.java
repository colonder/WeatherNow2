package com.example.jakub.weathernow2;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import java.util.Timer;
import java.util.TimerTask;

import Engine.GPSLocalisation;
import Engine.TaskParams;
import Engine.WeatherService;
import Pages.GPSPage;
import Pages.GPSPollutionPage;
import data.Parameters;

/**
 * Created by Jakub on 22.06.2016.
 */
public class ThirdMenu extends Fragment implements WeatherServiceCallback
{
    private ViewPager mPager;
    private PagerAdapter mPagerAdapter;
    private static final int NUM_PAGES = 2;
    private GPSPage gpsPage;
    private GPSPollutionPage gpsPollutionPage;
    private Handler handler;
    private Timer timer;
    private ProgressDialog progressDialog;
    private GPSLocalisation GPS;
    private TaskParams taskParams;
    private WeatherService weatherService;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //handler = new Handler();
        //timer = new Timer();

        /*progressDialog = new ProgressDialog(getContext());
        progressDialog.setMessage("Acquiring data...");
        progressDialog.show();
        GPS = new GPSLocalisation(getContext(), this);
        taskParams = new TaskParams(GPS.getLatitude(), GPS.getLongitude(), getContext());

        try
        {
            callTask(this);
        }

        catch (RuntimeException e)
        {
            serviceFailure(e);
        }*/
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        ViewGroup view = (ViewGroup) inflater.inflate(R.layout.view_pager_layout3, container, false);

        mPagerAdapter = new ScreenSlidePagerAdapter(getChildFragmentManager());
        mPager = (ViewPager) view.findViewById(R.id.pager3);
        mPager.setAdapter(mPagerAdapter);

        return view;
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
                    return gpsPage = new GPSPage();
                case 1:
                    return gpsPollutionPage = new GPSPollutionPage();
                default:
                    return gpsPage = new GPSPage();
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
                            //weatherService = new WeatherService(weatherCallback);
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
    }

    @Override
    public void serviceFailure(Exception exception)
    {
        Toast.makeText(getContext(), exception.getMessage(), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void inform(String message)
    {
        Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
    }
}
