package com.company.recipeapp.api;

import com.company.recipeapp.model.MealResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface MealApi {
    @GET("search.php")
    Call<MealResponse> searchMeals(@Query("s") String query);
}
