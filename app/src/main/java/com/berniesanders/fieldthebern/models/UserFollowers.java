package com.berniesanders.fieldthebern.models;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

/**
 *
 */
public class UserFollowers implements Parcelable {

    java.util.List<FollowedUser> data;


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeList(this.data);
    }

    public UserFollowers() {
    }

    protected UserFollowers(Parcel in) {
        this.data = new ArrayList<FollowedUser>();
        in.readList(this.data, List.class.getClassLoader());
    }

    public static final Parcelable.Creator<UserFollowers> CREATOR = new Parcelable.Creator<UserFollowers>() {
        public UserFollowers createFromParcel(Parcel source) {
            return new UserFollowers(source);
        }

        public UserFollowers[] newArray(int size) {
            return new UserFollowers[size];
        }
    };
}
