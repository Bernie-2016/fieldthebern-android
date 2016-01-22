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
@Layout(R.layout.row_video)
public class Video extends Content implements Parcelable {

  private String src;
  private String id;

  @Override
  public String getText() {
    return src;
  }

  public String getId() {
    return id;
  }

  public String getSrc() {
    return src;
  }

  public void setId(String id) {
    this.id = id;
  }

  @Override
  public int describeContents() {
    return 0;
  }

  @Override
  public void writeToParcel(Parcel dest, int flags) {
    super.writeToParcel(dest, flags);
    dest.writeString(this.src);
    dest.writeString(this.id);
  }

  public Video() {
  }

  protected Video(Parcel in) {
    super(in);
    this.src = in.readString();
    this.id = in.readString();
  }

  public static final Parcelable.Creator<Video> CREATOR = new Parcelable.Creator<Video>() {
    public Video createFromParcel(Parcel source) {
      return new Video(source);
    }

    public Video[] newArray(int size) {
      return new Video[size];
    }
  };

  @Override
  public String toString() {
    return "Video{" +
        "src='" + src + '\'' +
        "id='" + id + '\'' +
        '}';
  }
}
