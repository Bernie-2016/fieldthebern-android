package org.feelthebern.android.repositories;

import android.content.Context;

import com.google.gson.Gson;
import com.google.gson.stream.JsonReader;
import com.squareup.okhttp.Interceptor;
import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Response;
import com.squareup.okhttp.ResponseBody;

import org.feelthebern.android.config.UrlConfig;
import org.feelthebern.android.models.Collection;
import org.feelthebern.android.models.Content;
import org.feelthebern.android.models.Page;
import org.feelthebern.android.repositories.specs.PageSpec;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.util.HashMap;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

import okio.BufferedSink;
import okio.Okio;
import retrofit.GsonConverterFactory;
import retrofit.Retrofit;
import retrofit.RxJavaCallAdapterFactory;
import rx.Observable;
import rx.functions.Func1;
import timber.log.Timber;

/**
 * Data repository for loading the pages
 * @see Page
 * @see PageSpec
 */
@Singleton
public class PageRepo {

    final Gson gson;
    private final Context context;
    private HashMap<PageSpec, List<Content>> cache;


    @Inject
    public PageRepo(Gson gson, Context context) {
        this.gson = gson;
        this.context = context;
        cache = new HashMap<>();
    }

    /**
     * Main "API" method to the PageRepo.
     *
     * This is what presenters can use to load data without caring where the data comes from.
     *
     * Generally you will want to subscribe on Schedulers.io()
     * and observe on AndroidSchedulers.mainThread(),
     * unit tests can run synchronously
     *
     */
    public Observable<List<Content>> get(final PageSpec spec) {

        Timber.v("loading spec: %s", spec.toString());

        if (cache.containsKey(spec)) {
            Timber.v("loading spec from mem cache");
            return Observable.just(cache.get(spec));
        }

        return getFromHttp(spec)
                .map(new Func1<List<Content>, List<Content>>() {
                    @Override
                    public List<Content> call(List<Content> contentList) {
                        cache.put(spec, contentList);
                        return contentList;
                    }
                });
    }


    /**
     */
    private Observable<List<Content>> getFromHttp(final PageSpec spec) {
        Timber.v("loading spec from http");
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(UrlConfig.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .build();

        PageSpec.PageEndpoint endpoint = retrofit.create(PageSpec.PageEndpoint.class);

        return endpoint.load(spec.url(), spec.id());
    }

}
