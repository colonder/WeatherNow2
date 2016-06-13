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

    public GPSLocalisation(Context Context)
    {
        this.context = context;
        criteria = new Criteria();
        locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        refresh();
    }

    private void refresh()
    {
        try
        {
            location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        }

        catch (SecurityException e)
        {
            Toast.makeText(context, "Security error", Toast.LENGTH_LONG).show();
        }
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
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }
}
