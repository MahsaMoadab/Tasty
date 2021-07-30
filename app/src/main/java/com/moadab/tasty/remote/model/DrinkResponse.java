package com.moadab.tasty.remote.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class DrinkResponse {

    @SerializedName("drinks")
    private List<Drinks> drinks;

    public List<Drinks> getDrinks() {
        return drinks;
    }

    public void setDrinks(List<Drinks> drinks) {
        this.drinks = drinks;
    }
}
