/*
 * Copyright (c) 2016 - Bernie 2016, Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */

package com.berniesanders.fieldthebern.models;

import android.os.Parcel;
import android.os.Parcelable;

import com.berniesanders.fieldthebern.R;
import com.berniesanders.fieldthebern.annotations.Layout;
import com.google.gson.annotations.SerializedName;

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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Page)) return false;

        Page page = (Page) o;

        if (data != page.data) return false;
        if (title != null ? !title.equals(page.title) : page.title != null) return false;
        if (url != null ? !url.equals(page.url) : page.url != null) return false;
        if (imageUrlThumb != null ? !imageUrlThumb.equals(page.imageUrlThumb) : page.imageUrlThumb != null) {
            return false;
        }
        return !(imageUrlFull != null ? !imageUrlFull.equals(page.imageUrlFull) : page.imageUrlFull != null);

    }

    @Override
    public int hashCode() {
        int result = data;
        result = 31 * result + (title != null ? title.hashCode() : 0);
        result = 31 * result + (url != null ? url.hashCode() : 0);
        result = 31 * result + (imageUrlThumb != null ? imageUrlThumb.hashCode() : 0);
        result = 31 * result + (imageUrlFull != null ? imageUrlFull.hashCode() : 0);
        return result;
    }
}
