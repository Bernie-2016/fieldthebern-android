/*
 * Copyright 2015 FeelTheBern.org
 */
package com.berniesanders.canvass.repositories.specs;

import com.berniesanders.canvass.models.LoginEmailRequest;
import com.berniesanders.canvass.models.Token;
import com.berniesanders.canvass.models.LoginFacebookRequest;

import retrofit.http.Body;
import retrofit.http.Field;
import retrofit.http.FormUrlEncoded;
import retrofit.http.Header;
import retrofit.http.Headers;
import retrofit.http.POST;
import rx.Observable;

/**
 * Used to configure/filter a request to the data layer repository
 */
public class TokenSpec {

    private static final String CLIENT_ID = "cbe732e440995e8ae73dfb093de4b880f3a68346e4b1bf933e626599a090bdec";
    private static final String CLIENT_SECRET = "30d732185171be840adff6e11aae9157614bb7be2da2d27100bcb7bc938ac369";


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

        @FormUrlEncoded
        @Headers({"Accept:application/json",
                "Content-Type:application/x-www-form-urlencoded; charset=utf-8",
        })
        @POST("oauth/token")
        Observable<Token> loginEmail(
                @Header("Authorization") String authString,
                @Field("grant_type") String grantType,
                @Field("username") String username,
                @Field("password") String password);


        @FormUrlEncoded
        @Headers({"Accept:application/json",
                "Content-Type:application/x-www-form-urlencoded; charset=utf-8",
        })
        @POST("oauth/token")
        Observable<Token> loginFacebook(
                @Header("Authorization") String authString,
                @Body LoginFacebookRequest loginFacebookRequest);

    }
}
