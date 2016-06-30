package Pages;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.jakub.weathernow2.PollutionServiceCallback;
import com.example.jakub.weathernow2.R;
import com.example.jakub.weathernow2.WeatherServiceCallback;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Timer;
import java.util.TimerTask;

import Engine.PollutionService;
import Engine.TaskParams;
import Engine.WeatherService;
import data.Parameters;
import pollutionData.PollutionParameters;

/**
 * Created by Jakub on 22.06.2016.
 */
public class CityPage extends Fragment implements WeatherServiceCallback, PollutionServiceCallback
{
    private ArrayAdapter<String> cityAdapter;
    private Handler handler;
    private Timer timer;
    private TaskParams taskParams;
    private WeatherService weatherService;
    private JSONArray jsonArray;
    private TextView cityTempTextView, cityDescTextView;
    private String unit;
    private String speed;
    private ArrayAdapter<CharSequence> countryAdapter;
    private AutoCompleteTextView searchCity, searchCountry;
    private TextView pressureTextView, humidityTextView, tempMaxTextView, tempMinTextView,
            cloudsTextView, rainTextView, snowTextView, windSpeedTextView, windAngleTextView;
    private static boolean detailed;
    private PollutionService pollutionService;
    private Parameters parameters;

    public static void setDetailed(boolean detailed)
    {
        CityPage.detailed = detailed;
    }

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        handler = new Handler();
        timer = new Timer();
        taskParams = new TaskParams(getActivity());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        ViewGroup view = (ViewGroup) inflater.inflate(R.layout.city_weather_page, container, false);

        cityTempTextView = (TextView) view.findViewById(R.id.cityTempTextView);
        cityDescTextView = (TextView) view.findViewById(R.id.cityDescTextView);

        pressureTextView = (TextView) view.findViewById(R.id.cityPressureTextView);
        humidityTextView = (TextView) view.findViewById(R.id.cityHumidityTextView);
        tempMaxTextView = (TextView) view.findViewById(R.id.cityTempMaxTextView);
        tempMinTextView = (TextView) view.findViewById(R.id.cityTempMinTextView);
        cloudsTextView = (TextView) view.findViewById(R.id.cityCloudsTextView);
        rainTextView = (TextView) view.findViewById(R.id.cityRainTextView);
        snowTextView = (TextView) view.findViewById(R.id.citySnowTextView);
        windSpeedTextView = (TextView) view.findViewById(R.id.cityWindSpeedTextView);
        windAngleTextView = (TextView) view.findViewById(R.id.cityWindAngleTextView);

        searchCountry = (AutoCompleteTextView) view.findViewById(R.id.searchCountryTextView);
        searchCity = (AutoCompleteTextView) view.findViewById(R.id.searchCityTextView);

        countryAdapter = ArrayAdapter.createFromResource(getContext(), R.array.country_labels,
                android.R.layout.simple_dropdown_item_1line);

        searchCountry.setAdapter(countryAdapter);

        cityAdapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_dropdown_item_1line);

        searchCity.setAdapter(cityAdapter);

        searchCountry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                searchCountry.showDropDown();
            }
        });

        searchCountry.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id)
            {
                new SpinnerTask((String) parent.getItemAtPosition(position)).execute();
            }
        });

        searchCity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                searchCity.showDropDown();
            }
        });

        searchCity.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id)
            {
                searchCity.showDropDown();
                try
                {
                    taskParams.setCityID(jsonArray.getJSONObject(cityAdapter.getPosition((String)
                            parent.getItemAtPosition(position))).optInt("_id"));
                    callTask(CityPage.this, CityPage.this);
                }

                catch (JSONException e)
                {
                    e.printStackTrace();
                }
            }
        });

        return view;
    }

    @Override
    public void serviceSuccess(Parameters parameters)
    {
        taskParams.setLon(parameters.getCoordination().getLongitude());
        taskParams.setLat(parameters.getCoordination().getLatitude());
        this.parameters = parameters;

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
        
        cityTempTextView.setText(parameters.getMain().getTemperature() + " º" + unit);
        cityDescTextView.setText(parameters.getWeather().getDescription());

        pressureTextView.setText("Pressure: " + parameters.getMain().getPressure() + " hPa");
        humidityTextView.setText("Humidity: " + parameters.getMain().getHumidity() + "%");
        tempMaxTextView.setText("Max temp: " + parameters.getMain().getTemp_max() + " º" + unit);
        tempMinTextView.setText("Min temp: " + parameters.getMain().getTemp_min() + " º" + unit);
        cloudsTextView.setText("Cloudiness: " + parameters.getClouds().getCloudiness() + "%");
        rainTextView.setText("Rain in last 3 hours: " + parameters.getRain().getLast3H());
        snowTextView.setText("Snow in last 3 hours: " + parameters.getSnow().getLast3H());
        windSpeedTextView.setText("Wind speed: " + parameters.getWind().getSpeed() + " " + speed);
        windAngleTextView.setText("Wind angle: " + parameters.getWind().getDegrees() + "º");
    }

    @Override
    public void serviceFailure(Exception exception)
    {
        Toast.makeText(getActivity(), exception.getMessage(), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void inform(String message)
    {
        Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
    }

    public void callTask(final WeatherServiceCallback weatherCallback,
                         final PollutionServiceCallback pollutionCallback)
    {
        TimerTask doAsyncTask = new TimerTask()
        {
            @Override
            public void run()
            {
                handler.post(new Runnable()
                {
                    @Override
                    public void run()
                    {
                        try
                        {
                            weatherService = new WeatherService(weatherCallback, "CITY");
                            pollutionService = new PollutionService(pollutionCallback);
                            weatherService.execute(taskParams);
                            pollutionService.execute(taskParams);
                        }

                        catch (Exception e)
                        {
                            e.printStackTrace();
                        }
                    }
                });
            }
        };

        timer.schedule(doAsyncTask, 0, 5000);
    }

    @Override
    public void pollutionServiceSuccess(PollutionParameters pollutionParameters)
    {
        CityPollutionPage.updateLabels(pollutionParameters, parameters);
    }

    @Override
    public void pollutionServiceFailure(Exception exception)
    {
        Toast.makeText(getActivity(), exception.getMessage(), Toast.LENGTH_SHORT).show();
    }

    private class SpinnerTask extends AsyncTask<Void, Void, Void>
    {
        String label;
        ProgressDialog dialog;
        int progress = 0;
        String line;
        BufferedReader jsonReader;

        public SpinnerTask(String selectedItem)
        {
            for(int i = 0; i < getResources().getStringArray(R.array.country_labels).length; i++)
            {
                if(getResources().getStringArray(R.array.country_labels)[i].equals(selectedItem))
                {
                    label = getResources().getStringArray(R.array.country_val)[i];
                }
            }

            jsonReader = new BufferedReader(new InputStreamReader(getResources().
                openRawResource(R.raw.city_list)));
        }

        @Override
        protected void onPreExecute()
        {
            dialog = new ProgressDialog(getActivity());
            dialog.setTitle(R.string.city_dialog_title);
            dialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
            dialog.setMax(209579);
            dialog.setProgressNumberFormat(null);
            dialog.setProgress(0);
            dialog.show();
        }

        @Override
        protected Void doInBackground(Void... params)
        {
            jsonArray = new JSONArray();

            try
            {

                while((line = jsonReader.readLine()) != null)
                {
                    if (line.contains(label))
                    {
                        jsonArray.put(new JSONObject(line));
                        dialog.setMessage("Available: " + jsonArray.length());
                    }

                    progress++;
                    dialog.setProgress(progress);
                }

            }

            catch (IOException e)
            {
                e.printStackTrace();
            }

            catch (JSONException e)
            {
                e.printStackTrace();
            }

            try
            {
                jsonReader.close();
            }

            catch (IOException e)
            {
                e.printStackTrace();
            }
            
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid)
        {
            if (cityAdapter.getCount() != 0)
                cityAdapter.clear();

            for (int i = 0; i < jsonArray.length(); i++) {

                try
                {
                    cityAdapter.add(jsonArray.getJSONObject(i).optString("name"));
                }

                catch (JSONException e)
                {
                    e.printStackTrace();
                }
            }

            cityAdapter.notifyDataSetChanged();

            dialog.hide();
        }
    }
}
