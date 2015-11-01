package org.feelthebern.android.models;

import android.os.Parcel;
import android.os.Parcelable;

import org.feelthebern.android.R;
import org.feelthebern.android.annotations.Layout;

/**
 *
 */
@Layout(R.layout.row_list)
public class List extends Content implements Parcelable {

    private java.util.List<String> list;

    @Override
    public String getText() {
        return list.get(0);//TODO?
    }

    public java.util.List<String> getList() {
        return list;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        super.writeToParcel(dest,flags);
        dest.writeStringList(this.list);
    }

    public List() {
    }

    protected List(Parcel in) {
        super(in);
        this.list = in.createStringArrayList();
    }

    public static final Parcelable.Creator<List> CREATOR = new Parcelable.Creator<List>() {
        public List createFromParcel(Parcel source) {
            return new List(source);
        }

        public List[] newArray(int size) {
            return new List[size];
        }
    };
}
