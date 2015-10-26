package org.feelthebern.android.events;

/**
 *
 */
public class ChangeBackgroundEvent {

    private final String url;

    public ChangeBackgroundEvent(String url) {
        this.url = url;
    }

    public String getUrl() {
        return url;
    }
}
