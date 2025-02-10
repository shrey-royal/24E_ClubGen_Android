package com.company.proapp.storage;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.company.proapp.R;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class InternalStorage extends AppCompatActivity {

    private EditText fileName, fileContent;
    private Button btn_f_save, btn_f_read;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_internal_storage);

        fileName = findViewById(R.id.editTextFileName);
        fileContent = findViewById(R.id.editTextFileContent);
        btn_f_save = findViewById(R.id.btn_f_save);
        btn_f_read = findViewById(R.id.btn_f_read);

        btn_f_save.setOnClickListener(v -> {
            String fname = fileName.getText().toString().trim();
            String fcontent = fileContent.getText().toString().trim();

            if (fname.isEmpty()) {
                Toast.makeText(this, "Please enter a filename", Toast.LENGTH_SHORT).show();
                return;
            }
            if(fcontent.isEmpty()) {
                Toast.makeText(this, "Please enter the filecontent", Toast.LENGTH_SHORT).show();
                return;
            }
            saveToFile(fname, fcontent);
        });

        btn_f_read.setOnClickListener(v -> {
            String fname = fileName.getText().toString().trim();
            if (fname.isEmpty()) {
                Toast.makeText(this, "Please enter a filename", Toast.LENGTH_SHORT).show();
                return;
            }
            readFromFile(fname);
        });
    }

    private void saveToFile(String fileName, String fileContent) {
        FileOutputStream fos = null;

        try {
            fos = openFileOutput(fileName, MODE_PRIVATE);
            fos.write(fileContent.getBytes());
            Toast.makeText(this, "File saved successfully!", Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            System.out.println(e.getMessage());
            Toast.makeText(this, "Error saving file", Toast.LENGTH_SHORT).show();
        } finally {
            if (fos != null) {
                try {
                    fos.close();
                } catch (IOException e) {
                    System.out.println(e.getMessage());
                }
            }
        }
    }

    private void readFromFile(String fname) {
        FileInputStream fis = null;

        try {
            fis = openFileInput(fname);
            StringBuilder fileContent = new StringBuilder();
            int c;
            while((c = fis.read()) != -1) {
                fileContent.append((char) c);
            }
            Toast.makeText(this, "File Content: " + fileContent.toString(), Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            System.out.println(e.getMessage());
            Toast.makeText(this, "Error reading File", Toast.LENGTH_SHORT).show();
        } finally {
            if (fis != null) {
                try {
                    fis.close();
                } catch (IOException e) {
                    System.out.println(e.getMessage());
                }
            }
        }
    }
}