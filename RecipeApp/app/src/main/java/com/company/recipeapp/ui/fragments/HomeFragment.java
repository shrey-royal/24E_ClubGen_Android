package com.company.recipeapp.ui.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.company.recipeapp.R;
import com.company.recipeapp.api.ApiClient;
import com.company.recipeapp.api.MealApi;
import com.company.recipeapp.model.Meal;
import com.company.recipeapp.model.MealResponse;
import com.company.recipeapp.ui.adapters.MealAdapter;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HomeFragment extends Fragment {
    private RecyclerView recyclerView;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        recyclerView = view.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        fetchMeals();
        return view;
    }

    private void fetchMeals() {
        MealApi api = ApiClient.getRetrofitInstance().create(MealApi.class);
        api.searchMeals("pasta").enqueue(new Callback<MealResponse>() {
            @Override
            public void onResponse(Call<MealResponse> call, Response<MealResponse> response) {
                if (response.body() != null) {
                    List<Meal> meals = response.body().getMeals();
                    MealAdapter adapter = new MealAdapter(meals, meal -> {
                        Bundle bundle = new Bundle();
                        bundle.putString("mealName", meal.getStrMeal());
                        bundle.putString("mealImage", meal.getStrMealThumb());
                        bundle.putString("mealInstructions", meal.getStrInstructions());
                        Navigation.findNavController(requireView()).navigate(R.id.action_home_to_details, bundle);
                    });
                    recyclerView.setAdapter(adapter);
                }
            }

            @Override
            public void onFailure(Call<MealResponse> call, Throwable t) {
                // Handle error
            }
        });
    }
}
