package com.berniesanders.fieldthebern.controllers;

import android.content.Context;
import android.view.View;

/**
 *
 */
public class ToastService {

    public static final String NAME = "ToastController.NAME";

    public static ToastController get(Context context) {
        //noinspection ResourceType
        return (ToastController) context.getSystemService(NAME);
    }

    public static ToastController get(View v) {
        return get(v.getContext());
    }
}
