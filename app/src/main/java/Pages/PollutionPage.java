package Pages;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.jakub.weathernow2.R;

/**
 * Created by Jakub on 2016-06-19.
 */
public class PollutionPage extends Fragment
{
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        ViewGroup view = (ViewGroup) inflater.inflate(R.layout.pollution_layout, container, false);

        return view;
    }
}
