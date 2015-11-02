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
    private boolean shouldRamain;
    private boolean shouldHideToolbar;

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

    public ChangePageEvent close(boolean shouldClose) {
        this.shouldClose = shouldClose;
        return this;
    }

    /**
     * keeps the app bar as it is...
     * @param shouldRamain
     */
    public ChangePageEvent remain(boolean shouldRamain) {
        this.shouldRamain = shouldRamain;
        return this;
    }

    public ChangePageEvent hideToolbar(boolean shouldHideToolbar) {
        this.shouldHideToolbar = shouldHideToolbar;
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

    public boolean shouldRamain() {
        return shouldRamain;
    }
    public boolean shouldHideToolbar() {
        return shouldHideToolbar;
    }

    @Override
    public String toString() {
        return "ChangePageEvent{" +
                "img='" + img + '\'' +
                ", bus=" + bus +
                ", title='" + title + '\'' +
                ", close=" + shouldClose +
                ", hideToolbar=" + shouldHideToolbar +
                '}';
    }
}
