package Pages;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.jakub.weathernow2.R;

import data.Parameters;
import pollutionData.PollutionParameters;

/**
 * Created by Jakub on 2016-06-19.
 */
public class PollutionPage extends Fragment
{
    private TextView density;
    private TextView mass;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        ViewGroup view = (ViewGroup) inflater.inflate(R.layout.pollution_layout, container, false);

        density = (TextView) view.findViewById(R.id.densityValTextView);
        mass = (TextView) view.findViewById(R.id.massTextView);

        return view;
    }

    public void updateLabels(PollutionParameters pollutionParameters, Parameters parameters)
    {
        density.setText(String.valueOf(pollutionParameters.getData().getValue() *
                pollutionParameters.getData().getPressure() * 577.3 /
                parameters.getMain().getTemperature()));

        mass.setText(String.valueOf(pollutionParameters.getData().getValue() * 1.66));
    }
}
