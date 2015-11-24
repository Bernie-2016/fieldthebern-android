package com.berniesanders.canvass.repositories.interceptors;

/*
 * Made with love by volunteers
 * Copyright 2015 BernieSanders.com, FeelTheBern.org, Coderly, and the volunteers
 * License: GNU AGPLv3 - https://gnu.org/licenses/agpl.html 
 */

import com.berniesanders.canvass.repositories.TokenRepo;
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
        Request request = chain.request().newBuilder()
                .addHeader("Authorization", "Bearer " + tokenRepo.get().accessToken())
                .build();
        return chain.proceed(request);
    }
}
