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

/**
 * request format
 * {
 * latitude: <latitude>,
 * longitude: <longitude>,
 * street1: <street1>,
 * street2: <street2>,
 * city: <city>,
 * state: <state>,
 * zip: <zip>
 * }
 *
 *
 * Not all search parameters are required.
 * latitude and longitude are optional
 * At least one of the street_ parameters is required
 * If there is no zip_code, both city and state_code are required.
 * If there is a zip_code, neither city nor state_code are required.
 *
 * Responds with a single address matched and retrieved from the database.
 *
 * https://github.com/Bernie-2016/fieldthebern-api/wiki/API-Addresses
 */
public class RequestSingleAddress {

  Double latitude;
  Double longitude;
  @SerializedName("street_1")
  String street1;
  @SerializedName("street_2")
  String street2;
  String city;
  @SerializedName("state_code")
  String state;
  @SerializedName("zip_code")
  String zip;

  public RequestSingleAddress() {
  }

  public RequestSingleAddress(ApiAddress apiAddress) {

    ApiAddress.Attributes attributes = apiAddress.attributes();

    latitude = attributes.latitude();
    longitude = attributes.longitude();
    street1 = attributes.street1();
    street2 = attributes.street2();
    city = attributes.city();
    state = attributes.state();
    zip = attributes.zip();
  }

  public Double latitude() {
    return this.latitude;
  }

  public Double longitude() {
    return this.longitude;
  }

  public String street1() {
    return this.street1;
  }

  public String street2() {
    return this.street2;
  }

  public String city() {
    return this.city;
  }

  public String state() {
    return this.state;
  }

  public String zip() {
    return this.zip;
  }

  public RequestSingleAddress latitude(final double latitude) {
    this.latitude = latitude;
    return this;
  }

  public RequestSingleAddress longitude(final double longitude) {
    this.longitude = longitude;
    return this;
  }

  public RequestSingleAddress street1(final String street1) {
    this.street1 = street1;
    return this;
  }

  public RequestSingleAddress street2(final String street2) {
    this.street2 = street2;
    return this;
  }

  public RequestSingleAddress city(final String city) {
    this.city = city;
    return this;
  }

  public RequestSingleAddress state(final String state) {
    this.state = state;
    return this;
  }

  public RequestSingleAddress zip(final String zip) {
    this.zip = zip;
    return this;
  }

  @Override
  public String toString() {
    return "RequestSingleAddress{" +
        "latitude=" + latitude +
        ", longitude=" + longitude +
        ", street1='" + street1 + '\'' +
        ", street2='" + street2 + '\'' +
        ", city='" + city + '\'' +
        ", state='" + state + '\'' +
        ", zip='" + zip + '\'' +
        '}';
  }
}
