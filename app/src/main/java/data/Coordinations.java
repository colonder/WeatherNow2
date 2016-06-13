package data;

import org.json.JSONObject;

/**
 * Created by Jakub on 13.06.2016.
 */
public class Coordinations implements JSONPopulator {

    private double latitude, longitude;

    @Override
    public void poopulate(JSONObject jsonObject) {
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
