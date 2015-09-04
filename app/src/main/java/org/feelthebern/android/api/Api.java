package org.feelthebern.android.api;


import com.google.gson.Gson;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;

import javax.inject.Inject;

/**
 * Created by AndrewOrobator on 8/29/15.
 */
public class Api {
    public static final String JSON_URL = "http://feelthebern.org/ftb-json/";

    private final Gson mGson;

    @Inject
    public Api(Gson gson) {
        mGson = gson;
    }

    public void loadFeed(Callback callback) {
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url(JSON_URL)
                .build();

        client.newCall(request).enqueue(callback);
    }
}
