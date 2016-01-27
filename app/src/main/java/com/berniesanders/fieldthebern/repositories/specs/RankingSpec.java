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

import android.support.annotation.StringDef;
import com.berniesanders.fieldthebern.models.Rankings;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import retrofit2.http.GET;
import retrofit2.http.Query;
import rx.Observable;

/**
 * Used to configure/filter a request to the data layer repository
 */
public class RankingSpec {

  public static final String EVERYONE = "everyone";
  public static final String STATE = "state";
  public static final String FRIENDS = "friends";

  @RankType
  private final String type;

  /**
   * type: EVERYONE | STATE | FRIENDS
   */
  public RankingSpec(@RankType String type) {
    this.type = type;
  }

  @RankType
  public String type() {
    return type;
  }

  @StringDef({ EVERYONE, STATE, FRIENDS })
  @Retention(RetentionPolicy.SOURCE)
  public @interface RankType {
  }

  /**
   * Retrofit 2 endpoint definition
   */
  public interface RankEndpoint {

    @GET("rankings")
    Observable<Rankings> get(@Query("type") @RankType String type);
  }
}
