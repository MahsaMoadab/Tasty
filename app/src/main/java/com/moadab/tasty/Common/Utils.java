package com.moadab.tasty.Common;

import android.app.AlertDialog;
import android.content.Context;

import com.moadab.tasty.remote.api.DrinksApi;
import com.moadab.tasty.remote.api.FoodApi;
import com.moadab.tasty.remote.api.FoodClient;

public class Utils {

    public static FoodApi getApi() {
        return FoodClient.getFoodClient().create(FoodApi.class);
    }

    public static DrinksApi getDrinkApi() {
        return FoodClient.getDrinkClient().create(DrinksApi.class);
    }
    public static AlertDialog showDialogMessage(Context context, String title, String message) {
        AlertDialog alertDialog = new AlertDialog.Builder(context).setTitle(title).setMessage(message).show();
        if (alertDialog.isShowing()) {
            alertDialog.cancel();
        }
        return alertDialog;
    }


}
