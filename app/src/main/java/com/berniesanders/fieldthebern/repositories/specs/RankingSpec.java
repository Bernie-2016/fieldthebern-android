/*
 * Copyright 2015 FeelTheBern.org
 */
package com.berniesanders.fieldthebern.repositories.specs;

import android.support.annotation.StringDef;

import com.berniesanders.fieldthebern.models.Rankings;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import retrofit.http.GET;
import retrofit.http.Query;
import rx.Observable;

/**
 * Used to configure/filter a request to the data layer repository
 */
public class RankingSpec {

    public static final String EVERYONE = "everyone";
    public static final String STATE = "state";
    public static final String FRIENDS = "friends";

    @RankType
    private final String type;

    /**
     * type: EVERYONE | STATE | FRIENDS
     */
    public RankingSpec(@RankType String type) {
        this.type = type;
    }

    @RankType
    public String type() {
        return type;
    }


    @StringDef({EVERYONE, STATE, FRIENDS})
    @Retention(RetentionPolicy.SOURCE)
    public @interface RankType {
    }

    /**
     * Retrofit 2 endpoint definition
     */
    public interface RankEndpoint {

        @GET("rankings")
        Observable<Rankings> get(@Query("type") @RankType String type);
    }
}
