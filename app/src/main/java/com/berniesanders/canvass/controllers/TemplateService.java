package com.berniesanders.canvass.controllers;

import android.content.Context;
import android.view.View;

/**
 *
 */
public class TemplateService {

    public static final String NAME = "TemplateController.NAME";

    public static TemplateController get(Context context) {
        //noinspection ResourceType
        return (TemplateController) context.getSystemService(NAME);
    }

    public static TemplateController get(View v) {
        return get(v.getContext());
    }
}
