/*
 * Copyright 2015 FeelTheBern.org
 */
package com.berniesanders.canvass.repositories.specs;

import com.berniesanders.canvass.config.Config;
import com.berniesanders.canvass.config.ConfigImpl;
import com.berniesanders.canvass.models.Collection;

import retrofit.http.GET;
import retrofit.http.Path;
import rx.Observable;

import javax.inject.Inject;

/**
 * Used to configure/filter a request to the data layer repository
 * which will return the data from http or the database
 */
public class CollectionSpec {

    @Inject
    Config config;
    private final String url;

    public CollectionSpec() {
        url = config.getCOLLECTION_JSON_URL_STUB();
    }

    public String url() {
        return url;
    }


    /**
     * Retrofit 2 endpoint definition
     */
    public interface CollectionEndpoint {
        @GET("ftb-json/{urlStub}")
        Observable<Collection> load(@Path("urlStub") String urlStub);
    }
}
