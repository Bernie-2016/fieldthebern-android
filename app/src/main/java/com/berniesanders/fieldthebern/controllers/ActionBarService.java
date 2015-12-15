package com.berniesanders.fieldthebern.controllers;

import android.content.Context;
import android.view.View;

/**
 *
 */
public class ActionBarService {

    public static final String NAME = "ActionBarService.NAME";

    public static ActionBarController get(Context context) {
        //noinspection ResourceType
        return (ActionBarController) context.getSystemService(NAME);
    }

    public static ActionBarController get(View v) {
        return get(v.getContext());
    }
}
