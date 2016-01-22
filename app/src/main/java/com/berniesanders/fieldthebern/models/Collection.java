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
import com.google.gson.annotations.SerializedName;
import java.util.ArrayList;
import java.util.List;

/**
 * Collection is an object with a field for an array of items.
 * items include "page" and other "collections"
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
 */
@Layout(R.layout.item_collection)
public class Collection extends ApiItem implements Parcelable {
  public static final String COLLECTION_PARCEL = "CollectionParcelKey";

  private String url;

  @SerializedName("image_url_thumb")
  private String imageUrlThumb;

  @SerializedName("image_url_full")
  private String imageUrlFull;
  private List<ApiItem> items;

  public List<ApiItem> getApiItems() {
    return items;
  }

  public String getImageUrlFull() {
    return imageUrlFull;
  }

  @Override
  public String getImageUrlThumb() {
    return imageUrlThumb;
  }

  public String getUrl() {
    return url;
  }

  @Override
  public int describeContents() {
    return 0;
  }

  @Override
  public void writeToParcel(Parcel dest, int flags) {
    super.writeToParcel(dest, flags);
    dest.writeString(this.url);
    dest.writeString(this.imageUrlThumb);
    dest.writeString(this.imageUrlFull);
    dest.writeTypedList(this.items);
  }

  public Collection() {
  }

  protected Collection(Parcel in) {
    super(in);
    this.url = in.readString();
    this.imageUrlThumb = in.readString();
    this.imageUrlFull = in.readString();
    this.items = new ArrayList<>();
    in.readTypedList(items, ApiItem.CREATOR);
  }

  public static final Parcelable.Creator<Collection> CREATOR =
      new Parcelable.Creator<Collection>() {
        public Collection createFromParcel(Parcel source) {
          return new Collection(source);
        }

        public Collection[] newArray(int size) {
          return new Collection[size];
        }
      };

  @Override
  public String toString() {
    //simple toString so we don't return the whole damn collection
    return "Collection{" +
        "title='" + title + '\'' +
        '}';
  }
}
