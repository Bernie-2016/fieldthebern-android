package com.berniesanders.canvass.repositories;

import android.content.Context;
import com.berniesanders.canvass.FTBApplication;
import com.berniesanders.canvass.config.Config;
import com.berniesanders.canvass.models.Collection;
import com.berniesanders.canvass.repositories.specs.CollectionSpec;
import com.google.gson.Gson;
import com.google.gson.stream.JsonReader;
import com.squareup.okhttp.*;
import okio.Buffer;
import okio.BufferedSink;
import okio.BufferedSource;
import okio.Okio;
import retrofit.GsonConverterFactory;
import retrofit.Retrofit;
import retrofit.RxJavaCallAdapterFactory;
import rx.Observable;
import rx.Subscriber;
import rx.functions.Func1;
import timber.log.Timber;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.io.*;

/**
 * Data repository for loading the tiles on the "home" page
 */
@Singleton
public class CollectionRepo {

    final Gson gson;
    private final Context context;
    private Collection collectionMemCache;

    private static final String JSON_FILE_PATH = "ftb.json";

    @Inject
    public CollectionRepo(Gson gson, Context context) {
        this.gson = gson;
        this.context = context;
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

        OkHttpClient client = new OkHttpClient();
        client.interceptors().add(new Interceptor() {
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
        });


        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(FTBApplication.getComponent().config().getFeelTheBernUrl())
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
