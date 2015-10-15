/*
 * Copyright 2015 FeelTheBern.org
 */
package org.feelthebern.android.repositories.specs;

/**
 * Used to configure/filter a request to the data layer repository
 * which will return the data from http or the database
 */
public class HomeIssueSpec {

    private final String url;

    public HomeIssueSpec(String url) {

        this.url = url;
    }

    public String url() {
        return url;
    }
}
