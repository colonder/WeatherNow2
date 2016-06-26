package pollutionData;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Jakub on 2016-06-25.
 */
public class Data
{
    private double precision;
    private double value;
    private double pressure;

    public Data(String input)
    {
        try
        {
            JSONArray jsonArray = new JSONArray(input);
            JSONObject data = jsonArray.getJSONObject(0);
            precision = data.optDouble("precision");
            value = data.optDouble("value");
            pressure = data.optDouble("pressure");
        }

        catch (JSONException e)
        {
            e.printStackTrace();
        }
    }

    public double getPrecision() {
        return precision;
    }

    public double getValue() {
        return value;
    }

    public double getPressure() {
        return pressure;
    }
}
