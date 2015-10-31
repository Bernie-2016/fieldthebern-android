package org.feelthebern.android.models;

import android.os.Parcel;
import android.os.Parcelable;

/**
 *
 */
public class Link implements Parcelable {
    private String href;

    private int start; //zero based index of the character prior to starting the link
    private int end;    //zero based index of the character after to ending the link


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.href);
        dest.writeInt(this.start);
        dest.writeInt(this.end);
    }

    public Link() {
    }

    protected Link(Parcel in) {
        this.href = in.readString();
        this.start = in.readInt();
        this.end = in.readInt();
    }

    public static final Parcelable.Creator<Link> CREATOR = new Parcelable.Creator<Link>() {
        public Link createFromParcel(Parcel source) {
            return new Link(source);
        }

        public Link[] newArray(int size) {
            return new Link[size];
        }
    };
}


// The following should linkify the word "majority"
//text: "The majority of the U.S. prison population is male and..."
//href: "http://www.example.com/",
//start: 4,
//end: 12
