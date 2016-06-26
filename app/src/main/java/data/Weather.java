package data;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Jakub on 13.06.2016.
 */
public class Weather
{

    private int weatherID;
    private String groupParameters;
    private String description;
    private String iconID;

    public Weather(String input)
    {
        try
        {
            JSONArray jsonArray = new JSONArray(input);
            JSONObject data = jsonArray.getJSONObject(0);
            weatherID = data.optInt("id");
            groupParameters = data.optString("main");
            description = data.optString("description");
            iconID = data.optString("icon");
        }

        catch (JSONException e)
        {
            e.printStackTrace();
        }
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
