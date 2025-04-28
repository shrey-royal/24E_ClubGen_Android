package com.royal.myprodapp;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.location.Location;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;
import androidx.work.Data;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.concurrent.CountDownLatch;

import cz.msebera.android.httpclient.Header;

public class WeatherWorker extends Worker {

    final Result[] resultHolder = new Result[1];
    final CountDownLatch latch = new CountDownLatch(1);

    public WeatherWorker(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
    }

    @NonNull
    @Override
    public Result doWork() {
        Log.d("WeatherApp","Startwork of worker called ...");
        Log.d("WeatherApp", "Running on worker on thread id : " + Thread.currentThread().getId());

//        LocationUtils.getCurrentLocation(getApplicationContext(), new LocationUtils.LocationCallbackInterface() {
//            @Override
//            public void onLocationReceived(Location location) throws IOException {
//                if(location != null){
//                    String weatherData = fetchWeatherData(location.getLatitude(),location.getLongitude());
//                    if (weatherData != null) {
//                        showNotification("Weather_data", weatherData);
//
//                        Data outputData = new Data.Builder()
//                                .putString("result_key", weatherData)
//                                .build();
//
//                        resultHolder[0] = Result.success(outputData);
//                    } else {
//                        resultHolder[0] = Result.failure();
//                    }
//                    latch.countDown();
//                }else{
//                    resultHolder[0] = Result.failure();
//                    latch.countDown();
//                }
//            }
//        });

//        LocationUtils.getCurrentLocation(getApplicationContext(), new LocationUtils.LocationCallbackInterface() {
//            @Override
//            public void onLocationReceived(Location location) throws IOException {
//                if (location != null) {
//                    WeatherRepository.getWeatherAsync(location.getLatitude(), location.getLongitude(), weatherData -> {
//                        if (weatherData != null) {
//                            // Show notification
//                            showNotification("Weather_data", weatherData);
//
//                            // Store success result with output data
//                            Data outputData = new Data.Builder()
//                                    .putString("result_key", weatherData)
//                                    .build();
//
//                            resultHolder[0] = Result.success(outputData);
//                        } else {
//                            resultHolder[0] = Result.failure();
//                        }
//                        latch.countDown();
//                    });
//                } else {
//                    resultHolder[0] = Result.failure();
//                    latch.countDown();
//                }
//            }
//        });

        LocationUtils.getCurrentLocation(getApplicationContext(), new LocationUtils.LocationCallbackInterface() {
            @Override
            public void onLocationReceived(Location location) throws IOException {
                if(location != null){
                    fetchweatherDataAsync(location.getLatitude(),location.getLongitude());
                }else{
                    resultHolder[0] = Result.failure();
                    latch.countDown();
                }
            }
        });

        try {
            // Wait for async operations to complete (timeout for safety)
            boolean completed = latch.await(10, java.util.concurrent.TimeUnit.SECONDS);
            if (!completed) {
                Log.e("WeatherApp", "Timeout waiting for location/weather data");
                return Result.failure();
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
            return Result.failure();
        }

        Log.d("WeatherApp", "DoWork ends on thread id : " + Thread.currentThread().getId());
        return resultHolder[0] != null ? resultHolder[0] : Result.failure();
    }

    private void fetchweatherDataAsync(double lat, double lon) throws MalformedURLException {
        AsyncHttpClient client = new AsyncHttpClient();
        String url = "https://api.openweathermap.org/data/2.5/weather?lat=" + lat + "&lon=" + lon + "&appid=f8f21f53baf1d62d425f5e190c577346";
        Log.d("WeatherApp", "fetchweatherDataAsync on thread id : " + Thread.currentThread().getId());
        client.get(url, new AsyncHttpResponseHandler() {

            @Override
            public void onStart() {
                // Called before a request is started
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] response) {
                // Called when response HTTP status is "200 OK"
                String weatherData = new String(response);
                if (weatherData != null) {
                    showNotification("Weather_data", weatherData);

                    Data outputData = new Data.Builder()
                            .putString("result_key", weatherData)
                            .build();

                    resultHolder[0] = Result.success(outputData);
                } else {
                    resultHolder[0] = Result.failure();
                }
                latch.countDown();
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] errorResponse, Throwable e) {
                // Called when response HTTP status is "4XX" (for example, 401, 403, 404)
            }

            @Override
            public void onRetry(int retryNo) {
                // Called when request is retried
            }
        });
    }

    private String fetchWeatherData(double lat, double lon) throws IOException {
        String strurl = "https://api.openweathermap.org/data/2.5/weather?lat=" + lat + "&lon=" + lon + "&appid=f8f21f53baf1d62d425f5e190c577346";
        URL url = new URL(strurl);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");
        connection.setConnectTimeout(10000); // 10 seconds timeout
        connection.setReadTimeout(10000);

        int responseCode = connection.getResponseCode();
        Log.d("WeatherApp", "fetchWeatherData called on thread id : " + Thread.currentThread().getId());
        if (responseCode == HttpURLConnection.HTTP_OK) {
            InputStream inputStream = connection.getInputStream();
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
            StringBuilder result = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                result.append(line);
            }
            reader.close();
            connection.disconnect();
            return result.toString();
        } else {
            connection.disconnect();
            return null;
        }
    }


    private void showNotification(String title, String content) {
        String channelId = "weather_channel";
        NotificationManager manager = (NotificationManager) getApplicationContext().getSystemService(Context.NOTIFICATION_SERVICE);

        NotificationChannel channel = new NotificationChannel(channelId, "Weather Updates", NotificationManager.IMPORTANCE_DEFAULT);
        manager.createNotificationChannel(channel);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(getApplicationContext(), channelId)
                .setContentTitle(title)
                .setContentText(content.length() > 60 ? content.substring(0, 60) + "..." : content)
                .setSmallIcon(android.R.drawable.ic_menu_compass)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT);

        // Generate a unique ID using current timestamp
        int notificationId = (int) System.currentTimeMillis(); // <- This ensures a different ID each time

        manager.notify(notificationId, builder.build());
    }
}
