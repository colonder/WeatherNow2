package pollutionData;

import org.json.JSONObject;

import data.JSONPopulator;

/**
 * Created by Jakub on 2016-06-25.
 */
public class PollutionParameters implements JSONPopulator
{

    private Data data;
    private Location location;

    @Override
    public void poopulate(JSONObject jsonObject)
    {
        data = new Data(jsonObject.optString("data"));
        location = new Location();

        location.poopulate(jsonObject.optJSONObject("location"));
    }

    public Data getData() {
        return data;
    }

    public Location getLocation() {
        return location;
    }
}
