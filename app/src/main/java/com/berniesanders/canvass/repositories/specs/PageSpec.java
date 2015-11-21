/*
 * Copyright 2015 FeelTheBern.org
 */
package com.berniesanders.canvass.repositories.specs;

import com.berniesanders.canvass.config.Config;
import com.berniesanders.canvass.models.Content;
import com.berniesanders.canvass.models.Page;

import java.util.List;

import retrofit.http.GET;
import retrofit.http.Path;
import retrofit.http.Query;
import rx.Observable;

import javax.inject.Inject;

/**
 * Used to configure/filter a page request to the data layer repository
 * which will return the data from http or the database
 */
public class PageSpec {

    private final String id;
    private final String url;
    private final Page page;
    private final Config config;

    public PageSpec(Page page, Config config) {
        this.page = page;
        this.id = String.valueOf(page.getData());
        url = config.getPageJsonUrlStub();
        this.config = config;
    }

    public String id() {
        return id;
    }

    public String url() {
        return url;
    }

    public Page page() {
        return page;
    }


    @Override
    public String toString() {
        return "PageSpec{" +
                "id='" + id + '\'' +
                ", url='" + url + '\'' +
                ", page title=" + page.getTitle() +
                '}';
    }

    /**
     * Retrofit 2 endpoint definition
     */
    public interface PageEndpoint {
        @GET("ftb-json/{urlStub}")
        Observable<List<Content>> load(@Path("urlStub") String urlStub,
                              @Query("id") String id);
    }
}
