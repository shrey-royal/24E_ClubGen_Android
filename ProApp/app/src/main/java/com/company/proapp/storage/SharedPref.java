package com.company.proapp.storage;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.company.proapp.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class SharedPref extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        EditText nameInput, emailInput;
        Button saveButton, viewButton, removeButton;

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shared_pref);

        nameInput = findViewById(R.id.editTextName);
        emailInput = findViewById(R.id.editTextEmail);
        saveButton = findViewById(R.id.buttonSave);
        viewButton = findViewById(R.id.buttonView);
        removeButton = findViewById(R.id.removeButton);

        SharedPreferences sharedPreferences = getSharedPreferences("UserData", Context.MODE_PRIVATE);

        saveButton .setOnClickListener(v -> {
            String name = nameInput.getText().toString();
            String email = emailInput.getText().toString();

            if (!name.isEmpty() && !email.isEmpty()) {
                try {
                    String existingData = sharedPreferences.getString("user_list", "[]");
                    JSONArray userArray = new JSONArray(existingData);

//                    JSONObject userObject = new JSONObject();
//                    userObject.put("name", name);
//                    userObject.put("email", email);
//
//                    userArray.put(userObject);

                    userArray.put(new JSONObject().put("name", name).put("email", email));

                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putString("user_list", userArray.toString());
                    editor.apply();

                    Toast.makeText(this, "Data Saved!", Toast.LENGTH_SHORT).show();

                    nameInput.setText("");
                    emailInput.setText("");
                } catch (JSONException e) {
                    Log.e("Shared_Pref", "Error saving data into shared preferences!");
                    Toast.makeText(this, "Error saving data", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(this, "Please enter both fields", Toast.LENGTH_SHORT).show();
            }
        });

        viewButton.setOnClickListener(v -> {
            Intent intent = new Intent(this, ViewSharedPref.class);
            startActivity(intent);
        });

        removeButton.setOnClickListener(v -> {
            String name = nameInput.getText().toString();
            String email = emailInput.getText().toString();

            if (!name.isEmpty() && !email.isEmpty()) {
                try {
                    String existingData = sharedPreferences.getString("user_list", "[]");
                    JSONArray userArray = new JSONArray(existingData);

                    boolean isDataRemoved = false;

                    JSONArray updatedArray = new JSONArray();
                    for (int i = 0; i < userArray.length(); i++) {
                        JSONObject userObject = userArray.getJSONObject(i);
                        String savedName = userObject.getString("name");
                        String savedEmail = userObject.getString("email");

                        if (savedName.equals(name) && savedEmail.equals(email)) {
                            isDataRemoved = true;
                            continue;
                        }
                        updatedArray.put(userObject);
                    }

                    if (isDataRemoved) {
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.putString("user_list", updatedArray.toString());
                        editor.apply();
                        Toast.makeText(this, "Data Removed!", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(this, "Data not found!", Toast.LENGTH_SHORT).show();
                    }
                    nameInput.setText("");
                    emailInput.setText("");
                } catch (JSONException e) {
                    Log.e("Shared_Pref", "Error removing data from shared preferences!");
                    Toast.makeText(this, "Error processing data", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(this, "Please enter both fields", Toast.LENGTH_SHORT).show();
            }
        });
    }
}