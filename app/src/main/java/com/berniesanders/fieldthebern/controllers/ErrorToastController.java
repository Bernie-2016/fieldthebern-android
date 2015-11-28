package com.berniesanders.fieldthebern.controllers;

import android.content.Context;
import android.widget.Toast;

import com.berniesanders.fieldthebern.models.ApiError;
import com.google.gson.Gson;

import java.io.IOException;
import java.util.Map;

import javax.inject.Inject;

import retrofit.HttpException;

/**
 *
 */
public class ErrorToastController {

    Context context;
    Gson gson;

    @Inject
    public ErrorToastController(Context context, Gson gson) {
        this.context = context;
        this.gson = gson;
    }

    public void showApiError(Throwable e) {
        String body = null;
        try {
            body = ((HttpException) e).response().errorBody().string();
        } catch (IOException e1) {
            e1.printStackTrace();
        }

        ApiError apiError = gson.fromJson(body, ApiError.class);

        Map<String, String> errorMap = apiError.getErrors();
        String firstErrorKey = errorMap.keySet().iterator().next();
        String firstErrorValue = errorMap.values().iterator().next();

        Toast.makeText(context,
                firstErrorKey + " " + firstErrorValue,
                Toast.LENGTH_SHORT)
                .show();
    }
}
