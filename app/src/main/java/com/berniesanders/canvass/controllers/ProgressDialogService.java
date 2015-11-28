package com.berniesanders.canvass.controllers;

import android.content.Context;
import android.view.View;

/**
 *
 */
public class ProgressDialogService {

    public static final String NAME = "ExampleService.NAME";

    public static ProgressDialogController get(Context context) {
        //noinspection ResourceType
        return (ProgressDialogController) context.getSystemService(NAME);
    }

    public static ProgressDialogController get(View v) {
        return get(v.getContext());
    }
}
