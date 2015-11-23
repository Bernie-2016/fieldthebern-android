package com.berniesanders.canvass.repositories.interceptors;

import com.berniesanders.canvass.config.Config;
import com.squareup.okhttp.Interceptor;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import java.io.IOException;

public class UserAgentInterceptor implements Interceptor {

    private final String userAgent;

    public  UserAgentInterceptor(String userAgent) {
        this.userAgent = userAgent;
    }

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request request = chain.request().newBuilder()
                .removeHeader("User-Agent")
                .addHeader("User-Agent", userAgent)
                .build();
        return chain.proceed(request);
    }
}
