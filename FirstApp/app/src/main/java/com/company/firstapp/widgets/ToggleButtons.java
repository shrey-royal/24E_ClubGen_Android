package com.company.firstapp.widgets;

import android.graphics.Color;
import android.os.Bundle;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.company.firstapp.R;
import com.google.android.material.switchmaterial.SwitchMaterial;

public class ToggleButtons extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_toggle_buttons);

        RelativeLayout layout = (RelativeLayout) findViewById(R.id.tglBtnLyt);
        TextView label = (TextView) findViewById(R.id.tv_tglLabel);
        SwitchMaterial tgl_btn = (SwitchMaterial) findViewById(R.id.tgl_btn);

        layout.setBackgroundColor(Color.WHITE);

        tgl_btn.setOnCheckedChangeListener((btn_view, isChecked) -> {
            if(isChecked) { //dark mode
                layout.setBackgroundColor(Color.BLACK);
                label.setTextColor(Color.WHITE);
            } else {    //light mode
                layout.setBackgroundColor(Color.WHITE);
                label.setTextColor(Color.BLACK);
            }
        });
    }
}