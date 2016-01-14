package com.berniesanders.fieldthebern.repositories;


import android.content.Context;

import com.berniesanders.fieldthebern.R;
import com.berniesanders.fieldthebern.models.StatePrimaryResponse;
import com.google.gson.Gson;

import java.io.IOException;
import java.io.InputStream;

import javax.inject.Inject;
import javax.inject.Singleton;

import rx.Observable;
import rx.Subscriber;

/**
 *
 */
@Singleton
public class StatesRepo  {

    final Gson gson;
    private final Context context;

    @Inject
    public StatesRepo(Gson gson, Context context) {
        this.gson = gson;
        this.context = context;

    }

    public Observable<StatePrimaryResponse.StatePrimary[]> getStatePrimaries() {

        return Observable.create(new Observable.OnSubscribe<StatePrimaryResponse.StatePrimary[]>() {
            @Override
            public void call(Subscriber<? super StatePrimaryResponse.StatePrimary[]> subscriber) {

                String json = null;
                try {
                    InputStream is = context.getResources().openRawResource(R.raw.states);
                    int size = is.available();
                    byte[] buffer = new byte[size];
                    int read = is.read(buffer);
                    is.close();
                    json = new String(buffer, "UTF-8");
                } catch (IOException ex) {
                    ex.printStackTrace();
                }

                StatePrimaryResponse.StatePrimary[] response = gson.fromJson(json, StatePrimaryResponse.StatePrimary[].class);
                subscriber.onNext(response);
                subscriber.onCompleted();
            }
        });
    }

}
