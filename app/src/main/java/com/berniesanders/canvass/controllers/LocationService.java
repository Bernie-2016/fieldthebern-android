package com.berniesanders.canvass.controllers;

import android.content.Context;
import android.view.View;

/**
 *
 */
public class LocationService {

    public static final String NAME = "LocationController.NAME";

    public static LocationController get(Context context) {
        //noinspection ResourceType
        return (LocationController) context.getSystemService(NAME);
    }

    public static LocationController get(View v) {
        return get(v.getContext());
    }
}
