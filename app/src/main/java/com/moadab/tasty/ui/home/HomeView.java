package com.moadab.tasty.ui.home;

import android.widget.ImageView;

import com.moadab.tasty.remote.model.Categories;
import com.moadab.tasty.remote.model.Meals;

import java.util.List;

public interface HomeView {
    void showLoading();
    void hideLoading();
    void setMeal(List<Meals.Meal> meal);
    void setCategory(List<Categories.Category> category);
    void onErrorLoading(String message);
}
