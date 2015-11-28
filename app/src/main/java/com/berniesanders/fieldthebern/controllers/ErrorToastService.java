package com.berniesanders.fieldthebern.controllers;

import android.content.Context;
import android.view.View;

/**
 *
 */
public class ErrorToastService {

    public static final String NAME = "ErrorToastController.NAME";

    public static ErrorToastController get(Context context) {
        //noinspection ResourceType
        return (ErrorToastController) context.getSystemService(NAME);
    }

    public static ErrorToastController get(View v) {
        return get(v.getContext());
    }
}
