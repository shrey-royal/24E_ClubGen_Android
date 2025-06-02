package com.royal.newsapp.network;

import com.royal.newsapp.model.NewsResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface NewsAPI {
    @GET("top-headlines")
    Call<NewsResponse> getTopHeadLines(
            @Query("country") String country,
            @Query("apiKey") String apiKey
    );
}
