package com.company.firstapp.menuss;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.company.firstapp.R;

public class Option_Menu_1 extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_option_menu1);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.options_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.option1)
            Toast.makeText(this, "Option 1 Selected", Toast.LENGTH_SHORT).show();
        else if (item.getItemId() == R.id.option2)
            Toast.makeText(this, "Option 2 Selected", Toast.LENGTH_SHORT).show();
        else if (item.getItemId() == R.id.option3)
            Toast.makeText(this, "Option 3 Selected", Toast.LENGTH_SHORT).show();
        else
            return super.onOptionsItemSelected(item);

        return true;
    }
}