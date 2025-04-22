package com.royal.myprodapp;

import android.annotation.SuppressLint;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;
import androidx.work.Data;
import androidx.work.ListenableWorker;
import androidx.work.Worker;
import androidx.work.WorkerParameters;
import androidx.work.impl.utils.futures.SettableFuture;

import com.google.common.util.concurrent.ListenableFuture;

public class WeatherWorker extends ListenableWorker {
    @SuppressLint("RestrictedApi")
    private SettableFuture<Result> future;
    @SuppressLint("RestrictedApi")
    public WeatherWorker(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
        future = SettableFuture.create();
    }

    @SuppressLint("RestrictedApi")
    @Override
    public ListenableFuture<Result> startWork() {
        Log.d("WeatherApp","Startwork of worker called ...");
        LocationUtils.getCurrentLocation(getApplicationContext(), location -> {
            if (location != null) {
                WeatherRepository.getWeatherAsync(location.getLatitude(), location.getLongitude(), new WeatherRepository.WeatherCallback() {
                    @Override
                    public void onResult(String weatherData) {
                        if (weatherData != null) {
                            Data data = new Data.Builder()
                                    .putString("result_key", weatherData)
                                    .build();
                            showNotification("Weather_data",weatherData);
                            future.set(Result.success(data));
                        } else {
                            future.set(Result.failure());
                        }
                    }
                });
            } else {
                future.set(Result.failure());
            }
        });

        return future;
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

        manager.notify(1, builder.build());
    }
}