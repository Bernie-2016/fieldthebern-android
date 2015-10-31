package org.feelthebern.android.models;

import android.os.Parcel;
import android.os.Parcelable;

import org.feelthebern.android.R;
import org.feelthebern.android.annotations.Layout;

/**
 *
 */
@Layout(R.layout.row_video)
public class Video extends Content implements Parcelable {

    private String src;
    private String id;

    @Override
    public String getText() {
        return src;
    }

    public String getId() {
        return id;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        super.writeToParcel(dest,flags);
        dest.writeString(this.src);
        dest.writeString(this.id);
    }

    public Video() {
    }

    protected Video(Parcel in) {
        super(in);
        this.src = in.readString();
        this.id = in.readString();
    }

    public static final Parcelable.Creator<Video> CREATOR = new Parcelable.Creator<Video>() {
        public Video createFromParcel(Parcel source) {
            return new Video(source);
        }

        public Video[] newArray(int size) {
            return new Video[size];
        }
    };


    @Override
    public String toString() {
        return "Video{" +
                "src='" + src + '\'' +
                "id='" + id + '\'' +
                '}';
    }
}
