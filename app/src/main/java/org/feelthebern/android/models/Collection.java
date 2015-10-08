package org.feelthebern.android.models;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by AndrewOrobator on 8/29/15.
 */
public class Collection implements ApiItem {
    private String name;
    private String title;
    private String url;

    @SerializedName("image_url_thumb")
    private String imageUrlThumb;

    @SerializedName("image_url_full")
    private String imageUrlFull;
    private List<ApiItem> mApiItems;

    public List<ApiItem> getApiItems() {
        return mApiItems;
    }

    public String getImageUrlFull() {
        return imageUrlFull;
    }

    public String getImageUrlThumb() {
        return imageUrlThumb;
    }

    public String getName() {
        return name;
    }

    public String getTitle() {
        return title;
    }

    public String getUrl() {
        return url;
    }

    public void setApiItems(List<ApiItem> apiItems) {
        if (mApiItems != null) {
            throw new IllegalStateException("Api Items have already been set");
        }

        mApiItems = apiItems;
    }

}
