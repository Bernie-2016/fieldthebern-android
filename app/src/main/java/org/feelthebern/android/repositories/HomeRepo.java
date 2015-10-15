package org.feelthebern.android.repositories;

import com.google.gson.Gson;

import org.feelthebern.android.api.Api;
import org.feelthebern.android.models.Collection;
import org.feelthebern.android.repositories.specs.HomeIssueSpec;

import javax.inject.Inject;

import rx.Observable;
import rx.Subscriber;

/**
 *
 */
public class HomeRepo {


    @Inject
    Gson gson;

    @Inject
    Api api;


    public Observable<Collection> get(final HomeIssueSpec spec) {

        //TODO
        return Observable.create(new Observable.OnSubscribe<Collection>() {
            @Override
            public void call(Subscriber<? super Collection> subscriber) {
                //subscriber.onNext();
            }
        });
    }
}
