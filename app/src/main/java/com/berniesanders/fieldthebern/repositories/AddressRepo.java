/*
 * Made with love by volunteers
 * Copyright 2015 FeelTheBern.org, BernieSanders.com, Coderly, LostPacketSoftware
 * and the volunteers that wrote this code
 * License: GNU AGPLv3 - https://gnu.org/licenses/agpl.html
 */
package com.berniesanders.fieldthebern.repositories;

import android.content.Context;

import com.berniesanders.fieldthebern.config.Config;
import com.berniesanders.fieldthebern.exceptions.NetworkUnavailableException;
import com.berniesanders.fieldthebern.models.MultiAddressResponse;
import com.berniesanders.fieldthebern.models.SingleAddressResponse;
import com.berniesanders.fieldthebern.network.NetChecker;
import com.berniesanders.fieldthebern.repositories.auth.ApiAuthenticator;
import com.berniesanders.fieldthebern.repositories.interceptors.AddTokenInterceptor;
import com.berniesanders.fieldthebern.repositories.interceptors.UserAgentInterceptor;
import com.berniesanders.fieldthebern.repositories.specs.AddressSpec;
import com.f2prateek.rx.preferences.RxSharedPreferences;
import com.google.gson.Gson;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.logging.HttpLoggingInterceptor;

import javax.inject.Inject;
import javax.inject.Singleton;

import retrofit.GsonConverterFactory;
import retrofit.Retrofit;
import retrofit.RxJavaCallAdapterFactory;
import rx.Observable;
import rx.Subscriber;
import rx.functions.Func1;
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
    private final Context context;


    @Inject
    public AddressRepo(Gson gson,
                       TokenRepo tokenRepo,
                       RxSharedPreferences rxPrefs,
                       Config config,
                       Context context) {
        this.gson = gson;
        this.tokenRepo = tokenRepo;
        this.rxPrefs = rxPrefs;
        this.config = config;
        this.context = context;

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
    public Observable<MultiAddressResponse> getMultiple(final AddressSpec spec) {
        Timber.v("getMultiple()");

        return Observable.create(new Observable.OnSubscribe<MultiAddressResponse>() {
            @Override
            public void call(Subscriber<? super MultiAddressResponse> subscriber) {
                if (!NetChecker.connected(context)) {
                    subscriber.onError(new NetworkUnavailableException("No internet available"));
                }
            }
        })
        .flatMap(new Func1<MultiAddressResponse, Observable<MultiAddressResponse>>() {
            @Override
            public Observable<MultiAddressResponse> call(MultiAddressResponse multiAddressResponse) {
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
        });
    }

    /**
     *
     */
    public Observable<SingleAddressResponse> getSingle(final AddressSpec spec) {
        Timber.v("getSingle()");

        return Observable.create(new Observable.OnSubscribe<SingleAddressResponse>() {
            @Override
            public void call(Subscriber<? super SingleAddressResponse> subscriber) {
                if (!NetChecker.connected(context)) {
                    subscriber.onError(new NetworkUnavailableException("No internet available"));
                }
            }
        })
        .flatMap(new Func1<SingleAddressResponse, Observable<SingleAddressResponse>>() {
            @Override
            public Observable<SingleAddressResponse> call(SingleAddressResponse multiAddressResponse) {
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
        });

    }

}
