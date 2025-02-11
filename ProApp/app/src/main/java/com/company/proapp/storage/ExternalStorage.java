package com.company.proapp.storage;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.company.proapp.R;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

public class ExternalStorage extends AppCompatActivity {

    private static final int REQUEST_STORAGE_PERMISSION = 100;
    private final String FILE_NAME = "user_data.txt";
    private ListView listViewData;
    private final ArrayList<String> userDataList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_external_storage);

        Button btnSaveData = findViewById(R.id.btnSaveData);
        Button btnFetchData = findViewById(R.id.btnFetchData);
        listViewData = findViewById(R.id.listViewData);

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_STORAGE_PERMISSION);
        }

        btnSaveData.setOnClickListener(v -> saveDataToFile());
        btnFetchData.setOnClickListener(v -> fetchDataFromFile());
    }

    private void saveDataToFile() {
        String[] userNames = {"Alice", "Bob", "Charlie", "David"};

        if(isExternalStorageWritable()) {
            File f = new File(getExternalFilesDir(null), FILE_NAME);
            try (FileWriter w = new FileWriter(f)) {
                for (String name : userNames) {
                    w.write(name + "\n");
                }
                Toast.makeText(this, "File written successfully", Toast.LENGTH_SHORT).show();
            } catch (IOException e) {
                Log.e("EXTERNAL_STORAGE", "IOException Error!");
                Toast.makeText(this, "IOException", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void fetchDataFromFile() {
        userDataList.clear();

        if (isExternalStorageReadable()) {
            File file = new File(getExternalFilesDir(null), FILE_NAME);
            if (file.exists()) {
                try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
                    String line;
                    while ((line = reader.readLine()) != null) {
                        userDataList.add(line);
                    }
                    displayDataInListView();
                } catch (IOException e) {
                    Log.e("EXTERNAL_STORAGE", "IOException Error!");
                    Toast.makeText(this, "IOException", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(this, "No data file found.", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void displayDataInListView() {
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, userDataList);
        listViewData.setAdapter(adapter);
    }

    private boolean isExternalStorageWritable() {
        return Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED);
    }

    private boolean isExternalStorageReadable() {
        String state = Environment.getExternalStorageState();
        return state.equals(Environment.MEDIA_MOUNTED) || state.equals(Environment.MEDIA_MOUNTED_READ_ONLY);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_STORAGE_PERMISSION) {
            if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "Permission granted.", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Permissions denied.", Toast.LENGTH_SHORT).show();
            }
        }
    }
}