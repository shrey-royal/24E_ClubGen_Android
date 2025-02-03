package com.company.proapp;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.work.PeriodicWorkRequest;
import androidx.work.WorkManager;

import com.company.proapp.services.WeatherWorker;

import java.util.concurrent.TimeUnit;

public class MainActivity extends AppCompatActivity {
    private static final int CAMERA_PERMISSION_REQUEST_CODE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

//        startMusicPlayerService();

        scheduleWeatherMonitoring();

/*
        Context context = getApplicationContext();
        Intent i = new Intent(this, CameraService.class);
        context.startForegroundService(i);

        // Check if permission is already granted
        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.CAMERA)
                == PackageManager.PERMISSION_GRANTED) {
            startCameraService();
        } else {
            // Request permission
            ActivityCompat.requestPermissions(this,
                    new String[]{android.Manifest.permission.CAMERA},
                    CAMERA_PERMISSION_REQUEST_CODE);
        }
    }
    // Handle the permission result
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == CAMERA_PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                startCameraService();
            } else {
                Toast.makeText(this, "Camera permission is required", Toast.LENGTH_SHORT).show();
            }
        }
*/
    }

//    private void startCameraService() {
//        Intent serviceIntent = new Intent(this, CameraService.class);
//        ContextCompat.startForegroundService(this, serviceIntent);
//    }

//    private void startMusicPlayerService() {
//        Intent serviceIntent = new Intent(this, MusicPlayerService.class);
//        ContextCompat.startForegroundService(this, serviceIntent);
//    }

    private void scheduleWeatherMonitoring() {
        PeriodicWorkRequest weatherRequest = new PeriodicWorkRequest.Builder(WeatherWorker.class, 1, TimeUnit.MINUTES)
                .build();

        WorkManager.getInstance(this).enqueue(weatherRequest);
    }

}