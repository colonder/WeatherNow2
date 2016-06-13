package data;

import org.json.JSONObject;

/**
 * Created by Jakub on 13.06.2016.
 */
public class Rain implements JSONPopulator {

    private int last3H;

    @Override
    public void poopulate(JSONObject jsonObject)
    {
        last3H = jsonObject.optInt("3h");
    }

    public int getLast3H() {
        return last3H;
    }
}
