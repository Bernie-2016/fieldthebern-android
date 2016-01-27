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

import com.berniesanders.fieldthebern.models.LoginEmailRequest;
import com.berniesanders.fieldthebern.models.LoginFacebookRequest;
import com.berniesanders.fieldthebern.models.Token;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.POST;
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
    @Headers({
        "Accept:application/json", "Content-Type:application/x-www-form-urlencoded; charset=utf-8",
    })
    @POST("oauth/token")
    Observable<Token> loginEmail(@Header("Authorization") String authString,
        @Field("grant_type") String grantType, @Field("username") String username,
        @Field("password") String password);

    @FormUrlEncoded
    @Headers({
        "Accept:application/json", "Content-Type:application/x-www-form-urlencoded; charset=utf-8",
    })
    @POST("oauth/token")
    Observable<Token> loginFacebook(@Header("Authorization") String authString,
        @Field("grant_type") String grantType, @Field("username") String username,
        @Field("password") String password);

    @FormUrlEncoded
    @Headers({
        "Accept:application/json", "Content-Type:application/x-www-form-urlencoded; charset=utf-8",
    })
    @POST("oauth/token")
    Observable<Token> refresh(
        //@Header("Authorization") String authString,
        @Field("grant_type") String grantType, @Field("client_id") String clientId,
        @Field("client_secret") String clientSecret, @Field("refresh_token") String refreshToken);
  }
}
