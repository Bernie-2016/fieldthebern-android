package com.berniesanders.canvass.config;

public interface Config {
    String getBaseUrl();

    String getCanvassUrl();

    String getClientId();

    String getClientSecret();

    String getCollectionJsonUrlStub();

    String getPageJsonUrlStub();
}
