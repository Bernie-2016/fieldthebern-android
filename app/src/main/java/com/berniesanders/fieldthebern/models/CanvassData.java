package com.berniesanders.fieldthebern.models;

/*
 * Made with love by volunteers
 * Copyright 2015 BernieSanders.com, FeelTheBern.org, Coderly, Lost Packet Software and the volunteers
 * License: GNU AGPLv3 - https://gnu.org/licenses/agpl.html 
 */

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Wrapper class for an array of mixed items of Person and ApiAddress
 * see Visit
 */
public class CanvassData implements Parcelable {

    protected String type;

    public String type() {
        return type;
    }

    public CanvassData type(final String type){
        this.type = type;
        return this;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.type);
    }

    public CanvassData() {
    }

    protected CanvassData(Parcel in) {
        this.type = in.readString();
    }

    public static final Parcelable.Creator<CanvassData> CREATOR = new Parcelable.Creator<CanvassData>() {
        public CanvassData createFromParcel(Parcel source) {
            return new CanvassData(source);
        }

        public CanvassData[] newArray(int size) {
            return new CanvassData[size];
        }
    };
}
