package com.berniesanders.canvass.models;

import android.os.Parcel;
import android.os.Parcelable;

import com.berniesanders.canvass.annotations.Layout;

import java.util.ArrayList;
import java.util.List;

/**
 *
 */
@Layout(com.berniesanders.canvass.R.layout.row_p)
public class P extends Content implements Parcelable, Linkable {
    private List<Anchor> anchors;
    private List<Link> links;

    public List<Anchor> getAnchors() {
        return anchors;
    }

    public List<Link> getLinks() {
        return links;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        super.writeToParcel(dest,flags);
        dest.writeList(this.anchors);
        dest.writeTypedList(links);
    }

    public P() {
    }

    protected P(Parcel in) {
        super(in);
        this.anchors = new ArrayList<Anchor>();
        in.readList(this.anchors, List.class.getClassLoader());
        this.links = in.createTypedArrayList(Link.CREATOR);
    }

    public static final Parcelable.Creator<P> CREATOR = new Parcelable.Creator<P>() {
        public P createFromParcel(Parcel source) {
            return new P(source);
        }

        public P[] newArray(int size) {
            return new P[size];
        }
    };
}
