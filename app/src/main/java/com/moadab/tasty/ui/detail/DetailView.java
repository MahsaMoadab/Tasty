package com.moadab.tasty.ui.detail;

import com.moadab.tasty.remote.model.Meals;

public interface DetailView {

    void showLoading();
    void hideLoading();
    void setMeal(Meals.Meal meal);
    void onErrorLoading(String message);
}
