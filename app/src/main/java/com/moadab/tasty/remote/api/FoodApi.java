package com.moadab.tasty.remote.api;

import com.moadab.tasty.remote.model.Categories;
import com.moadab.tasty.remote.model.Meals;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface FoodApi {
    //https://www.themealdb.com/api/json/v2/1/latest.php
    @GET("latest.php")
    Call<Meals> getMeal();

    @GET("categories.php")
    Call<Categories> getCategories();

    //example https://themealdb.com/api/json/v2/1/filter.php?c=Beef
    @GET("filter.php") 
    Call<Meals> getMealByCategory(@Query("c") String category);

    @GET("search.php")
    Call<Meals> getMealByName(@Query("s") String mealName);
}
