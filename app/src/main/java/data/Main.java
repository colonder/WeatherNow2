package data;

import org.json.JSONObject;

/**
 * Created by Jakub on 13.06.2016.
 */
public class Main implements JSONPopulator {

    private double temperature;
    private int pressure;
    private int humidity;
    private int temp_min;
    private int temp_max;


    @Override
    public void poopulate(JSONObject jsonObject)
    {
        temperature = jsonObject.optDouble("temp");
        pressure = jsonObject.optInt("pressure");
        humidity = jsonObject.optInt("humidity");
        temp_min = jsonObject.optInt("temp_min");
        temp_max = jsonObject.optInt("temp_max");
    }

    public double getTemperature() {
        return temperature;
    }

    public int getPressure() {
        return pressure;
    }

    public int getHumidity()
    {
        return humidity;
    }

    public int getTemp_min() {
        return temp_min;
    }

    public int getTemp_max() {
        return temp_max;
    }
}

