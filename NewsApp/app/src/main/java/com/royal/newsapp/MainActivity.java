package com.royal.newsapp;

import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;
import com.google.android.material.navigation.NavigationView;
import com.royal.newsapp.adapter.NewsAdapter;
import com.royal.newsapp.model.Article;
import com.royal.newsapp.model.NewsResponse;
import com.royal.newsapp.network.NewsAPI;
import com.royal.newsapp.network.RetroFitClient;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private NewsAdapter adapter;
    private final List<Article> articles = new ArrayList<>();
    private final String[] categories = {"Top", "Business", "Technology", "Health", "Sports", "Science"};
    private ChipGroup chipGroup;
    private ProgressBar progressBar;
    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
    private Toolbar toolbar;

    private static final String API_KEY = "b781229af6f841669e35fa7251a218bc";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recyclerView = findViewById(R.id.recyclerView);
        chipGroup = findViewById(R.id.chipGroup);
        progressBar = findViewById(R.id.progressBar);

        drawerLayout = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view);
        toolbar = findViewById(R.id.toolbar);

        setSupportActionBar(toolbar);

        // Setup Drawer Toggle (hamburger icon)
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawerLayout, toolbar,
                R.string.navigation_drawer_open, R.string.navigation_drawer_close
        );
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        // Handle Navigation item clicks
        navigationView.setNavigationItemSelectedListener(item -> {
            int id = item.getItemId();

            if (id == R.id.nav_home) {
                Toast.makeText(MainActivity.this, "Home selected", Toast.LENGTH_SHORT).show();
            } else if (id == R.id.nav_settings) {
                Toast.makeText(MainActivity.this, "Settings selected", Toast.LENGTH_SHORT).show();
            }

            drawerLayout.closeDrawer(GravityCompat.START);
            return true;
        });

        setupCategoryChips();
        setupRecyclerView();

        fetchNews("general");
    }

    private void setupCategoryChips() {
        for (String category : categories) {
            Chip chip = new Chip(this);
            chip.setText(category);
            chip.setCheckable(true);
            chip.setChipBackgroundColorResource(R.color.surface);
            chip.setTextColor(getResources().getColor(R.color.textPrimary));
            chip.setTextAppearance(android.R.style.TextAppearance_Material_Large);
            chip.setRippleColorResource(R.color.primary);
            chipGroup.addView(chip);
        }

        chipGroup.setOnCheckedChangeListener((group, checkedId) -> {
            if (checkedId != View.NO_ID) {
                Chip chip = group.findViewById(checkedId);
                String selectedCategory = chip.getText().toString().toLowerCase();

                if (selectedCategory.equals("top")) {
                    fetchNews("general");
                } else {
                    fetchNews(selectedCategory);
                }
            }
        });
    }

    private void setupRecyclerView() {
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new NewsAdapter(articles, this);
        recyclerView.setAdapter(adapter);
    }

    private void fetchNews(String category) {
        showLoading(true);
        articles.clear();
        adapter.notifyDataSetChanged();

        NewsAPI api = RetroFitClient.getInstance().create(NewsAPI.class);
        Call<NewsResponse> call = api.getCategoryNews("us", category, API_KEY);

        call.enqueue(new Callback<NewsResponse>() {
            @Override
            public void onResponse(Call<NewsResponse> call, Response<NewsResponse> response) {
                showLoading(false);
                if (response.isSuccessful() && response.body() != null) {
                    articles.addAll(response.body().getArticles());
                    adapter.notifyDataSetChanged();
                } else {
                    Toast.makeText(MainActivity.this, "No news found.", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<NewsResponse> call, Throwable t) {
                showLoading(false);
                Toast.makeText(MainActivity.this, "Failed to load news", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void showLoading(boolean isLoading) {
        if (progressBar != null) {
            progressBar.setVisibility(isLoading ? View.VISIBLE : View.GONE);
        }
        recyclerView.setVisibility(isLoading ? View.GONE : View.VISIBLE);
    }

}