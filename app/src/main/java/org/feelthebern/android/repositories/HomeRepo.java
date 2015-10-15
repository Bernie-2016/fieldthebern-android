package org.feelthebern.android.repositories;

import com.google.gson.Gson;

import org.feelthebern.android.config.UrlConfig;
import org.feelthebern.android.models.Collection;
import org.feelthebern.android.repositories.specs.HomeIssueSpec;

import javax.inject.Inject;

import retrofit.GsonConverterFactory;
import retrofit.Retrofit;
import retrofit.RxJavaCallAdapterFactory;
import retrofit.http.GET;
import retrofit.http.Path;
import rx.Observable;

/**
 * Data repository for loading the tiles on the "home" page
 */
public class HomeRepo {

    final Gson gson;

    @Inject
    public HomeRepo(Gson gson) {
        this.gson = gson;
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
    public Observable<Collection> get(final HomeIssueSpec spec) {

        /*
            eventually this will be something like

            if (cached) {
                getFromDb();
            } else {
                getFromHttp();
            }

         */
        return getFromHttp(spec.url());
    }


    /**
     * Retrofit 2 endpoint definition
     *
     * TODO: better to put this with the model? How best to handle changing urls?
     */
    private interface MainEndpoint {
//        @GET(UrlConfig.HOME_JSON_URL_STUB)
//        Observable<Collection> load();
        @GET("{urlStub}/")
        Observable<Collection> load(@Path("urlStub") String urlStub);
    }

    /**
     * Might be best to pass the spec through to this method...?
     */
    private Observable<Collection> getFromHttp(final String urlStub) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(UrlConfig.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .build();

        MainEndpoint endpoint = retrofit.create(MainEndpoint.class);

        return endpoint.load(urlStub);
    }
}
