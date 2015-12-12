package com.berniesanders.fieldthebern.parsing;

import com.berniesanders.fieldthebern.models.ErrorResponse;
import com.bugsnag.android.Bugsnag;
import com.google.gson.Gson;

import java.io.IOException;

import javax.inject.Inject;

import retrofit.HttpException;
import timber.log.Timber;

/*
 * Made with love by volunteers
 * Copyright 2015 BernieSanders.com, FeelTheBern.org, 
 * Coderly, LostPacketSoftware and the volunteers
 * License: GNU AGPLv3 - https://gnu.org/licenses/agpl.html 
 */
public class ErrorResponseParser {


    private final Gson gson;

    @Inject
    public ErrorResponseParser(Gson gson) {
        this.gson = gson;
    }


    public ErrorResponse parse(HttpException throwable) {
        String body = null;
        try {
            body = throwable.response().errorBody().string();
        } catch (IOException e) {
            Timber.e(throwable, "exception reading api errorBody json string");
            Bugsnag.notify(throwable);
        }

        return gson.fromJson(body, ErrorResponse.class);
    }
}
