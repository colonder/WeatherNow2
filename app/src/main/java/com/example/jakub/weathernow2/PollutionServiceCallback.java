package com.example.jakub.weathernow2;

import pollutionData.PollutionParameters;

/**
 * Created by Jakub on 2016-06-25.
 */
public interface PollutionServiceCallback
{
    void pollutionServiceSuccess(PollutionParameters pollutionParameters);
    void pollutionServiceFailure(Exception exception);
}
