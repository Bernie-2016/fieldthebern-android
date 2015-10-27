package org.feelthebern.android.models;

import com.google.gson.annotations.SerializedName;

import org.feelthebern.android.R;
import org.feelthebern.android.annotations.Layout;

import java.util.List;

/**
 * Collection is an object with a field for an array of items.
 * items include "page" and other "collections"
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
@Layout(R.layout.item_collection)
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
