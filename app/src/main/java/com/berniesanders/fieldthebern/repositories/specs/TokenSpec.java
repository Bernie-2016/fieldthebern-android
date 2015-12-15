/*
 * Copyright 2015 FeelTheBern.org
 */
package com.berniesanders.fieldthebern.repositories.specs;

import com.berniesanders.fieldthebern.models.LoginEmailRequest;
import com.berniesanders.fieldthebern.models.Token;
import com.berniesanders.fieldthebern.models.LoginFacebookRequest;

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
                @Field("grant_type") String grantType,
                @Field("username") String username,
                @Field("password") String password);

    }
}
