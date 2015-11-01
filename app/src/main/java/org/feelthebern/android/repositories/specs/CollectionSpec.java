/*
 * Copyright 2015 FeelTheBern.org
 */
package org.feelthebern.android.repositories.specs;

import org.feelthebern.android.config.UrlConfig;
import org.feelthebern.android.models.Collection;

import retrofit.http.GET;
import retrofit.http.Path;
import rx.Observable;

/**
 * Used to configure/filter a request to the data layer repository
 * which will return the data from http or the database
 */
public class CollectionSpec {

    private final String url = UrlConfig.COLLECTION_JSON_URL_STUB;

    public CollectionSpec() {
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
