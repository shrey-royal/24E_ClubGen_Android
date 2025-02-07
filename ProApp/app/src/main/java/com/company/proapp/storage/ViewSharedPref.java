package com.company.proapp.storage;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.company.proapp.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class ViewSharedPref extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_shared_pref);

        TextView resultTextView = findViewById(R.id.textViewResult);

        SharedPreferences sharedPreferences = getSharedPreferences("UserData", Context.MODE_PRIVATE);
        String data = sharedPreferences.getString("user_list", "[]");

        try {
            JSONArray userArray = new JSONArray(data);
            StringBuilder displayData = new StringBuilder();

            for (int i = 0; i < userArray.length(); i++) {
                JSONObject userObject = userArray.getJSONObject(i);
                String name = userObject.getString("name");
                String email = userObject.getString("email");

                displayData.append("Name: ").append(name).append("\nEmail: ").append(email).append("\n\n");
            }

            resultTextView.setText(displayData.toString());
        } catch (JSONException e) {
            Log.e("Shared_Pref", "Error loading data from shared preferences!");
            Toast.makeText(this, "Error loading data", Toast.LENGTH_SHORT).show();
        }
    }
}