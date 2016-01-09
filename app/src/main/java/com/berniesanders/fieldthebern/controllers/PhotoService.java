package com.berniesanders.fieldthebern.controllers;

import android.content.Context;
import android.view.View;

/**
 *
 */
public class PhotoService {

    public static final String NAME = "PhotoService.NAME";

    public static PhotoController get(Context context) {
        //noinspection ResourceType
        return (PhotoController) context.getSystemService(NAME);
    }

    public static PhotoController get(View v) {
        return get(v.getContext());
    }
}
