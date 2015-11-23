package com.berniesanders.canvass.config;

import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;
import android.text.TextUtils;

import com.berniesanders.canvass.R;
import com.google.common.base.Joiner;

import java.util.LinkedList;

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
    private final String USER_AGENT;

    public ConfigImpl(Context context) {
        BASE_URL = context.getString(R.string.baseUrl);
        CANVASS_URL = context.getString(R.string.canvassUrl);
        CLIENT_ID = context.getString(R.string.oauth2ClientId);
        CLIENT_SECRET = context.getString(R.string.oauth2ClientSecret);

        String name = "FieldTheBern";
        String version = null;
        try {
            version = context.getPackageManager().getPackageInfo(context.getPackageName(), 0).versionName;
        } catch (PackageManager.NameNotFoundException e) {
            version = "dev";
        }

        LinkedList<String> comments = new LinkedList<>();
        if(Build.VERSION.RELEASE != null) {
            comments.add("Android " + Build.VERSION.RELEASE);
        }
        if(Build.MODEL != null) {
            comments.add(Build.MODEL);
        }

        String nameAndVersion = name + "/" + version;

        String comment = Joiner.on("; ").join(comments);
        if(comment.length() > 0) {
            comment = " (" + comment + ")";
        }

        USER_AGENT = nameAndVersion + comment;
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

    @Override
    public String getUserAgent() {
        return USER_AGENT;
    }
}
