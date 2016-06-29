package Pages;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.jakub.weathernow2.R;

import Engine.TaskParams;
import data.Parameters;

/**
 * Created by Jakub on 2016-06-19.
 */
public class ForecastPage extends Fragment
{
    private TextView pressureTextView, humidityTextView, tempMaxTextView, tempMinTextView,
            descriptionTextView, cloudsTextView, rainTextView, snowTextView,
            windSpeedTextView, windAngleTextView, locationTextView, temperatureTextView;
    private static boolean detailed = false;
    private String unit, speed;

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        ViewGroup view = (ViewGroup) inflater.inflate(R.layout.forecast_layout, container, false);
        locationTextView = (TextView) view.findViewById(R.id.locationTextView);
        temperatureTextView = (TextView) view.findViewById(R.id.temperatureTextView);
        pressureTextView = (TextView) view.findViewById(R.id.pressureTextView);
        humidityTextView = (TextView) view.findViewById(R.id.humidityTextView);
        tempMaxTextView = (TextView) view.findViewById(R.id.tempMaxTextView);
        tempMinTextView = (TextView) view.findViewById(R.id.tempMinTextView);
        descriptionTextView = (TextView) view.findViewById(R.id.descriptionTextView);
        cloudsTextView = (TextView) view.findViewById(R.id.cloudsTextView);
        rainTextView = (TextView) view.findViewById(R.id.rainTextView);
        snowTextView = (TextView) view.findViewById(R.id.snowTextView);
        windSpeedTextView = (TextView) view.findViewById(R.id.windSpeedTextView);
        windAngleTextView = (TextView) view.findViewById(R.id.windAngleTextView);

        return view;
    }

    public void updateLabels(Parameters parameters)
    {
        if(!detailed)
        {
            windAngleTextView.setVisibility(View.INVISIBLE);
            rainTextView.setVisibility(View.INVISIBLE);
            snowTextView.setVisibility(View.INVISIBLE);
        }

        else
        {
            windAngleTextView.setVisibility(View.VISIBLE);
            rainTextView.setVisibility(View.VISIBLE);
            snowTextView.setVisibility(View.VISIBLE);
        }

        switch(TaskParams.getUnits())
        {
            case "metric":
                unit = "C";
                speed = "m/s";
                break;

            case "imperial":
                unit = "F";
                speed = "mph";
                break;

            case "kelvin":
                unit = "K";
                speed = "m/s";
                break;
        }

        locationTextView.setText(parameters.getCityName());
        temperatureTextView.setText(parameters.getMain().getTemperature() + "º" + unit);
        pressureTextView.setText("Pressure: " + parameters.getMain().getPressure() + " hPa");
        humidityTextView.setText("Humidity: " + parameters.getMain().getHumidity() + "%");
        tempMaxTextView.setText("Max temp: " + parameters.getMain().getTemp_max() + "º" + unit);
        tempMinTextView.setText("Min temp: " + parameters.getMain().getTemp_min() + "º" + unit);
        descriptionTextView.setText(parameters.getWeather().getDescription());
        cloudsTextView.setText("Cloudiness: " + parameters.getClouds().getCloudiness() + "%");
        rainTextView.setText("Rain in last 3 hours: " + parameters.getRain().getLast3H());
        snowTextView.setText("Snow in last 3 hours: " + parameters.getSnow().getLast3H());

        windSpeedTextView.setText("Wind speed: " + parameters.getWind().getSpeed() + " " + speed);

        windAngleTextView.setText("Wind angle: " + parameters.getWind().getDegrees() + "º");
    }

    public static void setDetailed(boolean detailed)
    {
        ForecastPage.detailed = detailed;
    }
}
