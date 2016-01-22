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
 * text: "http://feelthebern.org/wp-content/uploads/2015/07/US-rates-over-time.jpg",
 * type: "img",
 * width: 445,
 * height: 512,
 * caption: "Source",
 * source: "http://www.washingtonpost.com/news/wonkblog/wp/2014/04/30/the-meteoric-costly-and-unprecedented-rise-of-incarceration-in-america/"
 */
@Layout(R.layout.row_img) public class Img extends Content implements Parcelable {

  public static final String IMG_PARCEL_KEY = "img_parcel_key";

  //text here is the url of the image
  //private String text;

  private int width;
  private int height;
  private String caption;
  private String source;  //attribution source

    /*
    text: "http://feelthebern.org/wp-content/uploads/2015/07/US-rates-over-time.jpg",
    type: "img",
    width: 445,
    height: 512,
    caption: "Source",
    source: "http://www.washingtonpost.com/news/wonkblog/wp/2014/04/30/the-meteoric-costly-and-unprecedented-rise-of-incarceration-in-america/"
    */

  public int getWidth() {
    return width;
  }

  public int getHeight() {
    return height;
  }

  public String getCaption() {
    return caption;
  }

  public String getSource() {
    return source;
  }

  @Override public int describeContents() {
    return 0;
  }

  @Override public void writeToParcel(Parcel dest, int flags) {
    super.writeToParcel(dest, flags);
    dest.writeInt(this.width);
    dest.writeInt(this.height);
    dest.writeString(this.caption);
    dest.writeString(this.source);
  }

  public Img() {
  }

  protected Img(Parcel in) {
    super(in);
    this.width = in.readInt();
    this.height = in.readInt();
    this.caption = in.readString();
    this.source = in.readString();
  }

  public static final Parcelable.Creator<Img> CREATOR = new Parcelable.Creator<Img>() {
    public Img createFromParcel(Parcel source) {
      return new Img(source);
    }

    public Img[] newArray(int size) {
      return new Img[size];
    }
  };
}
