package org.feelthebern.android.events;

import com.squareup.otto.Bus;

/**
 *
 */
public class ChangePageEvent {

    private String img;
    private Bus bus;
    private String title;
    private boolean shouldClose;

    public ChangePageEvent() {
    }

    public ChangePageEvent img(String url) {
        this.img = url;
        return this;
    }

    public ChangePageEvent with(Bus bus) {
        this.bus = bus;
        return this;
    }

    public ChangePageEvent title(String title) {
        this.title = title;
        return this;
    }

    public ChangePageEvent shouldClose(boolean shouldClose) {
        this.shouldClose = shouldClose;
        return this;
    }

    public void dispatch() {
        bus.post(this);
    }

    ///////////////////getters

    public String getImgUrl() {
        return img;
    }

    public String getTitle() {
        return title;
    }

    public boolean shouldClose() {
        return shouldClose;
    }


    @Override
    public String toString() {
        return "ChangePageEvent{" +
                "img='" + img + '\'' +
                ", bus=" + bus +
                ", title='" + title + '\'' +
                ", shouldClose=" + shouldClose +
                '}';
    }
}
