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
 */
public class ApiItem implements Parcelable {

    //protected String imageUrlThumb;
    protected String type;
    protected String title;

    public String getImageUrlThumb() {
        return null;
    }

    public String getTitle() {
        return title;
    }

    public String getType() {
        return type;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.type);
        dest.writeString(this.title);
    }

    public ApiItem() {
    }

    protected ApiItem(Parcel in) {
        this.type = in.readString();
        this.title = in.readString();
    }

    public static final Parcelable.Creator<ApiItem> CREATOR = new Parcelable.Creator<ApiItem>() {
        public ApiItem createFromParcel(Parcel source) {
            return new ApiItem(source);
        }

        public ApiItem[] newArray(int size) {
            return new ApiItem[size];
        }
    };
}
