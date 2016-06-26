package pollutionData;

import org.json.JSONObject;

import data.JSONPopulator;

/**
 * Created by Jakub on 2016-06-25.
 */
public class Location implements JSONPopulator
{
    private double latitude;
    private double longitude;

    @Override
    public void poopulate(JSONObject jsonObject)
    {
        latitude = jsonObject.optDouble("latitude");
        longitude = jsonObject.optDouble("longitude");
    }

    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }
}
