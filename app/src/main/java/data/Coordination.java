package data;

import org.json.JSONObject;

/**
 * Created by Jakub on 13.06.2016.
 */
public class Coordination implements JSONPopulator
{

    private double latitude, longitude;

    @Override
    public void poopulate(JSONObject jsonObject)
    {
        latitude = jsonObject.optDouble("lat");
        longitude = jsonObject.optDouble("lon");
    }

    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }
}
