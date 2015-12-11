package com.berniesanders.fieldthebern.models;

import com.bugsnag.android.Bugsnag;
import com.google.gson.Gson;

import java.io.IOException;

import retrofit.HttpException;


/**
 * ErrorResponse
 * <p/>
 * JSON Format
 * {
 * "errors": [
 * {
 * "id": "DASHERIZED_CAPITALIZED_ERROR_NAME",
 * "title": "User friendly error name",
 * "detail": "Value of Error.message",
 * "status": "HTTP_CODE_IN_INTEGER_FORMAT"
 * }
 * ]
 * }
 */
public class ErrorResponse {
    public Error[] errors;

    public static class Error {
        public String id;
        public String title;
        public String detail;
        public int status;

        @Override
        public String toString() {
            return title + " " + detail;
        }
    }

    public static String[] parse(HttpException throwable) {
        String body = null;
        try {
            body = throwable.response().errorBody().string();
        } catch (IOException e) {
            Bugsnag.notify(new Exception("Error parsing error response"));
        }
        Gson gson = new Gson();

        ErrorResponse response = gson.fromJson(body, ErrorResponse.class);
        String[] returnVal = new String[response.errors.length];

        for (int i = 0; i < returnVal.length; i++) {
            returnVal[i] = response.errors[i].toString();
        }

        return returnVal;

    }
}
