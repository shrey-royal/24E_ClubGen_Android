package com.company.firstapp.intentss;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.company.firstapp.R;

public class IntentActivitry_1 extends AppCompatActivity {

    private EditText editTextName, editTextEmail, editTextPhone, editTextAddress;
    private RadioGroup genderRadioGroup;
    private Button submitButton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_intent_activitry1);

        editTextName = findViewById(R.id.editTextName);
        editTextEmail = findViewById(R.id.editTextEmail);
        editTextPhone = findViewById(R.id.editTextPhone);
        editTextAddress = findViewById(R.id.editTextAddress);
        genderRadioGroup = findViewById(R.id.genderRadioGroup);
        submitButton = findViewById(R.id.submitButton);

        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String name = editTextName.getText().toString();
                String email = editTextEmail.getText().toString();
                String phone = editTextPhone.getText().toString();
                String address = editTextAddress.getText().toString();

                if (name.isEmpty() || email.isEmpty() || phone.isEmpty() || address.isEmpty()) {
                    Toast.makeText(IntentActivitry_1.this, "Please fill all fields", Toast.LENGTH_SHORT).show();
                    return;
                }

                int selectedGenderId = genderRadioGroup.getCheckedRadioButtonId();
                if (selectedGenderId == -1) {
                    Toast.makeText(IntentActivitry_1.this, "Please select a gender", Toast.LENGTH_SHORT).show();
                    return;
                }

                RadioButton selectedGender = findViewById(selectedGenderId);
                String gender = selectedGender.getText().toString();

                Intent intent = new Intent(IntentActivitry_1.this, DisplayActivity.class);
                intent.putExtra("name", name);
                intent.putExtra("email", email);
                intent.putExtra("phone", phone);
                intent.putExtra("address", address);
                intent.putExtra("gender", gender);
                startActivity(intent);
            }
        });
    }
}