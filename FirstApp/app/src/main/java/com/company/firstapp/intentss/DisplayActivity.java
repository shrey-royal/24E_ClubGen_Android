package com.company.firstapp.intentss;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.company.firstapp.R;

public class DisplayActivity extends AppCompatActivity {

    private TextView nameTextView, emailTextView, phoneTextView, addressTextView;
    private RadioButton radioMaleDisplay, radioFemaleDisplay, radioOtherDisplay;
    private Button backButton;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_display);

        nameTextView = findViewById(R.id.nameTextView);
        emailTextView = findViewById(R.id.emailTextView);
        phoneTextView = findViewById(R.id.phoneTextView);
        addressTextView = findViewById(R.id.addressTextView);
        radioMaleDisplay = findViewById(R.id.radioMaleDisplay);
        radioFemaleDisplay = findViewById(R.id.radioFemaleDisplay);
        radioOtherDisplay = findViewById(R.id.radioOtherDisplay);
        backButton = findViewById(R.id.backButton);

        Intent intent = getIntent();
        String name = intent.getStringExtra("name");
        String email = intent.getStringExtra("email");
        String phone = intent.getStringExtra("phone");
        String address = intent.getStringExtra("address");
        String gender = intent.getStringExtra("gender");

        nameTextView.setText("Name: " + name);
        emailTextView.setText("Email: " + email);
        phoneTextView.setText("Phone: " + phone);
        addressTextView.setText("Address: " + address);

        if (gender.equals("Male")) {
            radioMaleDisplay.setChecked(true);
        } else if (gender.equals("Female")) {
            radioFemaleDisplay.setChecked(true);
        } else if (gender.equals("Other")) {
            radioOtherDisplay.setChecked(true);
        }

        backButton.setOnClickListener(v -> finish());
    }
}
