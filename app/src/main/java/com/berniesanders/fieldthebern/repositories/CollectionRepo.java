/*
 * Copyright (c) 2016 - Bernie 2016, Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */

package com.berniesanders.fieldthebern.repositories;

import android.content.Context;

import com.berniesanders.fieldthebern.config.Config;
import com.berniesanders.fieldthebern.exceptions.NetworkUnavailableException;
import com.berniesanders.fieldthebern.models.Collection;
import com.berniesanders.fieldthebern.network.NetChecker;
import com.berniesanders.fieldthebern.repositories.interceptors.UserAgentInterceptor;
import com.berniesanders.fieldthebern.repositories.specs.CollectionSpec;
import com.google.gson.Gson;
import com.google.gson.stream.JsonReader;
import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Response;
import okhttp3.ResponseBody;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;

import javax.inject.Inject;
import javax.inject.Singleton;

import okhttp3.logging.HttpLoggingInterceptor;
import okio.Buffer;
import okio.BufferedSink;
import okio.BufferedSource;
import okio.Okio;
import retrofit2.GsonConverterFactory;
import retrofit2.Retrofit;
import retrofit2.RxJavaCallAdapterFactory;
import rx.Observable;
import rx.Subscriber;
import rx.functions.Func1;
import timber.log.Timber;

/**
 * Data repository for loading the tiles on the "home" page
 */
@Singleton
public class CollectionRepo {

    final Gson gson;
    private final Context context;
    private final Config config;
    private Collection collectionMemCache;

    private static final String JSON_FILE_PATH = "ftb.json";

    @Inject
    public CollectionRepo(Gson gson, Context context, Config config) {
        this.gson = gson;
        this.context = context;
        this.config = config;
    }

    /**
     * Main "API" method to the HomeRepo.
     *
     * This is what presenters can use to load data without caring where the data comes from.
     *
     * Generally you will want to subscribe on Schedulers.io()
     * and observe on AndroidSchedulers.mainThread(),
     * unit tests can run synchronously
     *
     * @param spec
     * @return
     */
    public Observable<Collection> get(final CollectionSpec spec) {

        if (collectionMemCache!=null) {

            Timber.v("returning mem cache");
            return Observable.just(collectionMemCache);

        } else if (fileCacheExists()) {

            return getFromFile();
        }

        return getFromHttp(spec.url())
                .map(new Func1<Collection, Collection>() {

                    @Override
                    public Collection call(Collection collection) {
                        collectionMemCache = collection;
                        return collectionMemCache;
                    }
                });
    }

    private Observable<Collection> getFromFile() {

        return Observable.create(new Observable.OnSubscribe<Collection>() {

            @Override
            public void call(Subscriber<? super Collection> subscriber) {

                Timber.v("getFromFile()");
                File file = new File(context.getFilesDir(), JSON_FILE_PATH);
                Reader fileReader;

                try {
                    fileReader = new FileReader(file);
                } catch (FileNotFoundException e) {
                    Timber.e(e, "File not found? we're doomed");
                    subscriber.onError(e);
                    return;
                }

                JsonReader jsonReader = new JsonReader(fileReader);
                collectionMemCache = gson.fromJson(jsonReader, Collection.class);
                subscriber.onNext(collectionMemCache);
                subscriber.onCompleted();
            }
        });

    }

    private boolean fileCacheExists() {

        return new File(context.getFilesDir(), JSON_FILE_PATH)
                .exists();
    }


    /**
     * Might be best to pass the spec through to this method...?
     */
    private Observable<Collection> getFromHttp(final String urlStub) {
        Timber.v("getFromHttp()");

        if (!NetChecker.connected(context)) {
            return Observable.error(new NetworkUnavailableException("No internet available"));
        }

        HttpLoggingInterceptor.Logger logger = new HttpLoggingInterceptor.Logger() {
            @Override
            public void log(String message) {
                Timber.v(message);
            }
        };
        HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor(logger);
        loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

        OkHttpClient client = new OkHttpClient.Builder()
                .addInterceptor(new UserAgentInterceptor(config.getUserAgent()))
                .addInterceptor(new Interceptor() {
                    @Override
                    public Response intercept(Interceptor.Chain chain) throws IOException {
                        Response response = chain.proceed(chain.request());
                        MediaType contentType = response.body().contentType();
                        String bodyString = response.body().string();
                        ResponseBody body = ResponseBody.create(contentType, bodyString);
                        ResponseBody body2 = ResponseBody.create(contentType, bodyString);
                        write(response.newBuilder().body(body2).build());
                        return response.newBuilder().body(body).build();

                    }
                })
                .addInterceptor(loggingInterceptor)
                .build();



        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(CollectionRepo.this.config.getFeelTheBernUrl())
                .addConverterFactory(GsonConverterFactory.create(gson))
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .client(client)
                .build();

        CollectionSpec.CollectionEndpoint endpoint =
                retrofit.create(CollectionSpec.CollectionEndpoint.class);

        return endpoint.load(urlStub);

    }

    private final Buffer buffer = new Buffer();

    void read(BufferedSource in, long byteCount) throws IOException {
        in.require(byteCount);
        in.read(buffer, byteCount);
    }

    void write(Response response) throws IOException {

        File downloadedFile = new File(context.getFilesDir(), JSON_FILE_PATH);
        BufferedSink sink = Okio.buffer(Okio.sink(downloadedFile));
        sink.writeAll(response.body().source());
        sink.close();
    }

}
