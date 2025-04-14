package com.royal.myprodapp;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.work.OneTimeWorkRequest;
import androidx.work.WorkManager;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        OneTimeWorkRequest oneTimeWorkRequest = new OneTimeWorkRequest.Builder(LogUploadWorker.class).build();

        WorkManager.getInstance(this).enqueue(oneTimeWorkRequest);
    }
}