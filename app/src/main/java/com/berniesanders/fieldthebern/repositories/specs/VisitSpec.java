package com.berniesanders.fieldthebern.repositories.specs;

import com.berniesanders.fieldthebern.models.Visit;
import com.berniesanders.fieldthebern.models.VisitResult;

import retrofit.http.Body;
import retrofit.http.POST;
import rx.Observable;

/*
 * Made with love by volunteers
 * Copyright 2015 BernieSanders.com, FeelTheBern.org, Coderly,
 * LostPacketSoftware and the volunteers
 * License: GNU AGPLv3 - https://gnu.org/licenses/agpl.html 
 */
public class VisitSpec {

    /**
     * Retrofit 2 endpoint definition
     */
    public interface VisitEndpoint {

        @POST("visits")
        Observable<VisitResult> submit(@Body Visit visit);
    }
}
