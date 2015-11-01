package org.feelthebern.android.models;

import android.os.Parcel;
import android.os.Parcelable;


/**
 */
public class ApiItem implements Parcelable {

    //protected String imageUrlThumb;
    protected String type;

    public String getImageUrlThumb() {
        return null;
    }

    public String getTitle() {
        return null;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.type);
    }

    public ApiItem() {
    }

    protected ApiItem(Parcel in) {
        this.type = in.readString();
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
