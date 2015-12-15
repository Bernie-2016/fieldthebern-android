package com.berniesanders.fieldthebern.models;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

/**
 *
 */
public class UsersFollowing implements Parcelable {

    java.util.List<FollowingUser> data;


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeList(this.data);
    }

    public UsersFollowing() {
    }

    protected UsersFollowing(Parcel in) {
        this.data = new ArrayList<FollowingUser>();
        in.readList(this.data, List.class.getClassLoader());
    }

    public static final Parcelable.Creator<UsersFollowing> CREATOR = new Parcelable.Creator<UsersFollowing>() {
        public UsersFollowing createFromParcel(Parcel source) {
            return new UsersFollowing(source);
        }

        public UsersFollowing[] newArray(int size) {
            return new UsersFollowing[size];
        }
    };
}
