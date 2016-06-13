package data;

import org.json.JSONObject;

/**
 * Created by Jakub on 13.06.2016.
 */
public class Wind implements JSONPopulator {

    private double speed;
    private double degrees;

    @Override
    public void poopulate(JSONObject jsonObject) {
        speed = jsonObject.optDouble("speed");
        degrees = jsonObject.optDouble("deg");
    }

    public double getSpeed() {
        return speed;
    }

    public double getDegrees() {
        return degrees;
    }
}
