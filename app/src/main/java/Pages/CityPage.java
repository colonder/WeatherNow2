package Pages;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
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
    private Spinner citySpinner;
    private Spinner countrySpinner;
    private ArrayAdapter<String> cityAdapter;
    private boolean initCountry = true;
    private boolean initCity = true;
    private Handler handler;
    private Timer timer;
    private TaskParams taskParams;
    private WeatherService weatherService;
    private JSONArray jsonArray;

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        citiesList = new ArrayList<>();
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
                R.array.country_labels, R.layout.spinner_item);
        countryAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        countrySpinner.setAdapter(countryAdapter);

        cityAdapter = new ArrayAdapter<String>(getContext(), R.layout.spinner_item, citiesList);
        cityAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        citySpinner.setAdapter(cityAdapter);

        countrySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id)
            {
                if (!initCountry)
                {
                    new SpinnerTask().execute();
                }

                else
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

    private class SpinnerTask extends AsyncTask<Void, Void, Void>
    {
        String label;
        ProgressDialog dialog;
        int progress = 0;
        String line;
        BufferedReader jsonReader = new BufferedReader(new InputStreamReader(getResources().
        openRawResource(R.raw.city_list)));

        @Override
        protected void onPreExecute()
        {
            label = countrySpinner.getSelectedItem().toString();
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

            try
            {
                jsonReader.reset();
            }

            catch (IOException e)
            {
                e.printStackTrace();
            }

            dialog.hide();
        }
    }
}
