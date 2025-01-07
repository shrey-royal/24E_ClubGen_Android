package com.company.firstapp.menuss;

import android.os.Bundle;
import android.view.ContextMenu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.company.firstapp.R;

public class Context_Menu_1 extends AppCompatActivity {

    TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_context_menu1);

        textView = findViewById(R.id.cntx_menu_tv);

        registerForContextMenu(textView);
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.context_menu, menu);
    }

    @Override
    public boolean onContextItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.action_edit)
            Toast.makeText(this, "Edit Clicked", Toast.LENGTH_SHORT).show();
        else if (item.getItemId() == R.id.action_delete)
            Toast.makeText(this, "Delete Clicked", Toast.LENGTH_SHORT).show();
        else if (item.getItemId() == R.id.action_share)
            Toast.makeText(this, "Share Clicked", Toast.LENGTH_SHORT).show();
        else
            return super.onOptionsItemSelected(item);

        return true;
    }
}