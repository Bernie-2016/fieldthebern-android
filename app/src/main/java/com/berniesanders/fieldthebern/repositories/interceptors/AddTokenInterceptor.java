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

package com.berniesanders.fieldthebern.repositories.interceptors;

import com.berniesanders.fieldthebern.models.Token;
import com.berniesanders.fieldthebern.repositories.TokenRepo;
import java.io.IOException;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

public class AddTokenInterceptor implements Interceptor {

  private final TokenRepo tokenRepo;

  public AddTokenInterceptor(TokenRepo tokenRepo) {
    this.tokenRepo = tokenRepo;
  }

  @Override
  public Response intercept(Chain chain) throws IOException {

    Request.Builder builder = chain.request().newBuilder();

    Token token = tokenRepo.get();

    if (token != null) {
      builder.addHeader("Authorization", "Bearer " + token.accessToken());
    }

    return chain.proceed(builder.build());
  }
}
