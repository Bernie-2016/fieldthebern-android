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

import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.NonNull;

/**
 *
 */
public class User implements Parcelable {

  public static final String PREF_NAME = "User.Pref";
  public static final String PREF_SEEN_APP_INTRO = "Use.SeenAppIntro";

  UserData data = new UserData();

  @NonNull public UserData getData() {
    return data;
  }

  public void setData(UserData data) {
    this.data = data;
  }

  @Override public String toString() {
    return "User Data{" +
        "id=" + data.id +
        ", type='" + data.type + '\'' +
        ", attributes=" + data.attributes +
        ", relationships=" + data.relationships +
        '}';
  }

  @Override public int describeContents() {
    return 0;
  }

  @Override public void writeToParcel(Parcel dest, int flags) {
    dest.writeParcelable(this.data, 0);
  }

  public User() {
  }

  protected User(Parcel in) {
    this.data = in.readParcelable(UserData.class.getClassLoader());
  }

  public static final Creator<User> CREATOR = new Creator<User>() {
    public User createFromParcel(Parcel source) {
      return new User(source);
    }

    public User[] newArray(int size) {
      return new User[size];
    }
  };
}
