/*
 * Copyright (c) 2016 - Bernie 2016, Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */
package com.berniesanders.fieldthebern.repositories.specs;

import com.berniesanders.fieldthebern.models.CreateUserRequest;
import com.berniesanders.fieldthebern.models.User;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.PATCH;
import retrofit2.http.POST;
import retrofit2.http.Path;
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

    @Headers({ "Content-Type:application/json" }) @POST("users") Observable<User> create(
        @Body CreateUserRequest createUserRequest);

    @Headers({ "Content-Type:application/json" }) @PATCH("users/me") Observable<User> update(
        @Body CreateUserRequest createUserRequest);

    @GET("users/{id}") Observable<User> get(@Path("id") int id);

    @GET("users/me") Observable<User> getMe();
  }
}
