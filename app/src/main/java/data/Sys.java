package data;

import org.json.JSONObject;

/**
 * Created by Jakub on 13.06.2016.
 */
public class Sys implements JSONPopulator {

    private String countryCode;
    private long sunrise;
    private long sunset;

    @Override
    public void poopulate(JSONObject jsonObject) {
        countryCode = jsonObject.optString("country");
        sunrise = jsonObject.optLong("sunrise");
        sunset = jsonObject.optLong("sunset");
    }

    public String getCountryCode() {
        return countryCode;
    }

    public long getSunrise() {
        return sunrise;
    }

    public long getSunset() {
        return sunset;
    }
}
