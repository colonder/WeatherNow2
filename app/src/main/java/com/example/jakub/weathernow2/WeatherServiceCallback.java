package com.example.jakub.weathernow2;

import data.Parameters;

/**
 * Created by Jakub on 13.06.2016.
 */
public interface WeatherServiceCallback {
    void serviceSuccess(Parameters parameters);
    void serviceFailure(Exception exception);
    void inform(String message);
}
