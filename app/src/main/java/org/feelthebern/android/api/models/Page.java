package org.feelthebern.android.api.models;

import java.util.List;

/**
 * Created by AndrewOrobator on 9/4/15.
 */
public class Page implements ApiItem {
    private String title;
    private String url;
    private List<Content> contents;

    public List<Content> getContents() {
        return contents;
    }

    public String getTitle() {
        return title;
    }

    public String getUrl() {
        return url;
    }
}
