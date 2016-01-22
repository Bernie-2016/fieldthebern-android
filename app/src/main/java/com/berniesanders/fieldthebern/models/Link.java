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
import com.berniesanders.fieldthebern.parsing.Linky;

/**
 * The following should linkify the word "majority"
 * text: "The majority of the U.S. prison population is male and..."
 * href: "http://www.example.com/",
 * start: 4,
 * end: 12
 *
 * @see Linky
 */
public class Link implements Parcelable {
  private String href;

  private int start; //zero based index of the character prior to starting the link
  private int end;    //zero based index of the character after to ending the link

  public void setHref(String href) {
    this.href = href;
  }

  public void setStart(int start) {
    this.start = start;
  }

  public void setEnd(int end) {
    this.end = end;
  }

  public String getHref() {
    return href;
  }

  public int getStart() {
    return start;
  }

  public int getEnd() {
    return end;
  }

  @Override public int describeContents() {
    return 0;
  }

  @Override public void writeToParcel(Parcel dest, int flags) {
    dest.writeString(this.href);
    dest.writeInt(this.start);
    dest.writeInt(this.end);
  }

  public Link() {
  }

  protected Link(Parcel in) {
    this.href = in.readString();
    this.start = in.readInt();
    this.end = in.readInt();
  }

  public static final Parcelable.Creator<Link> CREATOR = new Parcelable.Creator<Link>() {
    public Link createFromParcel(Parcel source) {
      return new Link(source);
    }

    public Link[] newArray(int size) {
      return new Link[size];
    }
  };
}



