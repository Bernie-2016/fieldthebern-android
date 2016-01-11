package com.berniesanders.fieldthebern.parsing;

import com.berniesanders.fieldthebern.models.ErrorResponse;
import com.crashlytics.android.Crashlytics;
import com.google.gson.Gson;

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
            return gson.fromJson(body, ErrorResponse.class);
        } catch (Exception e) {
            Timber.e(throwable, "exception reading api errorBody json string");
            Crashlytics.logException(throwable);
        }

        //if an error occurred, you know, reading the error, return an empty ErrorResponse
        //this way at least we don't cascade the failure into a crash
        return new ErrorResponse();
    }
}
