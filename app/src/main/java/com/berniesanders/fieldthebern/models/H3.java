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
import java.util.ArrayList;
import java.util.List;

/**
 *
 */
@Layout(R.layout.row_h3)
public class H3 extends Content implements Parcelable, Linkable {
  private List<Anchor> anchors;
  private List<Link> links;

  public List<Anchor> getAnchors() {
    return anchors;
  }

  public List<Link> getLinks() {
    return links;
  }

  @Override
  public int describeContents() {
    return 0;
  }

  @Override
  public void writeToParcel(Parcel dest, int flags) {
    super.writeToParcel(dest, flags);
    dest.writeList(this.anchors);
    dest.writeList(this.links);
  }

  public H3() {
  }

  protected H3(Parcel in) {
    super(in);
    this.anchors = new ArrayList<>();
    in.readList(this.anchors, List.class.getClassLoader());
    this.links = new ArrayList<>();
    in.readList(this.links, List.class.getClassLoader());
  }

  public static final Parcelable.Creator<H3> CREATOR = new Parcelable.Creator<H3>() {
    public H3 createFromParcel(Parcel source) {
      return new H3(source);
    }

    public H3[] newArray(int size) {
      return new H3[size];
    }
  };
}
