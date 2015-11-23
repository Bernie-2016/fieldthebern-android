package com.berniesanders.canvass.controllers;

import android.content.Context;
import android.view.View;

/**
 *
 */
public class FacebookService {

    public static final String NAME = "FacebookService.NAME";

    public static FacebookController get(Context context) {
        //noinspection ResourceType
        return (FacebookController) context.getSystemService(NAME);
    }

    public static FacebookController get(View v) {
        return get(v.getContext());
    }
}
