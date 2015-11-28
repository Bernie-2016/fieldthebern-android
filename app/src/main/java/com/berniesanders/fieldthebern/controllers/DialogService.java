package com.berniesanders.fieldthebern.controllers;

import android.content.Context;
import android.view.View;

/**
 *
 */
public class DialogService {

    public static final String NAME = DialogService.class.getName();

    public static DialogController get(Context context) {
        //noinspection ResourceType
        return (DialogController) context.getSystemService(NAME);
    }

    public static DialogController get(View v) {
        return get(v.getContext());
    }
}
