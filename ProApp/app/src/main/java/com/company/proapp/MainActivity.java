package com.company.proapp;

import android.content.Intent;
import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.company.proapp.services.CameraService;
import com.company.proapp.services.MusicPlayerService;


public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

//        Intent i = new Intent(MainActivity.this, CameraService.class);
//        startActivity(i);

        Intent serviceIntent = new Intent(this, MusicPlayerService.class);
        startService(serviceIntent);
    }

}