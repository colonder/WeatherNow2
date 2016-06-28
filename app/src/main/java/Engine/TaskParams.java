package Engine;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

/**
 * Created by Jakub on 13.06.2016.
 */
public class TaskParams
{
    private double lat;
    private double lon;
    private static String language;
    private static String units;
    private static String accuracy;
    private int cityID;


    public TaskParams(double lat, double lon, Context context)
    {
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(context);
        this.lat = lat;
        this.lon = lon;
        language = sharedPref.getString("language", "");
        units = sharedPref.getString("units", "");
        accuracy = sharedPref.getString("accuracy", "");
    }

    public double getLatFine()
    {
        return lat;
    }

    public double getLonFine()
    {
        return lon;
    }

    public int getLatCoarse()
    {
        return ((int) lat);
    }

    public int getLonCoarse()
    {
        return ((int) lon);
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public void setLon(double lon) {
        this.lon = lon;
    }

    public static void setLanguage(String languageArg) {
        language = languageArg;
    }

    public static void setUnits(String unitsArg) {
        units = unitsArg;
    }

    public static void setAccuracy(String accuracyArg) {
        accuracy = accuracyArg;
    }

    public static String getLanguage() {
        return language;
    }

    public static String getUnits() {
        return units;
    }

    public static String getAccuracy() {
        return accuracy;
    }

    public int getCityID() {
        return cityID;
    }

    public void setCityID(int cityID) {
        this.cityID = cityID;
    }
}
