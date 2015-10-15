package org.feelthebern.android.repositories;

import com.google.gson.Gson;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import org.feelthebern.android.api.Api;
import org.feelthebern.android.models.Collection;
import org.feelthebern.android.repositories.specs.HomeIssueSpec;

import java.io.IOException;

import javax.inject.Inject;

import rx.Observable;
import rx.Subscriber;

/**
 *
 */
public class HomeRepo {


    Gson gson;

    @Inject
    public HomeRepo(Gson gson) {
        this.gson = gson;
    }

    public Observable<Collection> get(final HomeIssueSpec spec) {

        //TODO
        return Observable.create(new Observable.OnSubscribe<Collection>() {
            @Override
            public void call(final Subscriber<? super Collection> subscriber) {
                //subscriber.onNext();

                loadFeed(new Callback() {
                    @Override
                    public void onFailure(Request request, IOException e) {

                    }

                    @Override
                    public void onResponse(final Response response) throws IOException {
                        if (!response.isSuccessful()) {
                            return;
                        }

                        final String responseString = response.body().string();
                        final Collection homeCollection = gson.fromJson(responseString, Collection.class);
                        subscriber.onNext(homeCollection);
                    }
                },
                spec.url());
            }
        });
    }


    private void loadFeed(Callback callback, String url) {
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url(url)
                .build();

        client.newCall(request).enqueue(callback);
    }
}
