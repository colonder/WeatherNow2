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
import Pages.CityPage;
import Pages.CityPollutionPage;
import data.Parameters;

/**
 * Created by Jakub on 22.06.2016.
 */
public class SecondMenu extends Fragment
{
    private ViewPager mPager;
    private PagerAdapter mPagerAdapter;
    private static final int NUM_PAGES = 2;
    private CityPollutionPage cityPollutionPage;
    private CityPage cityPage;

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        ViewGroup view = (ViewGroup) inflater.inflate(R.layout.view_pager_layout2, container, false);

        mPagerAdapter = new ScreenSlidePagerAdapter(getChildFragmentManager());
        mPager = (ViewPager) view.findViewById(R.id.pager2);
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
                    return cityPage = new CityPage();
                case 1:
                    return cityPollutionPage = new CityPollutionPage();
                default:
                    return cityPage = new CityPage();
            }
        }

        @Override
        public int getCount()
        {
            return NUM_PAGES;
        }
    }
}
