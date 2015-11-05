package com.berniesanders.canvass.models;

import android.os.Parcel;
import android.os.Parcelable;

import com.berniesanders.canvass.parsing.PageContentDeserializer;

/**
 * Content is a single item in a page's 'contents' array
 * content item types include "p" "h1" "h2" "h3" "nav"
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
 *                      +-content item (this object) h1
 *                      +-content item (this object) h2
 *                      +-content item (this object) p
 *
 * @see PageContentDeserializer
 */
public class Content implements Parcelable {
    protected String text;
    protected String type;

    public String getText() {
        return text;
    }

    public String getType() {
        return type;
    }

    public void setText(String text) {
        this.text = text;
    }

    public void setType(String type) {
        this.type = type;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.text);
        dest.writeString(this.type);
    }

    public Content() {
    }

    protected Content(Parcel in) {
        this.text = in.readString();
        this.type = in.readString();
    }

    public static final Parcelable.Creator<Content> CREATOR = new Parcelable.Creator<Content>() {
        public Content createFromParcel(Parcel source) {
            return new Content(source);
        }

        public Content[] newArray(int size) {
            return new Content[size];
        }
    };
}
