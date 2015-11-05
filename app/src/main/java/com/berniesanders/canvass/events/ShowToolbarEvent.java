package com.berniesanders.canvass.events;

import com.squareup.otto.Bus;

/**
 *
 */
public class ShowToolbarEvent {

    private Bus bus;
    private boolean shouldShowToolbar;

    public ShowToolbarEvent() {
    }

    public ShowToolbarEvent with(Bus bus) {
        this.bus = bus;
        return this;
    }

    public ShowToolbarEvent showToolbar(boolean shouldHideToolbar) {
        this.shouldShowToolbar = shouldHideToolbar;
        return this;
    }

    public void dispatch() {
        bus.post(this);
    }

    ///////////////////getters

    public boolean shouldShowToolbar() {
        return shouldShowToolbar;
    }

    @Override
    public String toString() {
        return "ChangePageEvent{" +
                ", bus=" + bus +
                ", shouldShowToolbar=" + shouldShowToolbar +
                '}';
    }
}
