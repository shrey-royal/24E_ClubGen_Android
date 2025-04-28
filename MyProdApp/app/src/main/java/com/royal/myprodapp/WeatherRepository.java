package com.royal.myprodapp;

import android.util.Log;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;

import java.io.IOException;

public class WeatherRepository {
    private static final OkHttpClient client = new OkHttpClient();

    public interface WeatherCallback {
        void onResult(String weatherData);
    }

    public static void getWeatherAsync(double lat, double lon, WeatherCallback callback) {
        Log.d("WeatherApp","Getweatherasync called ...");
        String url = "https://api.openweathermap.org/data/2.5/weather?lat=" + lat + "&lon=" + lon + "&appid=f8f21f53baf1d62d425f5e190c577346&units=metric";

        Request request = new Request.Builder().url(url).build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.e("WeatherRepo", "Network request failed", e);
                callback.onResult(null);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful() && response.body() != null) {
                    ResponseBody responseBody = response.body();
                    String result = responseBody.string();
                    System.out.println(result);
                    callback.onResult(result);
                } else {
                    callback.onResult(null);
                }
            }
        });
    }
}
