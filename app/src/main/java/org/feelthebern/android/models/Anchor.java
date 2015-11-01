package org.feelthebern.android.models;

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
