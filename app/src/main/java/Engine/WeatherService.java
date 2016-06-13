package Engine;

import android.os.AsyncTask;

import com.example.jakub.weathernow2.WeatherServiceCallback;

/**
 * Created by Jakub on 13.06.2016.
 */
public class WeatherService extends AsyncTask<TaskParams, Void, Void>
{
    private WeatherServiceCallback callback;
    private Exception exception;

    public WeatherService(WeatherServiceCallback callback)
    {
        this.callback = callback;
    }

    @Override
    protected Void doInBackground(TaskParams... params)
    {


        return null;
    }
}
