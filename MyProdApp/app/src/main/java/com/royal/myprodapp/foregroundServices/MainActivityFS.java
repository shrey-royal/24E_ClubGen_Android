package com.royal.myprodapp.foregroundServices;

import android.Manifest;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.royal.myprodapp.R;

public class MainActivityFS extends AppCompatActivity {
    public Button start, stop;

    @RequiresApi(api = Build.VERSION_CODES.TIRAMISU)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ActivityCompat.requestPermissions(
                this,
                new String[]{
                        Manifest.permission.POST_NOTIFICATIONS
                },
                0
        );

        setContentView(R.layout.activity_main_fs);
        start = findViewById(R.id.btn_start_service);
        stop = findViewById(R.id.btn_stop_service);

        start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(MainActivityFS.this, RunningService.class);
                i.setAction(RunningService.Actions.START.toString());
                startService(i);
            }
        });

        stop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(MainActivityFS.this, RunningService.class);
                i.setAction(RunningService.Actions.STOP.toString());
                stopService(i);
            }
        });
    }
}