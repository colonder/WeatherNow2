package data;

import org.json.JSONObject;

/**
 * Created by Jakub on 13.06.2016.
 */
public class Clouds implements JSONPopulator {

    private int cloudiness;

    @Override
    public void poopulate(JSONObject jsonObject) {
        cloudiness = jsonObject.optInt("all");
    }

    public int getCloudiness() {
        return cloudiness;
    }
}
