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

import com.berniesanders.fieldthebern.models.Collection;
import retrofit2.http.GET;
import retrofit2.http.Path;
import rx.Observable;

/**
 * Used to configure/filter a request to the data layer repository
 * which will return the data from http or the database
 */
public class CollectionSpec {

  //    private final String url = Config.getCollectionJsonUrlStub();
  private final String url = "";

  public CollectionSpec() {
  }

  public String url() {
    return url;
  }

  /**
   * Retrofit 2 endpoint definition
   */
  public interface CollectionEndpoint {
    @GET("ftb-json/{urlStub}")
    Observable<Collection> load(@Path("urlStub") String urlStub);
  }
}
