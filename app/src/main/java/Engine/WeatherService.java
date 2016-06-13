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
 * Created by Jakub on 13.06.2016.
 */
public class WeatherService extends AsyncTask<TaskParams, Void, String>
{
    private WeatherServiceCallback callback;
    private Exception exception;

    public WeatherService(WeatherServiceCallback callback)
    {
        this.callback = callback;
    }

    @Override
    protected String doInBackground(TaskParams... params)
    {

        String query = "api.openweathermap.org/data/2.5/weather?lat=" +
                params[0].getLat()+ "&lon=" + params[0].getLon() +
                "&appid=10660a09a9fb335d72f576f7aa1bbe5b&units=metric";

        try
        {
            URL url = new URL(query);
            URLConnection connection = url.openConnection();
            InputStream inputStream =connection.getInputStream();
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
            StringBuilder builder = new StringBuilder();
            String line;

            while((line = reader.readLine()) != null)
            {
                builder.append(line);
            }

            return builder.toString();
        }

        catch (MalformedURLException e)
        {
            exception = e;
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
        if(s == null && exception != null)
        {
            callback.serviceFailure(exception);
            return;
        }

            //JSONObject data = new JSONObject(s);
            //JSONObject queryResults = data.optJSONObject("query");
            /*int count = queryResults.optInt("count");

            if (count == 0)
            {
                callback.serviceFailure(new LocationWeatherException("No information available"));
                return;
            }*/

            Parameters parameters = new Parameters();
            //parameters.poopulate(queryResults.optJSONObject("results").optJSONObject("channel"));
            callback.serviceSuccess(parameters);
    }
}

