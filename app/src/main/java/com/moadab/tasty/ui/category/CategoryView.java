package com.moadab.tasty.ui.category;

import android.os.Bundle;

import com.moadab.tasty.remote.model.Meals;

import java.util.List;

public interface CategoryView {
    void showLoading();
    void hideLoading();
    void setMeals(List<Meals.Meal> meals);
    void onErrorLoading(String message);

    void setArguments(Bundle args);
}
