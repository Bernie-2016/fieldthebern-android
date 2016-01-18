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
import android.support.annotation.NonNull;

/**
 *
 */
public class UserData extends CanvassData implements Parcelable {

    public static final String TYPE = "users";

    Integer id;
    UserAttributes attributes = new UserAttributes();
    UserRelationships relationships;

    @Override
    public String type() {
        return TYPE;
    }

    @Override
    public CanvassData type(String type) {
        this.type = type;
        return this;
    }

    public Integer id() {
        return this.id;
    }

    @NonNull
    public UserAttributes attributes() {
        return attributes;
    }

    public UserRelationships relationships() {
        return this.relationships;
    }

    public UserData id(final Integer id) {
        this.id = id;
        return this;
    }

    public UserData attributes(final UserAttributes attributes) {
        this.attributes = attributes;
        return this;
    }

    public UserData relationships(final UserRelationships relationships) {
        this.relationships = relationships;
        return this;
    }

    @Override
    public String toString() {
        return "UserData{" +
                "type='" + type + '\'' +
                ", id=" + id +
                ", attributes=" + attributes +
                ", relationships=" + relationships +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        UserData userData = (UserData) o;

        if (type != null ? !type.equals(userData.type) : userData.type != null) return false;
        if (id != null ? !id.equals(userData.id) : userData.id != null) return false;
        if (attributes != null ? !attributes.equals(userData.attributes) : userData.attributes != null) {
            return false;
        }
        return !(relationships != null ? !relationships.equals(userData.relationships) : userData.relationships != null);

    }

    @Override
    public int hashCode() {
        int result = type != null ? type.hashCode() : 0;
        result = 31 * result + (id != null ? id.hashCode() : 0);
        result = 31 * result + (attributes != null ? attributes.hashCode() : 0);
        result = 31 * result + (relationships != null ? relationships.hashCode() : 0);
        return result;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        super.writeToParcel(dest, flags);
        dest.writeString(this.type);
        dest.writeValue(this.id);
        dest.writeParcelable(this.attributes, 0);
        dest.writeParcelable(this.relationships, 0);
    }

    public UserData() {
    }

    protected UserData(Parcel in) {
        super(in);
        this.type = in.readString();
        this.id = (Integer) in.readValue(Integer.class.getClassLoader());
        this.attributes = in.readParcelable(UserAttributes.class.getClassLoader());
        this.relationships = in.readParcelable(UserRelationships.class.getClassLoader());
    }

    public static final Creator<UserData> CREATOR = new Creator<UserData>() {
        public UserData createFromParcel(Parcel source) {
            return new UserData(source);
        }

        public UserData[] newArray(int size) {
            return new UserData[size];
        }
    };
}
