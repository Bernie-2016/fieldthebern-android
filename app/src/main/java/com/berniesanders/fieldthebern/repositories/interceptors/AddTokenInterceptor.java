package com.berniesanders.fieldthebern.repositories.interceptors;

/*
 * Made with love by volunteers
 * Copyright 2015 BernieSanders.com, FeelTheBern.org, Coderly, and the volunteers
 * License: GNU AGPLv3 - https://gnu.org/licenses/agpl.html 
 */

import com.berniesanders.fieldthebern.repositories.TokenRepo;
import com.squareup.okhttp.Interceptor;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import java.io.IOException;

public class AddTokenInterceptor implements Interceptor {

    private final TokenRepo tokenRepo;

    public AddTokenInterceptor(TokenRepo tokenRepo) {
        this.tokenRepo = tokenRepo;
    }

    @Override
    public Response intercept(Chain chain) throws IOException {

        String token = tokenRepo.get().accessToken();

        Request.Builder builder = chain.request().newBuilder();

        if (token != null) {
            builder.addHeader("Authorization", "Bearer " + token);
        }

        return chain.proceed(builder.build());
    }
}
