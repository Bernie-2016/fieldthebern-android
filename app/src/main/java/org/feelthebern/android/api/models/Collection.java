package org.feelthebern.android.api.models;

import java.util.List;

/**
 * Created by AndrewOrobator on 8/29/15.
 */
public class Collection implements ApiItem {
    private String name;
    private String title;
    private String url;
    private List<ApiItem> mApiItems;

    public List<ApiItem> getApiItems() {
        return mApiItems;
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
