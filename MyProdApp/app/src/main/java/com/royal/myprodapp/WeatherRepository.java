package com.royal.myprodapp;

import android.util.Log;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class WeatherRepository {
    private static final OkHttpClient client = new OkHttpClient();

    public static String getWeather(double lat, double lon) {
        String url = "https://api.openweathermap.org/data/2.5/weather?lat="+lat+"&lon="+lon+"&appid=___";

        Request request = new Request.Builder().url(url).build();
        try (Response response = client.newCall(request).execute()) {
            if (response.isSuccessful() && response.body() != null) {
                return response.body().string();
            }
        } catch (Exception e) {
            Log.e("WeatherRepo", "Error fetching weather", e);
        }

        return null;
    }
}
