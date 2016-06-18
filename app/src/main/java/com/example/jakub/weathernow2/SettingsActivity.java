package com.example.jakub.weathernow2;

import android.app.Activity;
import android.os.Bundle;
import Engine.SettingsFragment;

/**
 * Created by Jakub on 18.06.2016.
 */
public class SettingsActivity extends Activity
{
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        // Display the fragment as the main content.
        getFragmentManager().beginTransaction().replace(android.R.id.content,
                new SettingsFragment()).commit();
    }
}
