/*
 * Made with love by volunteers
 * Copyright 2015 FeelTheBern.org, BernieSanders.com,
 * Coderly, LostPacketSoftware and the volunteers that wrote this code
 * License: GNU AGPLv3 - https://gnu.org/licenses/agpl.html
 */
package com.berniesanders.fieldthebern.repositories;

import com.berniesanders.fieldthebern.config.Config;
import com.berniesanders.fieldthebern.models.ApiAddress;
import com.berniesanders.fieldthebern.models.CanvassData;
import com.berniesanders.fieldthebern.models.Person;
import com.berniesanders.fieldthebern.models.Visit;
import com.berniesanders.fieldthebern.models.VisitResult;
import com.berniesanders.fieldthebern.repositories.auth.ApiAuthenticator;
import com.berniesanders.fieldthebern.repositories.interceptors.AddTokenInterceptor;
import com.berniesanders.fieldthebern.repositories.interceptors.UserAgentInterceptor;
import com.berniesanders.fieldthebern.repositories.specs.VisitSpec;
import com.f2prateek.rx.preferences.RxSharedPreferences;
import com.google.gson.Gson;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.logging.HttpLoggingInterceptor;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

import retrofit.GsonConverterFactory;
import retrofit.Retrofit;
import retrofit.RxJavaCallAdapterFactory;
import rx.Observable;
import timber.log.Timber;

/**
 * For starting a visit, updating it, then submitting to the API
 */
@Singleton
public class VisitRepo {

    final Gson gson;
    private final TokenRepo tokenRepo;
    private final RxSharedPreferences rxPrefs;
    private final OkHttpClient client = new OkHttpClient();
    private final Config config;

    private Visit visit;


    @Inject
    public VisitRepo(Gson gson, TokenRepo tokenRepo, RxSharedPreferences rxPrefs, Config config) {
        this.gson = gson;
        this.tokenRepo = tokenRepo;
        this.rxPrefs = rxPrefs;
        this.config = config;

        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

        client.interceptors().add(new UserAgentInterceptor(config.getUserAgent()));
        client.interceptors().add(new AddTokenInterceptor(tokenRepo));
        client.interceptors().add(interceptor);
        client.setAuthenticator(new ApiAuthenticator(tokenRepo));
    }


    public boolean inProgress() {
        return visit != null;
    }

    public Visit get() {
        return visit;
    }

    public Visit start(final ApiAddress apiAddress) {

        visit = new Visit();
        visit.start();
        visit.included().add(apiAddress);
        List<CanvassData> included = apiAddress.included();

        for(CanvassData canvassData : included) {
            if (canvassData.type().equals(Person.TYPE)) {
                addPerson((Person) canvassData);
            }
        }

        return visit;
    }

    public void addPerson(Person person) {
        if (!visit.included().contains(person)) {
            visit.included().add(person);
        }
    }

    /**
     *
     */
    public Observable<VisitResult> submit() {
        Timber.v("submit()");

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(config.getCanvassUrl())
                .addConverterFactory(GsonConverterFactory.create(gson))
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .client(client)
                .build();

        visit.stop(); //stop the timer



        VisitSpec.VisitEndpoint endpoint = retrofit.create(VisitSpec.VisitEndpoint.class);
        return endpoint.submit(visit);
    }

    public void clear() {
        visit = null;
    }
}
