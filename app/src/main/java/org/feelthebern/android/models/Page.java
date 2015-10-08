package org.feelthebern.android.models;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by AndrewOrobator on 9/4/15.
 */
public class Page implements ApiItem {
    private String title;
    private String url;
    private List<Content> contents;
    @SerializedName("image_url_thumb")
    private String imageUrlThumb;
    @SerializedName("image_url_full")
    private String imageUrlFull;

    public List<Content> getContents() {
        return contents;
    }

    public String getImageUrlFull() {
        return imageUrlFull;
    }

    public String getImageUrlThumb() {
        return imageUrlThumb;
    }

    public String getTitle() {
        return title;
    }

    public String getUrl() {
        return url;
    }
}
