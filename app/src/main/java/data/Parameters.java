package data;

import org.json.JSONObject;

/**
 * Created by Jakub on 13.06.2016.
 */
public class Parameters implements JSONPopulator {

    private Coordination coordination;
    private Weather weather;
    private Main main;
    private Wind wind;
    private Clouds clouds;
    private Rain rain;
    private Snow snow;
    private long calcTime;
    private Sys sys;
    private long cityID;
    private String cityName;

    @Override
    public void poopulate(JSONObject jsonObject)
    {
        coordination = new Coordination();
        weather = new Weather(jsonObject.optString("weather"));
        main = new Main();
        wind = new Wind();
        clouds = new Clouds();
        rain = new Rain();
        snow = new Snow();
        sys = new Sys();

        coordination.poopulate(jsonObject.optJSONObject("coord"));
        main.poopulate(jsonObject.optJSONObject("main"));
        wind.poopulate(jsonObject.optJSONObject("wind"));
        clouds.poopulate(jsonObject.optJSONObject("clouds"));

        try
        {
            rain.poopulate(jsonObject.optJSONObject("rain"));
        }

        catch (NullPointerException e)
        {

        }

        try
        {
            snow.poopulate(jsonObject.optJSONObject("snow"));
        }

        catch(NullPointerException e)
        {

        }

        calcTime = jsonObject.optLong("dt");
        sys.poopulate(jsonObject.optJSONObject("sys"));
        cityID = jsonObject.optLong("id");
        cityName = jsonObject.optString("name");
    }

    public Coordination getCoordination() {
        return coordination;
    }

    public Weather getWeather() {
        return weather;
    }

    public Main getMain() {
        return main;
    }

    public Wind getWind() {
        return wind;
    }

    public Clouds getClouds() {
        return clouds;
    }

    public Rain getRain() {
        return rain;
    }

    public Snow getSnow() {
        return snow;
    }

    public long getCalcTime() {
        return calcTime;
    }

    public Sys getSys() {
        return sys;
    }

    public long getCityID() {
        return cityID;
    }

    public String getCityName() {
        return cityName;
    }
}
