package com.company.proapp.services;

import android.content.Context;
import android.util.Log;

import androidx.work.Worker;
import androidx.work.WorkerParameters;

public class WeatherWorker extends Worker {

    public WeatherWorker(Context context, WorkerParameters workerParams) {
        super(context, workerParams);
    }

    @Override
    public Result doWork() {
        String weatherData = fetchWeatherData();
        Log.d("WeatherWorker", "Fetched weather data: " + weatherData);

        return Result.success();
    }

    private String fetchWeatherData() {
        return "Current weather: Sunny, 25°C";
    }
}
