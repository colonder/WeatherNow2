package Engine;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceFragment;

import com.example.jakub.weathernow2.FirstMenu;
import com.example.jakub.weathernow2.R;

import Pages.CityPage;
import Pages.ForecastPage;

/**
 * Created by Jakub on 18.06.2016.
 */
public class SettingsFragment extends PreferenceFragment implements SharedPreferences.OnSharedPreferenceChangeListener
{
    private FirstMenu firstMenu;
    private CityPage cityPage;

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        // Load the preferences from an XML resource
        addPreferencesFromResource(R.xml.preferences);
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key)
    {
        if (key.equals("language"))
        {
            // Set summary to be the user-description for the selected value
            Preference langPref = findPreference(key);
            TaskParams.setLanguage(sharedPreferences.getString(key, "english"));
        }

        if(key.equals("units"))
        {
            Preference unitPref = findPreference(key);
            unitPref.setSummary(sharedPreferences.getString(key, ""));
            TaskParams.setUnits(sharedPreferences.getString(key, "metric"));
        }

        if(key.equals("accuracy"))
        {
            Preference accPref = findPreference(key);
            accPref.setSummary(sharedPreferences.getString(key, ""));
            TaskParams.setAccuracy(sharedPreferences.getString(key, "accurate"));
        }

        if(key.equals("detailed"))
        {
            Preference detailPref = findPreference(key);
            ForecastPage.setDetailed(sharedPreferences.getBoolean(key, false));
            CityPage.setDetailed(sharedPreferences.getBoolean(key, false));
        }

        sharedPreferences.registerOnSharedPreferenceChangeListener(this);
    }

    @Override
    public void onResume() {
        super.onResume();
        getPreferenceScreen().getSharedPreferences()
                .registerOnSharedPreferenceChangeListener(this);
    }

    @Override
    public void onPause() {
        super.onPause();
        getPreferenceScreen().getSharedPreferences()
                .unregisterOnSharedPreferenceChangeListener(this);
    }
}
