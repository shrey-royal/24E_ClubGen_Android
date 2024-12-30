package com.company.firstapp.fragmentssss;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.company.firstapp.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class Fragment_1 extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fragment1);
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);

        if(savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.frag_container, new HomeFrag())
                    .commit();
        }

        bottomNavigationView.setOnItemSelectedListener(item -> {
            Fragment selectedFragment = null;

            if (item.getItemId() == R.id.nav_home) {
                selectedFragment = new HomeFrag();
            } else if (item.getItemId() == R.id.nav_search) {
                selectedFragment = new SearchFrag();
            } else if (item.getItemId() == R.id.nav_profile) {
                selectedFragment = new ProfileFrag();
            }

            if (selectedFragment != null) {
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.frag_container, selectedFragment)
                        .commit();
            }

            return true;
        });
    }
}