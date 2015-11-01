package org.feelthebern.android.repositories;

import android.content.Context;

import com.google.gson.Gson;
import com.google.gson.stream.JsonReader;

import org.feelthebern.android.config.UrlConfig;
import org.feelthebern.android.models.Collection;
import org.feelthebern.android.repositories.specs.CollectionSpec;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.Reader;

import javax.inject.Inject;
import javax.inject.Singleton;

import retrofit.GsonConverterFactory;
import retrofit.Retrofit;
import retrofit.RxJavaCallAdapterFactory;
import rx.Observable;
import rx.functions.Func1;

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
            return Observable.just(collectionMemCache);
        }
//        else if (fileCacheExists()) {
//            try {
//                return Observable.just(getFromFile());
//            } catch (FileNotFoundException e) {
//                Timber.e(e, "FileNotFoundException loading json");
//            }
//        }

        return getFromHttp(spec.url())
                .map(new Func1<Collection, Collection>() {
                    @Override
                    public Collection call(Collection collection) {
                        collectionMemCache = collection;
                        return collectionMemCache;
                    }
                });
    }

    private Collection getFromFile() throws FileNotFoundException {
        File file = new File(context.getFilesDir(), JSON_FILE_PATH);
        Reader fileReader = new FileReader(file);
        JsonReader jsonReader = new JsonReader(fileReader);
        collectionMemCache = gson.fromJson(jsonReader, Collection.class);
        return collectionMemCache;
    }

    private boolean fileCacheExists() {

        return new File(context.getFilesDir(), JSON_FILE_PATH)
                .exists();
    }




    /**
     * Might be best to pass the spec through to this method...?
     */
    private Observable<Collection> getFromHttp(final String urlStub) {


//        OkHttpClient client = new OkHttpClient();
//        client.interceptors().add(new Interceptor() {
//            @Override
//            public Response intercept(Chain chain) throws IOException {
//                Response response = chain.proceed(chain.request());
//                MediaType contentType = response.body().contentType();
//                String bodyString = response.body().string();
//                ResponseBody body = ResponseBody.create(contentType, bodyString);
//                ResponseBody body2 = ResponseBody.create(contentType, bodyString);
//                write(response.newBuilder().body(body2).build());
//                return response.newBuilder().body(body).build();
//
//            }
//        });


        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(UrlConfig.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                //.client(client)
                .build();

        CollectionSpec.CollectionEndpoint endpoint =
                retrofit.create(CollectionSpec.CollectionEndpoint.class);

        return endpoint.load(urlStub);
    }

//    private final Buffer buffer = new Buffer();
//
//    void read(BufferedSource in, long byteCount) throws IOException {
//        in.require(byteCount);
//        in.read(buffer, byteCount);
//    }

//    void write(Response response) throws IOException {
//        File downloadedFile = new File(context.getFilesDir(), JSON_FILE_PATH);
//
//        BufferedSink sink = Okio.buffer(Okio.sink(downloadedFile));
//        sink.writeAll(response.body().source());
//        sink.close();
//    }

}
