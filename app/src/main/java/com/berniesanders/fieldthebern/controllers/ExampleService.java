package com.berniesanders.fieldthebern.controllers;

import android.content.Context;
import android.view.View;

/**
 *
 */
public class ExampleService {

    public static final String NAME = "ExampleService.NAME";

    public static ExampleController get(Context context) {
        //noinspection ResourceType
        return (ExampleController) context.getSystemService(NAME);
    }

    public static ExampleController get(View v) {
        return get(v.getContext());
    }
}
