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
import com.google.gson.annotations.SerializedName;
import java.util.ArrayList;
import java.util.List;

/**
 * https://github.com/Bernie-2016/fieldthebern-api/wiki/API-Visits
 */
public class Visit implements Parcelable {

  Data data = new Data();

  /**
   * Mix of ApiAddress and Person objects
   */
  List<CanvassData> included;

  public List<CanvassData> included() {
    if (included == null) {
      included = new ArrayList<>();
    }
    return this.included;
  }

  public Attributes attributes() {
    return data.attributes;
  }

  public Visit() {
  }

  public Visit attributes(final Attributes attributes) {
    data.attributes = attributes;
    return this;
  }

  public Visit included(final List<CanvassData> included) {
    this.included = included;
    return this;
  }

  public void start() {
    data.attributes.startTime = System.currentTimeMillis();
  }

  public void stop() {
    data.attributes.finishTime = System.currentTimeMillis();
    data.attributes.duration(
        (int) ((data.attributes.finishTime - data.attributes.startTime) / 1000));
  }

  public int describeContents() {
    return 0;
  }

  public void writeToParcel(Parcel out, int flags) {
    out.writeParcelable(data, 0);
    out.writeList(included);
  }

  public static final Parcelable.Creator<Visit> CREATOR
      = new Parcelable.Creator<Visit>() {
    public Visit createFromParcel(Parcel in) {
      return new Visit(in);
    }

    public Visit[] newArray(int size) {
      return new Visit[size];
    }
  };

  private Visit(Parcel in) {
    data = in.readParcelable(Data.class.getClassLoader());
    included = in.readArrayList(CanvassData.class.getClassLoader());
  }

  public static class Data implements Parcelable {
    Attributes attributes = new Attributes();

    public Data() {
    }

    public int describeContents() {
      return 0;
    }

    public void writeToParcel(Parcel out, int flags) {
      out.writeParcelable(attributes, 0);
    }

    public static final Parcelable.Creator<Data> CREATOR
        = new Parcelable.Creator<Data>() {
      public Data createFromParcel(Parcel in) {
        return new Data(in);
      }

      public Data[] newArray(int size) {
        return new Data[size];
      }
    };

    private Data(Parcel in) {
      attributes = in.readParcelable(Attributes.class.getClassLoader());
    }
  }

  public static class Attributes implements Parcelable {

    /**
     * Milliseconds since epoch
     */
    transient long startTime;

    /**
     * Milliseconds since epoch
     */
    transient long finishTime;

    /**
     * Visit duration in seconds
     */
    @SerializedName("duration_sec")
    int duration = 0;

    public int duration() {
      return this.duration;
    }

    public Attributes() {
    }

    public Attributes duration(final int duration) {
      this.duration = duration;
      return this;
    }

    public int describeContents() {
      return 0;
    }

    public void writeToParcel(Parcel out, int flags) {
      out.writeLong(startTime);
      out.writeLong(finishTime);
      out.writeInt(duration);
    }

    public static final Parcelable.Creator<Attributes> CREATOR
        = new Parcelable.Creator<Attributes>() {
      public Attributes createFromParcel(Parcel in) {
        return new Attributes(in);
      }

      public Attributes[] newArray(int size) {
        return new Attributes[size];
      }
    };

    private Attributes(Parcel in) {
      startTime = in.readLong();
      finishTime = in.readLong();
      duration = in.readInt();
    }
  }
}
