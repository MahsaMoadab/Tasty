package com.moadab.tasty.ui.home;

import com.moadab.tasty.remote.model.Categories;
import com.moadab.tasty.remote.model.Drinks;

import java.util.List;

public interface HomeDrinkView {
    void showLoading();
    void hideLoading();
    void setMeal(List<Drinks> drinks);
    void setCategory(List<Categories.Category> category);
    void onErrorLoading(String message);
}
