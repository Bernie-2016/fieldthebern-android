package com.berniesanders.fieldthebern.controllers;

import android.content.Context;
import android.view.View;

/**
 *
 */
public class PermissionService {

    public static final String NAME = "PermissionService.NAME";

    public static PermissionController get(Context context) {
        //noinspection ResourceType
        return (PermissionController) context.getSystemService(NAME);
    }

    public static PermissionController get(View v) {
        return get(v.getContext());
    }
}
