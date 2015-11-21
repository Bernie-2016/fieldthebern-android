package com.berniesanders.canvass.config;

import android.content.Context;
import com.berniesanders.canvass.R;

/**
 * Central place for configuring data access urls
 */
public class ConfigImpl implements Config {

    //includes json for the whole site, not used currently
    //public static final String FULL_JSON_URL = "http://feelthebern.org/ftb-json/";

    private final String BASE_URL;
    private final String CANVASS_URL;

    private final String CLIENT_ID;
    private final String CLIENT_SECRET;

    public ConfigImpl(Context context) {
        BASE_URL = context.getString(R.string.baseUrl);
        CANVASS_URL = context.getString(R.string.canvassUrl);
        CLIENT_ID = context.getString(R.string.emailClientId);
        CLIENT_SECRET = context.getString(R.string.emailClientSecret);
    }

    @Override
    public String getFeelTheBernUrl() {
        return BASE_URL;
    }

    @Override
    public String getCanvassUrl() {
        return CANVASS_URL;
    }

    @Override
    public String getClientId() {
        return CLIENT_ID;
    }

    @Override
    public String getClientSecret() {
        return CLIENT_SECRET;
    }
}
