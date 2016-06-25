package Engine;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.widget.Toast;

import com.example.jakub.weathernow2.R;
import com.example.jakub.weathernow2.WeatherServiceCallback;

/**
 * Created by Jakub on 13.06.2016.
 */
public class GPSLocalisation implements LocationListener
{
    private LocationManager locationManager;
    private Criteria criteria;
    private Location location;
    private Context context;
    private double latitude;
    private double longitude;
    private WeatherServiceCallback callback;
    private SharedPreferences sharedPref;

    public GPSLocalisation(Context context, WeatherServiceCallback callback)
    {
        this.callback = callback;
        this.context = context;
        sharedPref = PreferenceManager.getDefaultSharedPreferences(context);
        criteria = new Criteria();
        locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        refresh();
        setLocationUpdates();
    }

    public void refresh()
    {
        try
        {
            if(!sharedPref.getBoolean("only_wifi", false))
            {
                location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);

                if (location == null) {
                    callback.inform(context.getResources().getString(R.string.network_fail));
                    location = locationManager.getLastKnownLocation(LocationManager.PASSIVE_PROVIDER);

                    if (location == null) {
                        callback.inform(context.getResources().getString(R.string.passive_fail));
                        location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                    }
                }
            }

            else
            {
                location = locationManager.getLastKnownLocation(LocationManager.PASSIVE_PROVIDER);
            }
        }

        catch (SecurityException e)
        {
            callback.serviceFailure(e);
        }
    }

    public void setLocationUpdates()
    {
        try
        {
            locationManager.requestLocationUpdates(5000, 1, criteria, this, null);
            setLatitude();
            setLongitude();
        }
        catch (SecurityException e)
        {
            callback.serviceFailure(e);
        }

        catch (NullPointerException e)
        {
            AlertDialog.Builder builder = new AlertDialog.Builder(context);
            builder.setMessage(R.string.location_failure).setTitle(R.string.location_dialog_title);
            builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    refresh();
                    setLocationUpdates();
                }
            });
            builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    callback.inform("Location unknown");
                }
            });

            builder.create().show();
        }
    }

    public double getLatitude()
    {
        return latitude;
    }

    public void setLatitude()
    {
        latitude = location.getLatitude();
    }

    public double getLongitude()
    {
        return longitude;
    }

    public void setLongitude()
    {
        longitude = location.getLongitude();
    }

    @Override
    public void onLocationChanged(Location location)
    {
        this.location = location;
        setLatitude();
        setLongitude();
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider)
    {
        Toast.makeText(context, "Provider enabled", Toast.LENGTH_LONG).show();
        refresh();
    }

    @Override
    public void onProviderDisabled(String provider)
    {
        Toast.makeText(context, "Provider disabled", Toast.LENGTH_LONG).show();
    }
}
