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
@Layout(R.layout.row_list)
public class List extends Content implements Parcelable {

  private java.util.List<String> list;

  @Override
  public String getText() {
    return list.get(0);//TODO?
  }

  public java.util.List<String> getList() {
    return list;
  }

  @Override
  public int describeContents() {
    return 0;
  }

  @Override
  public void writeToParcel(Parcel dest, int flags) {
    super.writeToParcel(dest, flags);
    dest.writeStringList(this.list);
  }

  public List() {
  }

  protected List(Parcel in) {
    super(in);
    this.list = in.createStringArrayList();
  }

  public static final Parcelable.Creator<List> CREATOR = new Parcelable.Creator<List>() {
    public List createFromParcel(Parcel source) {
      return new List(source);
    }

    public List[] newArray(int size) {
      return new List[size];
    }
  };
}
