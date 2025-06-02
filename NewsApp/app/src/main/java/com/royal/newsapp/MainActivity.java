package com.royal.newsapp;

import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.royal.newsapp.adapter.NewsAdapter;
import com.royal.newsapp.model.Article;
import com.royal.newsapp.model.NewsResponse;
import com.royal.newsapp.network.NewsAPI;
import com.royal.newsapp.network.RetroFitClient;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {
    RecyclerView recyclerView;
    NewsAdapter adapter;
    List<Article> articles = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        recyclerView = findViewById(R.id.recyclerView);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new NewsAdapter(articles, this);
        recyclerView.setAdapter(adapter);

        fetchNews();
    }

    private void fetchNews() {
        NewsAPI api = RetroFitClient.getInstance().create(NewsAPI.class);
        Call<NewsResponse> call = api.getTopHeadLines("us", "b781229af6f841669e35fa7251a218bc");

        call.enqueue(new Callback<NewsResponse>() {
            @Override
            public void onResponse(Call<NewsResponse> call, Response<NewsResponse> response) {
                if (response.isSuccessful()) {
                    articles.addAll(response.body().articles);
                    adapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onFailure(Call<NewsResponse> call, Throwable t) {
                Toast.makeText(MainActivity.this, "Failed to load news", Toast.LENGTH_SHORT).show();
            }
        });
    }
}