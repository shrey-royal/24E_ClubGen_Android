package com.company.firstapp.widgets;

import android.app.AlertDialog;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.RatingBar;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.company.firstapp.R;

import java.util.ArrayList;

public class MixUIs extends AppCompatActivity {

    Spinner spinner;
    AutoCompleteTextView autoCompleteTextView;
    RatingBar ratingBar;
    SeekBar seekBar;
    Button btnAlertDialog;
    DatePicker datePicker;
    TimePicker timePicker;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_mix_uis);

        //Alert Dialog
        btnAlertDialog = findViewById(R.id.btnAlertDialog);
        btnAlertDialog.setOnClickListener(v -> new AlertDialog.Builder(MixUIs.this)
                .setTitle("AlertDialog Example")
                .setMessage("This is an AlertBog")
                .setPositiveButton("Yes", null)
                .setNegativeButton("No", null)
                .setNeutralButton("Cancel", null)
                .show());

        //Spinner
        spinner = findViewById(R.id.spinner);
        ArrayList<String> spinnerItems = new ArrayList<>();
        spinnerItems.add("Item 1");
        spinnerItems.add("Item 2");
        spinnerItems.add("Item 3");
        spinnerItems.add("Item 4");
        spinnerItems.add("Item 5");
        ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, spinnerItems);
        spinner.setAdapter(spinnerAdapter);

        //AutoCompleteTextView
        autoCompleteTextView = findViewById(R.id.autoCompleteTextView);
        String[] suggestions = {"Apple", "Banana", "Cherry", "Date", "Elderberry"};
        ArrayAdapter<String> autoCompleteAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, suggestions);
        autoCompleteTextView.setAdapter(autoCompleteAdapter);

        //RatingBar
        ratingBar = findViewById(R.id.ratingBar);
        ratingBar.setOnRatingBarChangeListener((ratingBar, rating, fromUser) -> Toast.makeText(this, "Rating: " + rating, Toast.LENGTH_SHORT).show());

        //SeekBar
        seekBar = findViewById(R.id.seekBar);
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                Toast.makeText(MixUIs.this, "Progress: " + progress, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {}

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {}
        });

        //Date picker / Time picker
        datePicker = findViewById(R.id.datePicker);
        timePicker = findViewById(R.id.timePicker);
//        timePicker.setIs24HourView(true);
    }
}