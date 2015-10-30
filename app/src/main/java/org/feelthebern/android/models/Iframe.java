package org.feelthebern.android.models;

import android.os.Parcel;
import android.os.Parcelable;

import org.feelthebern.android.R;
import org.feelthebern.android.annotations.Layout;

/**
 *
 */
@Layout(R.layout.row_iframe)
public class Iframe extends Content implements Parcelable {

    private String src;

    @Override
    public String getText() {
        return src;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        super.writeToParcel(dest,flags);
        dest.writeString(this.src);
    }

    public Iframe() {
    }

    protected Iframe(Parcel in) {
        super(in);
        this.src = in.readString();
    }

    public static final Parcelable.Creator<Iframe> CREATOR = new Parcelable.Creator<Iframe>() {
        public Iframe createFromParcel(Parcel source) {
            return new Iframe(source);
        }

        public Iframe[] newArray(int size) {
            return new Iframe[size];
        }
    };
}
