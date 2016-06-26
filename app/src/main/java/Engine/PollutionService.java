package Engine;

import android.os.AsyncTask;

import com.example.jakub.weathernow2.PollutionServiceCallback;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

import pollutionData.PollutionParameters;

/**
 * Created by Jakub on 2016-06-25.
 */
public class PollutionService extends AsyncTask<TaskParams, Void, String>
{

    private PollutionServiceCallback callback;
    private Exception exception;

    public PollutionService(PollutionServiceCallback callback) {
        this.callback = callback;
    }

    @Override
    protected String doInBackground(TaskParams... params) {
        try
        {
            URL url = new URL("http://api.openweathermap.org/pollution/v1/co/" + params[0].getLat(false) +
                    "," + params[0].getLon(false) + "/current.json?&appid=10660a09a9fb335d72f576f7aa1bbe5b");

            URLConnection connection = url.openConnection();
            InputStream inputStream = connection.getInputStream();
            InputStreamReader streamReader = new InputStreamReader(inputStream);
            BufferedReader reader = new BufferedReader(streamReader);

            StringBuilder builder = new StringBuilder();
            String line;

            while ((line = reader.readLine()) != null)
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
        if (s == null && exception != null)
        {
            callback.pollutionServiceFailure(exception);
            return;
        }

        try
        {
            JSONObject data = new JSONObject(s);
            PollutionParameters parameters = new PollutionParameters();
            parameters.poopulate(data);
            callback.pollutionServiceSuccess(parameters);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
