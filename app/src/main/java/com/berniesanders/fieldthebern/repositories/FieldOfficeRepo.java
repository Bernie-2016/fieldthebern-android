package com.berniesanders.fieldthebern.repositories;

import android.content.Context;

import com.berniesanders.fieldthebern.R;
import com.berniesanders.fieldthebern.config.Config;
import com.berniesanders.fieldthebern.models.FieldOfficeList;
import com.berniesanders.fieldthebern.repositories.interceptors.UserAgentInterceptor;
import com.google.gson.Gson;
import com.squareup.okhttp.OkHttpClient;

import java.io.IOException;
import java.io.InputStream;

import javax.inject.Inject;

import rx.Observable;
import rx.Subscriber;
import timber.log.Timber;

public class FieldOfficeRepo {

    final Gson gson;
    private final OkHttpClient client = new OkHttpClient();
    private final Context context;


    @Inject
    public FieldOfficeRepo(Gson gson,
                           Config config,
                           Context context) {
        this.gson = gson;
        this.context = context;

        client.interceptors().add(new UserAgentInterceptor(config.getUserAgent()));
    }

    public Observable<FieldOfficeList> get() {
        return getFromFile();
    }


    private Observable<FieldOfficeList> getFromFile() {

        return Observable.create(new Observable.OnSubscribe<FieldOfficeList>() {

            @Override
            public void call(Subscriber<? super FieldOfficeList> subscriber) {

                Timber.v("getFromFile()");
                String json = null;
                try {
                    InputStream is = context.getResources().openRawResource(R.raw.field_offices_geocoded);
                    int size = is.available();
                    byte[] buffer = new byte[size];
                    int read = is.read(buffer);
                    is.close();
                    json = new String(buffer, "UTF-8");
                } catch (IOException ex) {
                    Timber.e(ex, "error reading field office file");
                    ex.printStackTrace();
                }

                FieldOfficeList fieldOffices = gson.fromJson(json, FieldOfficeList.class);
                subscriber.onNext(fieldOffices);
                subscriber.onCompleted();
            }
        });

    }

}
