package com.berniesanders.canvass.config;

/**
 * Central place for configuring data access urls
 */
public class ConfigImpl implements Config {

    //includes json for the whole site, not used currently
    //public static final String FULL_JSON_URL = "http://feelthebern.org/ftb-json/";

    private final String BASE_URL;
    private final String CANVASS_URL;

    private final String CLIENT_ID;
    private final String CLIENT_SECRET;

    //public static final String COLLECTION_JSON_URL_STUB = "index2.php";
//    private final String COLLECTION_JSON_URL_STUB;
    private final String PAGE_JSON_URL_STUB;

    public ConfigImpl() {
        BASE_URL = "http://feelthebern.org/";
        CANVASS_URL = "http://api.groundgameapp-staging.com/";
        CLIENT_ID = "cbe732e440995e8ae73dfb093de4b880f3a68346e4b1bf933e626599a090bdec";
        CLIENT_SECRET = "30d732185171be840adff6e11aae9157614bb7be2da2d27100bcb7bc938ac369";
//        COLLECTION_JSON_URL_STUB = "";
        PAGE_JSON_URL_STUB = "page.php";
    }

    @Override
    public String getFeelTheBernUrl() {
        return BASE_URL;
    }

    @Override
    public String getCanvassUrl() {
        return CANVASS_URL;
    }

    @Override
    public String getClientId() {
        return CLIENT_ID;
    }

    @Override
    public String getClientSecret() {
        return CLIENT_SECRET;
    }

//    @Override
//    public String getCollectionJsonUrlStub() {
//        return COLLECTION_JSON_URL_STUB;
//    }

    @Override
    public String getPageJsonUrlStub() {
        return PAGE_JSON_URL_STUB;
    }
}
