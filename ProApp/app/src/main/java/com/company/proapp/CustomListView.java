package com.company.proapp;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class CustomListView extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_view);

        ListView lv = findViewById(R.id.lv);
        ArrayList<ItemModel> items = new ArrayList<>();

        items.add(new ItemModel("Apple", "A tasty fruit", R.drawable.apple));
        items.add(new ItemModel("Banana", "A yellow fruit", R.drawable.banana));
        items.add(new ItemModel("Cherry", "A small red fruit", R.drawable.cherry));

        CustomAdapter adapter = new CustomAdapter(this, items);
        lv.setAdapter(adapter);

        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                ItemModel selectedItem = items.get(i);
                Toast.makeText(CustomListView.this, "Clicked: " + selectedItem.getTitle(), Toast.LENGTH_SHORT).show();
            }
        });


    }
}