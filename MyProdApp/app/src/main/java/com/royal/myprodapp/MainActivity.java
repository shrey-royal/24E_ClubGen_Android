package com.royal.myprodapp;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.work.OneTimeWorkRequest;
import androidx.work.WorkInfo;
import androidx.work.WorkManager;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

@RequiresApi(api = Build.VERSION_CODES.TIRAMISU)
public class MainActivity extends AppCompatActivity {
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 100;
    private TextView locationTextView, weatherDataTextView;
    private ImageView weatherImageView;

    private String[] permissions = new String[]{
            Manifest.permission.INTERNET,
            Manifest.permission.POST_NOTIFICATIONS,
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.ACCESS_BACKGROUND_LOCATION,
    };

    private void requestLocationPermission() {
        List<String> listPermissionsNeeded = new ArrayList<>();

        for (String permission : permissions) {
            if (ContextCompat.checkSelfPermission(this, permission) != PackageManager.PERMISSION_GRANTED) {
                listPermissionsNeeded.add(permission);
            }
        }

        if (!listPermissionsNeeded.isEmpty()) {
            ActivityCompat.requestPermissions(this,
                    listPermissionsNeeded.toArray(new String[0]),
                    LOCATION_PERMISSION_REQUEST_CODE);
        }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        locationTextView = findViewById(R.id.locationTextView);
        weatherImageView = findViewById(R.id.weatherImageView);
        weatherDataTextView = findViewById(R.id.weatherDataTextView);

        requestLocationPermission();
        startWorkerAndObserve();
    }

    private void startWorkerAndObserve() {
        OneTimeWorkRequest workRequest = new OneTimeWorkRequest.Builder(WeatherWorker.class).build();

        WorkManager.getInstance(this).enqueue(workRequest);

        WorkManager.getInstance(this).getWorkInfoByIdLiveData(workRequest.getId())
                .observe(this, workInfo -> {
                    if (workInfo != null && workInfo.getState() == WorkInfo.State.SUCCEEDED) {
                        String result = workInfo.getOutputData().getString("result_key");
                        try {
                            if (result == null) {
                                locationTextView.setText("Failed to fetch data");
                                weatherDataTextView.setText("Failed to fetch city");
                            } else {
                                JSONObject jsonObject = new JSONObject(result);
                                weatherDataTextView.setText(jsonObject.getString("name"));
                                locationTextView.setText(jsonObject.getJSONObject("main").getString("temp") + " °F");
                            }

                        } catch (JSONException e) {
                            throw new RuntimeException(e);
                        }
                        setWeatherImage(result);
                    } else if (workInfo != null && workInfo.getState() == WorkInfo.State.FAILED) {
                        locationTextView.setText("Worker failed 😓");
                    }
                });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        Log.d("WeatherApp","onRequestPermissionsResult called ...");

        if (requestCode == LOCATION_PERMISSION_REQUEST_CODE) {
            boolean allGranted = true;
            for (int result : grantResults) {
                if (result != PackageManager.PERMISSION_GRANTED) {
                    allGranted = false;
                    break;
                }
            }

            if (!allGranted) {
                locationTextView.setText("Permission denied 🙁");
            }
        }
    }

    private void setWeatherImage(String weatherData) {
        if (weatherData.contains("Cloud")) {
            weatherImageView.setImageResource(R.drawable.cloud);
        } else if (weatherData.contains("Rain")) {
            weatherImageView.setImageResource(R.drawable.rain);
        } else if (weatherData.contains("Smoke")) {
            weatherImageView.setImageResource(R.drawable.smoke);
        } else {
            weatherImageView.setImageResource(R.drawable.sun);
        }
    }

}