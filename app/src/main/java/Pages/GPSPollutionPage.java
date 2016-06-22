package Pages;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.jakub.weathernow2.R;

/**
 * Created by Jakub on 22.06.2016.
 */
public class GPSPollutionPage extends Fragment
{
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        ViewGroup view = (ViewGroup) inflater.inflate(R.layout.gps_pollution, container, false);

        return view;
    }
}
