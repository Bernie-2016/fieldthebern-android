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

package com.berniesanders.fieldthebern.models;

/**
 * See <a href="https://github.com/Bernie-2016/ground-game-api/wiki/API-Tokens">https://github.com/Bernie-2016/ground-game-api/wiki/API-Tokens</a>
 */
public class LoginFacebookRequest {

  private final String grant_type = "password";

  private String username;

  private String password;

  public LoginFacebookRequest() {
  }

  public String getGrantType() {
    return grant_type;
  }

  /**
   * email
   */
  public String username() {
    return username;
  }

  public String password() {
    return password;
  }

  public LoginFacebookRequest password(String token) {
    this.password = token;
    return this;
  }

  public LoginFacebookRequest username(String email) {
    this.username = email;
    return this;
  }
}
