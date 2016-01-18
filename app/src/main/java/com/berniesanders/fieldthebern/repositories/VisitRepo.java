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
import com.berniesanders.fieldthebern.models.ApiAddress;
import com.berniesanders.fieldthebern.models.CanvassData;
import com.berniesanders.fieldthebern.models.Person;
import com.berniesanders.fieldthebern.models.Visit;
import com.berniesanders.fieldthebern.models.VisitResult;
import com.berniesanders.fieldthebern.network.NetChecker;
import com.berniesanders.fieldthebern.repositories.auth.ApiAuthenticator;
import com.berniesanders.fieldthebern.repositories.interceptors.AddTokenInterceptor;
import com.berniesanders.fieldthebern.repositories.interceptors.UserAgentInterceptor;
import com.berniesanders.fieldthebern.repositories.specs.VisitSpec;
import com.f2prateek.rx.preferences.RxSharedPreferences;
import com.google.gson.Gson;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.logging.HttpLoggingInterceptor;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

import retrofit.GsonConverterFactory;
import retrofit.Retrofit;
import retrofit.RxJavaCallAdapterFactory;
import rx.Observable;
import timber.log.Timber;

/**
 * For starting a visit, updating it, then submitting to the API
 */
@Singleton
public class VisitRepo {

    final Gson gson;
    private final TokenRepo tokenRepo;
    private final RxSharedPreferences rxPrefs;
    private final OkHttpClient client = new OkHttpClient();
    private final Config config;
    private final Context context;

    private Visit visit;
    private List<Person> previousPeople = new ArrayList<>();


    @Inject
    public VisitRepo(Gson gson, TokenRepo tokenRepo, RxSharedPreferences rxPrefs, Config config, Context context) {
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


    public boolean inProgress() {
        return visit != null;
    }

    public Visit get() {
        return visit;
    }

    public Visit start(final ApiAddress apiAddress) {
        visit = new Visit();
        visit.start();
        setPreviousPeople(apiAddress);
        setAddress(apiAddress);
        return visit;
    }

    public void addPerson(Person person) {
        if (!visit.included().contains(person)) {
            visit.included().add(person);
        }
    }

    void setPreviousPeople(final ApiAddress apiAddress) {
        List<CanvassData> included = apiAddress.included();
        previousPeople.clear();
        for(CanvassData canvassData : included) {
            if (canvassData.type().equals(Person.TYPE)) {
                previousPeople.add(Person.copy((Person) canvassData));
            }
        }
    }

    /**
     *
     */
    public Observable<VisitResult> submit() {
        
        Timber.v("submit()");

        if (!NetChecker.connected(context)) {
            return Observable.error(new NetworkUnavailableException("No internet available"));
        }

        //remove anyone who wasn't spoken to
        List<CanvassData> included = visit.included();

        List<Person> peopleToRemove = new ArrayList<>();

        for (CanvassData canvassData : included) {
            if (canvassData.type().equals(Person.TYPE)) {
                Person person = (Person) canvassData;
                if (!person.spokenTo()) {
                    peopleToRemove.add(person);
                }
            }
        }

        visit.included().removeAll(peopleToRemove);

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(config.getCanvassUrl())
                .addConverterFactory(GsonConverterFactory.create(gson))
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .client(client)
                .build();

        visit.stop(); //stop the timer

        VisitSpec.VisitEndpoint endpoint = retrofit.create(VisitSpec.VisitEndpoint.class);
        return endpoint.submit(visit);
    }

    public void clear() {
        visit = null;
        previousPeople.clear();
    }

    public List<Person> getPreviousPeople() {
        return previousPeople;
    }

    public void setAddress(ApiAddress apiAddress) {


        try {
            //remove the previous address so we don't have duplicates
            ApiAddress previousAddress = (ApiAddress) visit.included().remove(0);
            if (previousAddress != null) {
                if (!apiAddress.equals(previousAddress)) { //if the address changed, reset the visit timer
                    //visit.included().clear();
                    visit.start();
                    //setPreviousPeople(apiAddress);
                }
            }
        } catch (IndexOutOfBoundsException e) {
            //more logic than an error
            Timber.d("attempted to read a previous address ");
        }

        visit.included().add(0, apiAddress);
        List<CanvassData> included = apiAddress.included();

        for(CanvassData canvassData : included) {
            if (canvassData.type().equals(Person.TYPE)) {
                addPerson((Person) canvassData);
            }
        }
    }
}
