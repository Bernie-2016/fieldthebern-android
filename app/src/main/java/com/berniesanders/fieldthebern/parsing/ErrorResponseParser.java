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

package com.berniesanders.fieldthebern.parsing;

import com.berniesanders.fieldthebern.models.ErrorResponse;
import com.crashlytics.android.Crashlytics;
import com.google.gson.Gson;
import javax.inject.Inject;
import retrofit2.HttpException;
import timber.log.Timber;

public class ErrorResponseParser {

  private final Gson gson;

  @Inject public ErrorResponseParser(Gson gson) {
    this.gson = gson;
  }

  public ErrorResponse parse(HttpException throwable) {
    String body = null;
    try {
      body = throwable.response().errorBody().string();
      return gson.fromJson(body, ErrorResponse.class);
    } catch (Exception e) {
      Timber.e(throwable, "exception reading api errorBody json string");
      Crashlytics.logException(throwable);
    }

    //if an error occurred, you know, reading the error, return an empty ErrorResponse
    //this way at least we don't cascade the failure into a crash
    return new ErrorResponse();
  }
}
