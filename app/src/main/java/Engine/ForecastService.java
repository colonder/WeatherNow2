package Engine;

import android.os.AsyncTask;

import com.example.jakub.weathernow2.WeatherServiceCallback;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

import data.Parameters;

/**
 * Created by Jakub on 30.06.2016.
 */
public class ForecastService extends AsyncTask<TaskParams, Void, String> {
    private WeatherServiceCallback callback;
    private Exception exception;
    private String mode;
    private URL url;

    public ForecastService(WeatherServiceCallback callback, String mode)
    {
        this.callback = callback;
        this.mode = mode;
    }

    @Override
    protected String doInBackground(TaskParams... params)
    {
        switch(mode)
        {
            case "GPS":
                try
                {
                    url = new URL("http://api.openweathermap.org/data/2.5/forecast?lat=" +
                            params[0].getLatFine() + "&lon=" + params[0].getLonFine() +
                            "&units=" + TaskParams.getUnits() +
                            "&type=" + TaskParams.getAccuracy() + "&lang=" + TaskParams.getLanguage() +
                            "&appid=10660a09a9fb335d72f576f7aa1bbe5b");
                }

                catch (MalformedURLException e)
                {
                    exception = e;
                }
                break;

            case "CITY":
                try
                {
                    url = new URL("http://api.openweathermap.org/data/2.5/forecast?id=" +
                            params[0].getCityID() + "&units=" + TaskParams.getUnits() +
                            "&type=" + TaskParams.getAccuracy() + "&lang=" +
                            TaskParams.getLanguage() + "&appid=10660a09a9fb335d72f576f7aa1bbe5b");
                }

                catch (MalformedURLException e)
                {
                    exception = e;
                }
                break;
        }

        try
        {
            URLConnection connection = url.openConnection();
            InputStream inputStream = connection.getInputStream();
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
            StringBuilder builder = new StringBuilder();
            String line;

            while ((line = reader.readLine()) != null)
            {
                builder.append(line);
            }

            return builder.toString();
        }

        catch (IOException e)
        {
            exception = e;
        }

        return null;
    }

    @Override
    protected void onPostExecute(String s)
    {
        if (s == null && exception != null)
        {
            callback.serviceFailure(exception);
            return;
        }

        try
        {
            JSONObject data = new JSONObject(s);
            Parameters parameters = new Parameters();
            parameters.poopulate(data);
            callback.serviceSuccess(parameters);
        }

        catch (JSONException e)
        {
            callback.serviceFailure(e);
        }

        catch (NullPointerException e1)
        {
            callback.serviceFailure(e1);
        }
    }
}
