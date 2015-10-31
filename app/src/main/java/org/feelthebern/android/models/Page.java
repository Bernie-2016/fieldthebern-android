package org.feelthebern.android.models;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import org.feelthebern.android.R;
import org.feelthebern.android.annotations.Layout;

import java.util.ArrayList;
import java.util.List;

/**
 * Collection is an object with a field 'content' for an array of items.
 * 'content' items include "p" "h1" "h2" "h3" "nav"
 *
 * The full site JSON structure is something like:
 *
 * collection {obj}
 *  +-items [array]
 *      +-page
 *      +-collection
 *          +-items [array]
 *              +-page
 *              +-page
 *      +-page
 *      +-collection
 *          +-items [array]
 *              +-page
 *              +-page {obj}
 *                  +-content [array]
 */
@Layout(R.layout.item_page)
public class Page extends ApiItem implements Parcelable {
    public static final String PAGE_PARCEL = "PageParcelKey";

    private int data;
    private String title;
    private String url;
    private List<Content> content;
    @SerializedName("image_url_thumb")
    private String imageUrlThumb;
    @SerializedName("image_url_full")
    private String imageUrlFull;

    public List<Content> getContent() {
        return content;
    }

    public String getImageUrlFull() {

        if (imageUrlFull.startsWith("http://feelthebern.org20")){
            imageUrlFull = imageUrlFull
                    .replace("http://feelthebern.org20",
                    "http://feelthebern.org/wp-content/uploads/20");
        }
        return imageUrlFull;
    }

    @Override
    public String getImageUrlThumb() {
        return imageUrlThumb;
    }

    @Override
    public String getTitle() {
        return title;
    }

    public String getUrl() {
        return url;
    }

    public int getData() {
        return data;
    }

    public void setData(int data) {
        this.data = data;
    }

    public void setContent(List<Content> contentList) {
        this.content = contentList;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        super.writeToParcel(dest, flags);
        dest.writeInt(this.data);
        dest.writeString(this.title);
        dest.writeString(this.url);
        dest.writeString(this.imageUrlThumb);
        dest.writeString(this.imageUrlFull);
        dest.writeTypedList(this.content);
    }

    public Page() {
    }

    protected Page(Parcel in) {
        super(in);
        this.data = in.readInt();
        this.title = in.readString();
        this.url = in.readString();
        this.imageUrlThumb = in.readString();
        this.imageUrlFull = in.readString();
        this.content = new ArrayList<Content>();
        in.readTypedList(this.content, Content.CREATOR);
    }

    public static final Parcelable.Creator<Page> CREATOR = new Parcelable.Creator<Page>() {
        public Page createFromParcel(Parcel source) {
            return new Page(source);
        }

        public Page[] newArray(int size) {
            return new Page[size];
        }
    };

    @Override
    public String toString() {
        return "Page{" +
                "data id=" + data +
                ", title='" + title + '\'' +
                ", url='" + url + '\'' +
                '}';
    }


}
