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

/**
 * Wrapper class for an array of mixed items of Person and ApiAddress
 * see Visit
 */
public class CanvassData implements Parcelable {

  protected String type;

  public String type() {
    return type;
  }

  public CanvassData type(final String type) {
    this.type = type;
    return this;
  }

  @Override public int describeContents() {
    return 0;
  }

  @Override public void writeToParcel(Parcel dest, int flags) {
    dest.writeString(this.type);
  }

  public CanvassData() {
  }

  protected CanvassData(Parcel in) {
    this.type = in.readString();
  }

  public static final Parcelable.Creator<CanvassData> CREATOR =
      new Parcelable.Creator<CanvassData>() {
        public CanvassData createFromParcel(Parcel source) {
          return new CanvassData(source);
        }

        public CanvassData[] newArray(int size) {
          return new CanvassData[size];
        }
      };
}
