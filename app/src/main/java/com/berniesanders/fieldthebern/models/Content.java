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
import com.berniesanders.fieldthebern.parsing.PageContentDeserializer;

/**
 * Content is a single item in a page's 'contents' array
 * content item types include "p" "h1" "h2" "h3" "nav"
 *
 * The full site JSON structure is something like:
 *
 * collection {obj}
 * +-items [array]
 * +-page
 * +-collection
 * +-items [array]
 * +-page
 * +-page
 * +-page
 * +-collection
 * +-items [array]
 * +-page
 * +-page {obj}
 * +-content [array]
 * +-content item (this object) h1
 * +-content item (this object) h2
 * +-content item (this object) p
 *
 * @see PageContentDeserializer
 */
public class Content implements Parcelable {
  protected String text;
  protected String type;

  public String getText() {
    return text;
  }

  public String getType() {
    return type;
  }

  public void setText(String text) {
    this.text = text;
  }

  public void setType(String type) {
    this.type = type;
  }

  @Override
  public int describeContents() {
    return 0;
  }

  @Override
  public void writeToParcel(Parcel dest, int flags) {
    dest.writeString(this.text);
    dest.writeString(this.type);
  }

  public Content() {
  }

  protected Content(Parcel in) {
    this.text = in.readString();
    this.type = in.readString();
  }

  public static final Parcelable.Creator<Content> CREATOR = new Parcelable.Creator<Content>() {
    public Content createFromParcel(Parcel source) {
      return new Content(source);
    }

    public Content[] newArray(int size) {
      return new Content[size];
    }
  };
}
