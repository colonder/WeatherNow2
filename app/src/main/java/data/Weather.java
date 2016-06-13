package data;

import org.json.JSONObject;

/**
 * Created by Jakub on 13.06.2016.
 */
public class Weather implements JSONPopulator {

    private int weatherID;
    private String groupParameters;
    private String description;
    private String iconID;

    @Override
    public void poopulate(JSONObject jsonObject) {
        weatherID = jsonObject.optInt("id");
        groupParameters = jsonObject.optString("main");
        description = jsonObject.optString("description");
        iconID = jsonObject.optString("icon");
    }

    public int getWeatherID() {
        return weatherID;
    }

    public String getGroupParameters() {
        return groupParameters;
    }

    public String getDescription() {
        return description;
    }

    public String getIconID() {
        return iconID;
    }
}
