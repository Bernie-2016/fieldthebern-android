package com.berniesanders.fieldthebern.controllers;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.berniesanders.fieldthebern.models.ErrorResponse;
import com.google.gson.Gson;

import java.io.IOException;

import javax.inject.Inject;

import retrofit.HttpException;

/**
 *
 */
public class ToastController {

    Context context;

    @Inject
    public ToastController(Context context) {
        this.context = context;
    }

    public void toast(String... text) {
        for (int i = 0; i < text.length; i++) {
            Toast.makeText(context, text[i], Toast.LENGTH_LONG).show();
        }
    }
}
