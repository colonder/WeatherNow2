package com.example.jakub.weathernow2;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.NavigationView;
import android.support.multidex.MultiDex;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import java.util.Timer;
import java.util.TimerTask;

import Engine.ForecastPage;
import Engine.GPSLocalisation;
import Engine.PollutionPage;
import Engine.TaskParams;
import Engine.WeatherService;
import data.Parameters;

public class MainActivity extends AppCompatActivity implements WeatherServiceCallback
{
    private GPSLocalisation GPS;
    private TaskParams taskParams;
    private WeatherService weatherService;
    private ProgressDialog progressDialog;
    private NavigationView mNavigationView;
    private DrawerLayout mDrawerLayout;
    private Toolbar toolbar;
    private ActionBarDrawerToggle mDrawerToggle;
    private static final int NUM_PAGES = 2;
    private ViewPager mPager;
    private PagerAdapter mPagerAdapter;
    private ForecastPage forecastPage;
    private PollutionPage pollutionPage;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        MultiDex.install(getApplicationContext());
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initDrawerNavigation();

        mPager = (ViewPager) findViewById(R.id.pager);
        mPagerAdapter = new ScreenSlidePagerAdapter(getSupportFragmentManager());
        mPager.setAdapter(mPagerAdapter);
        mPager.addOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                // When changing pages, reset the action bar actions since they are dependent
                // on which page is currently active. An alternative approach is to have each
                // fragment expose actions itself (rather than the activity exposing actions),
                // but for simplicity, the activity provides the actions in this sample.
                invalidateOptionsMenu();
            }
        });

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

    public void initDrawerNavigation()
    {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle(R.string.app_name);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mNavigationView = (NavigationView) findViewById(R.id.navigation_view);
        mNavigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {

            @Override
            public boolean onNavigationItemSelected(MenuItem item) {
                item.setChecked(true);
                switch (item.getItemId()) {
                    case R.id.nav_dyn_gps:
                        inform("Dynamic GPS selected");
                        break;

                    case R.id.nav_city:
                        inform("City selected");
                        break;

                    case R.id.nav_provided_gps:
                        inform("Provided GPS selected");
                        break;

                    case R.id.nav_settings:
                        Intent i = new Intent(getApplicationContext(), SettingsActivity.class);
                        startActivity(i);
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
                mDrawerLayout.openDrawer(GravityCompat.START);  // OPEN DRAWER
                return true;
        }
        return super.onOptionsItemSelected(item);
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
}
