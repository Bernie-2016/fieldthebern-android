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
import com.berniesanders.fieldthebern.R;
import com.berniesanders.fieldthebern.annotations.Layout;

/**
 *
 */
@Layout(R.layout.row_iframe) public class Iframe extends Content implements Parcelable {

  private String src;

  @Override public String getText() {
    return src;
  }

  @Override public int describeContents() {
    return 0;
  }

  @Override public void writeToParcel(Parcel dest, int flags) {
    super.writeToParcel(dest, flags);
    dest.writeString(this.src);
  }

  public Iframe() {
  }

  protected Iframe(Parcel in) {
    super(in);
    this.src = in.readString();
  }

  public static final Parcelable.Creator<Iframe> CREATOR = new Parcelable.Creator<Iframe>() {
    public Iframe createFromParcel(Parcel source) {
      return new Iframe(source);
    }

    public Iframe[] newArray(int size) {
      return new Iframe[size];
    }
  };
}
