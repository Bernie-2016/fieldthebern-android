package com.berniesanders.fieldthebern.models;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

/**
 *
 */
public class UserVisit implements Parcelable {

    java.util.List<Visit> data;


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeList(this.data);
    }

    public UserVisit() {
    }

    protected UserVisit(Parcel in) {
        this.data = new ArrayList<Visit>();
        in.readList(this.data, List.class.getClassLoader());
    }

    public static final Parcelable.Creator<UserVisit> CREATOR = new Parcelable.Creator<UserVisit>() {
        public UserVisit createFromParcel(Parcel source) {
            return new UserVisit(source);
        }

        public UserVisit[] newArray(int size) {
            return new UserVisit[size];
        }
    };
}
