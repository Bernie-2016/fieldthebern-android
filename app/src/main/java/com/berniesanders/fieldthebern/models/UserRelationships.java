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
public class UserRelationships implements Parcelable {

    UserVisit visits;
    UserFollowers followers;
    UsersFollowing following;

    @Override
    public String toString() {
        return "UserRelationships{" +
                "visits=" + visits +
                ", followers=" + followers +
                ", following=" + following +
                '}';
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable(this.visits, flags);
        dest.writeParcelable(this.followers, flags);
        dest.writeParcelable(this.following, flags);
    }

    public UserRelationships() {
    }

    protected UserRelationships(Parcel in) {
        this.visits = in.readParcelable(UserVisit.class.getClassLoader());
        this.followers = in.readParcelable(UserFollowers.class.getClassLoader());
        this.following = in.readParcelable(UsersFollowing.class.getClassLoader());
    }

    public static final Parcelable.Creator<UserRelationships> CREATOR = new Parcelable.Creator<UserRelationships>() {
        public UserRelationships createFromParcel(Parcel source) {
            return new UserRelationships(source);
        }

        public UserRelationships[] newArray(int size) {
            return new UserRelationships[size];
        }
    };
}
