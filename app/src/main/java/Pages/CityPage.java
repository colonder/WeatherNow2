package Pages;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.jakub.weathernow2.R;
import com.example.jakub.weathernow2.WeatherServiceCallback;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import Engine.TaskParams;
import Engine.WeatherService;
import data.Parameters;

/**
 * Created by Jakub on 22.06.2016.
 */
public class CityPage extends Fragment implements WeatherServiceCallback
{
    private ArrayList<String> citiesList;
    private ArrayList<String> countryList;
    private Spinner citySpinner;
    private Spinner countrySpinner;
    private JSONArray jsonArray;
    private BufferedReader jsonReader;
    private ArrayAdapter<String> cityAdapter;
    private boolean initCountry = true;
    private boolean initCity = true;
    private Handler handler;
    private Timer timer;
    private TaskParams taskParams;
    private WeatherService weatherService;

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        citiesList = new ArrayList<>();
        countryList = new ArrayList<>();
        jsonReader = new BufferedReader(new InputStreamReader(this.getResources().
                openRawResource(R.raw.city_list)));

        handler = new Handler();
        timer = new Timer();
        taskParams = new TaskParams(getActivity());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        ViewGroup view = (ViewGroup) inflater.inflate(R.layout.city_forecast_page, container, false);

        citySpinner = (Spinner) view.findViewById(R.id.city_spinner);
        countrySpinner = (Spinner) view.findViewById(R.id.country_spinner);

        ArrayAdapter<CharSequence> countryAdapter = ArrayAdapter.createFromResource(getContext(),
                R.array.country_labels, android.R.layout.simple_spinner_item);
        countryAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        countrySpinner.setAdapter(countryAdapter);

        cityAdapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_item, citiesList);
        cityAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        citySpinner.setAdapter(cityAdapter);

        countrySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (!initCountry) 
                {
                    String label = countrySpinner.getSelectedItem().toString();

                    ProgressDialog dialog = new ProgressDialog(getActivity());
                    dialog.setMessage("Refreshing list of available cities");
                    dialog.show();

                    jsonArray = new JSONArray();

                    try {

                        for (String line; (line = jsonReader.readLine()) != null; )
                        {
                            if (line.contains(label))
                            {
                                jsonArray.put(new JSONObject(line));
                            }
                        }


                    } catch (IOException e) {
                        e.printStackTrace();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    if (citiesList.size() != 0)
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
                } else
                    initCountry = false;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent)
            {

            }
        });

        citySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
        {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id)
            {
                if(!initCity)
                {
                    try
                    {
                        taskParams.setCityID(jsonArray.getJSONObject(citySpinner.
                                getSelectedItemPosition()).optInt("_id"));
                        callTask(CityPage.this);
                    }

                    catch (JSONException e)
                    {
                        e.printStackTrace();
                    }
                }
                
                else
                    initCity = false;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent)
            {

            }
        });

        return view;
    }


    @Override
    public void serviceSuccess(Parameters parameters)
    {
        Toast.makeText(getActivity(), "Service success", Toast.LENGTH_SHORT).show();
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

    public void callTask(final WeatherServiceCallback weatherCallback)
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
                            weatherService.execute(taskParams);
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
}
