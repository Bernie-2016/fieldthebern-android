/*
 * Made with love by volunteers
 * Copyright 2015 FeelTheBern.org, BernieSanders.com, Coderly, LostPacketSoftware
 * and the volunteers that wrote this code
 * License: GNU AGPLv3 - https://gnu.org/licenses/agpl.html
 */
package com.berniesanders.fieldthebern.repositories;

import com.berniesanders.fieldthebern.config.Config;
import com.berniesanders.fieldthebern.models.ApiAddress;
import com.berniesanders.fieldthebern.repositories.auth.ApiAuthenticator;
import com.berniesanders.fieldthebern.repositories.interceptors.AddTokenInterceptor;
import com.berniesanders.fieldthebern.repositories.interceptors.UserAgentInterceptor;
import com.berniesanders.fieldthebern.repositories.specs.AddressSpec;
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
 * For loading address canvass data from the API
 */
@Singleton
public class AddressRepo {

    final Gson gson;
    private final TokenRepo tokenRepo;
    private final RxSharedPreferences rxPrefs;
    private final OkHttpClient client = new OkHttpClient();
    private final Config config;


    @Inject
    public AddressRepo(Gson gson, TokenRepo tokenRepo, RxSharedPreferences rxPrefs, Config config) {
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



    /**
     *
     */
    public Observable<List<ApiAddress>> getMultiple(final AddressSpec spec) {
        Timber.v("getMultiple()");

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(config.getCanvassUrl())
                .addConverterFactory(GsonConverterFactory.create(gson))
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .client(client)
                .build();

        AddressSpec.AddressEndpoint endpoint = retrofit.create(AddressSpec.AddressEndpoint.class);

        return endpoint.getMultiple(
                spec.multipleAddresses().latitude(),
                spec.multipleAddresses().longitude(),
                spec.multipleAddresses().radius()
                );
    }

    /**
     *
     */
    public Observable<ApiAddress> getSingle(final AddressSpec spec) {
        Timber.v("getSingle()");

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(config.getCanvassUrl())
                .addConverterFactory(GsonConverterFactory.create(gson))
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .client(client)
                .build();

        AddressSpec.AddressEndpoint endpoint = retrofit.create(AddressSpec.AddressEndpoint.class);

        return endpoint.getSingle(
                spec.singleAddress().latitude(),
                spec.singleAddress().longitude(),
                spec.singleAddress().street1(),
                spec.singleAddress().street2(),
                spec.singleAddress().city(),
                spec.singleAddress().state(),
                spec.singleAddress().zip()
        );
    }

}
