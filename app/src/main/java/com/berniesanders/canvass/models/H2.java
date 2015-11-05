package com.berniesanders.canvass.models;

import android.os.Parcel;
import android.os.Parcelable;

import com.berniesanders.canvass.R;
import com.berniesanders.canvass.annotations.Layout;

import java.util.ArrayList;
import java.util.List;

/**
 *
 */
@Layout(R.layout.row_h2)
public class H2 extends Content implements Parcelable, Linkable {
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
        dest.writeList(this.links);
    }

    public H2() {
    }

    protected H2(Parcel in) {
        super(in);
        this.anchors = new ArrayList<Anchor>();
        in.readList(this.anchors, List.class.getClassLoader());
        this.links = new ArrayList<Link>();
        in.readList(this.links, List.class.getClassLoader());
    }

    public static final Parcelable.Creator<H2> CREATOR = new Parcelable.Creator<H2>() {
        public H2 createFromParcel(Parcel source) {
            return new H2(source);
        }

        public H2[] newArray(int size) {
            return new H2[size];
        }
    };
}
