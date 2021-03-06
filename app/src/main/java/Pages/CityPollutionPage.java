package Pages;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.jakub.weathernow2.R;

import java.math.BigDecimal;

import data.Parameters;
import pollutionData.PollutionParameters;

/**
 * Created by Jakub on 22.06.2016.
 */
public class CityPollutionPage extends Fragment
{
    private static TextView density;
    private static TextView mass;
    private static BigDecimal val;
    private static String tmp;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        ViewGroup view = (ViewGroup) inflater.inflate(R.layout.city_pollution_page, container, false);

        density = (TextView) view.findViewById(R.id.cityDensityValTextView);
        mass = (TextView) view.findViewById(R.id.cityMassTextView);

        return view;
    }

    public static void updateLabels(PollutionParameters pollutionParameters, Parameters parameters)
    {
        val = new BigDecimal(pollutionParameters.getData().getValue() *
                pollutionParameters.getData().getPressure() * 577.3 /
                parameters.getMain().getTemperature()).setScale(5, BigDecimal.ROUND_UP);

        density.setText(String.valueOf(val));
        tmp = String.valueOf(pollutionParameters.getData().getValue() * 1.66);
        mass.setText(tmp.substring(0, 4) + tmp.substring(tmp.length() - 3));
    }
}
