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

import com.google.gson.annotations.SerializedName;
import java.util.List;

/*
{
  "data":[
    {ApiAddress obj},
    {ApiAddress obj},
    {ApiAddress obj},
    {ApiAddress obj}
  ],
  "included":[
    {user}
  ]
}
 */
public class SingleAddressResponse {

  @SerializedName("data") List<ApiAddress> addresses;

  //I guess this is a list in case multiple users hav visited the house
  @SerializedName("included") List<CanvassData> included;

  public List<ApiAddress> addresses() {
    return this.addresses;
  }

  public List<CanvassData> included() {
    return this.included;
  }

  public SingleAddressResponse addresses(final List<ApiAddress> addresses) {
    this.addresses = addresses;
    return this;
  }

  public SingleAddressResponse included(final List<CanvassData> included) {
    this.included = included;
    return this;
  }
}
