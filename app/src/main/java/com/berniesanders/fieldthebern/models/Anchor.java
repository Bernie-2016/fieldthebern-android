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
 *
 */
public class Anchor implements Parcelable {
    private String name;
    private int point; //i THINK this is the zero-based index of the item it points to.... or something


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.name);
        dest.writeInt(this.point);
    }

    public Anchor() {
    }

    protected Anchor(Parcel in) {
        this.name = in.readString();
        this.point = in.readInt();
    }

    public static final Parcelable.Creator<Anchor> CREATOR = new Parcelable.Creator<Anchor>() {
        public Anchor createFromParcel(Parcel source) {
            return new Anchor(source);
        }

        public Anchor[] newArray(int size) {
            return new Anchor[size];
        }
    };
}
