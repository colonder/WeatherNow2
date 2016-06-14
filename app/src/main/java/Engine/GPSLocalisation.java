package Engine;

import android.content.Context;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.widget.Toast;

/**
 * Created by Jakub on 13.06.2016.
 */
public class GPSLocalisation implements LocationListener
{
    private LocationManager locationManager;
    private Criteria criteria;
    private final int MY_PERMISSION_ACCESS = -12;
    private Location location;
    private Context context;
    private double latitude;
    private double longitude;

    public GPSLocalisation(Context context)
    {
        this.context = context;
        criteria = new Criteria();
        locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        refresh();
        setLocationUpdates();
    }

    public void refresh()
    {
        try
        {
            location = locationManager.getLastKnownLocation(LocationManager.PASSIVE_PROVIDER);
        }

        catch (SecurityException e)
        {
            Toast.makeText(context, "Security error", Toast.LENGTH_LONG).show();
        }
    }

    public void setLocationUpdates()
    {
        try
        {
            locationManager.requestLocationUpdates(5000, 0.1f, criteria, this, null);
            setLatitude();
            setLongitude();
        }
        catch (SecurityException e)
        {
            Toast.makeText(context, "Security error", Toast.LENGTH_LONG).show();
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
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider)
    {
        Toast.makeText(context, "GPS provider enabled", Toast.LENGTH_LONG).show();
        refresh();
    }

    @Override
    public void onProviderDisabled(String provider)
    {
        Toast.makeText(context, "GPS provider disabled", Toast.LENGTH_LONG).show();
    }
}
