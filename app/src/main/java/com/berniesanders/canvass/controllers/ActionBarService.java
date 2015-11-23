package com.berniesanders.canvass.controllers;

import android.content.Context;
import android.view.View;

/**
 *
 */
public class ActionBarService {

    public static final String NAME = "ActionBarService.NAME";

    public static ActionBarController getActionbarController(Context context) {
        //noinspection ResourceType
        return (ActionBarController) context.getSystemService(NAME);
    }

    public static ActionBarController getActionbarController(View v) {
        return getActionbarController(v.getContext());
    }
}
