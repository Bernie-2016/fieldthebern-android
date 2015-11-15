/*
 * Copyright 2015 FeelTheBern.org
 */
package com.berniesanders.canvass.repositories.specs;

import com.berniesanders.canvass.models.LoginEmailRequest;
import com.berniesanders.canvass.models.LoginEmailResponse;
import com.berniesanders.canvass.models.LoginFacebookRequest;
import com.berniesanders.canvass.models.LoginFacebookResponse;

import retrofit.http.Body;
import retrofit.http.Headers;
import retrofit.http.POST;
import rx.Observable;

/**
 * Used to configure/filter a request to the data layer repository
 */
public class TokenSpec {

    LoginEmailRequest loginEmailRequest;
    LoginFacebookRequest loginFacebookRequest;

    public TokenSpec() {
    }


    public TokenSpec email(LoginEmailRequest loginEmailRequest) {
        this.loginEmailRequest = loginEmailRequest;
        return this;
    }

    public TokenSpec facebook(LoginFacebookRequest loginFacebookRequest) {
        this.loginFacebookRequest = loginFacebookRequest;
        return this;
    }


    public LoginEmailRequest getEmail() {
        return loginEmailRequest;
    }

    public LoginFacebookRequest getFacebook() {
        return loginFacebookRequest;
    }

    /**
     * Retrofit 2 endpoint definition
     */
    public interface TokenEndpoint {

        @Headers({"Content-Type:application/json"})
        @POST("oauth/tokens")
        Observable<LoginEmailResponse> loginEmail(@Body LoginEmailRequest loginEmailRequest);

        @Headers({"Content-Type:application/json"})
        @POST("oauth/tokens")
        Observable<LoginFacebookResponse> loginFacebook(@Body LoginFacebookRequest loginFacebookRequest);

    }
}
