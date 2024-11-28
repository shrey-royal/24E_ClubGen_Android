package com.company.firstapp.widgets;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.company.firstapp.R;

public class CustomToast extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_custom_toast);

        findViewById(R.id.btn_showToast).setOnClickListener(view -> {
            // Toast.LENGTH_SHORT -> 3s
            // Toast.LENGTH_LONG -> 5s
            Toast.makeText(this, "Ayush is learning Android", Toast.LENGTH_SHORT).show();
        });
    }
}