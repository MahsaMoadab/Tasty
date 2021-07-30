package com.moadab.tasty.remote.api;

import com.moadab.tasty.remote.model.Categories;
import com.moadab.tasty.remote.model.DrinkResponse;
import com.moadab.tasty.remote.model.Drinks;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface DrinksApi {
    //https://www.themealdb.com/api/json/v2/1/latest.php
    @GET("latest.php")
    Call<DrinkResponse> getMeal();

    //https://www.thecocktaildb.com/api/json/v1/1/list.php?c=list
    @GET("popular.php")
    Call<Categories> getPopular();

    //example https://themealdb.com/api/json/v2/1/filter.php?c=Beef
    @GET("filter.php") 
    Call<Drinks> getMealByCategory(@Query("c") String category);

    @GET("search.php")
    Call<Drinks> getMealByName(@Query("s") String mealName);
}
