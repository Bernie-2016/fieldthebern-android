package com.berniesanders.canvass.models;

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
