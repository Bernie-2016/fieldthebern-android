package org.feelthebern.android.models;

import com.google.gson.annotations.SerializedName;

import org.feelthebern.android.R;
import org.feelthebern.android.annotations.Layout;

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
public class Page implements ApiItem {
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
