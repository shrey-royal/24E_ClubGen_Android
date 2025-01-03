package com.company.recipeapp.ui.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import com.bumptech.glide.Glide;
import com.company.recipeapp.R;

public class DetailsFragment extends Fragment {
    private ImageView mealImage;
    private TextView mealName, mealInstructions;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_details, container, false);

        mealImage = view.findViewById(R.id.mealImage);
        mealName = view.findViewById(R.id.mealName);
        mealInstructions = view.findViewById(R.id.mealInstructions);

        if (getArguments() != null) {
            mealName.setText(getArguments().getString("mealName"));
            mealInstructions.setText(getArguments().getString("mealInstructions"));
            Glide.with(this).load(getArguments().getString("mealImage")).into(mealImage);
        }

        return view;
    }
}
