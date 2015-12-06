/*
 * Copyright 2015 FeelTheBern.org
 */
package com.berniesanders.fieldthebern.repositories.specs;

import com.berniesanders.fieldthebern.models.CreateUserRequest;
import com.berniesanders.fieldthebern.models.User;

import retrofit.http.Body;
import retrofit.http.GET;
import retrofit.http.Headers;
import retrofit.http.PATCH;
import retrofit.http.POST;
import retrofit.http.Path;
import rx.Observable;

/**
 * Used to configure/filter a request to the data layer repository
 */
public class UserSpec {

    private User user;
    CreateUserRequest createUserRequest;

    public UserSpec() {
    }


    public UserSpec update(User user) {
        this.user = user;
        return this;
    }

    public UserSpec create(CreateUserRequest createUserRequest) {
        this.createUserRequest = createUserRequest;
        return this;
    }

    public User user() {
        return user;
    }

    public CreateUserRequest getCreateUserRequest() {
        return createUserRequest;
    }

    /**
     * Retrofit 2 endpoint definition
     */
    public interface UserEndpoint {

        @Headers({"Content-Type:application/json"})
        @POST("users")
        Observable<User> create(@Body CreateUserRequest createUserRequest);

        @Headers({"Content-Type:application/json"})
        @PATCH("users/me")
        Observable<User> update(@Body CreateUserRequest createUserRequest);

        @GET("users/{id}")
        Observable<User> get(@Path("id") int id);

        @GET("users/me")
        Observable<User> getMe();
    }
}
