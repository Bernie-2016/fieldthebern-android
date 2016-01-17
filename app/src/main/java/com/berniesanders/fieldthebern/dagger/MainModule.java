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

package com.berniesanders.fieldthebern.dagger;

import android.content.Context;
import android.content.SharedPreferences;
import android.location.LocationManager;
import android.preference.PreferenceManager;
import com.berniesanders.fieldthebern.config.Config;
import com.berniesanders.fieldthebern.config.ConfigImpl;
import com.berniesanders.fieldthebern.controllers.ToastController;
import com.berniesanders.fieldthebern.models.ApiItem;
import com.berniesanders.fieldthebern.models.CanvassData;
import com.berniesanders.fieldthebern.models.Content;
import com.berniesanders.fieldthebern.parsing.CanvassDataSerializer;
import com.berniesanders.fieldthebern.parsing.CollectionDeserializer;
import com.berniesanders.fieldthebern.parsing.ErrorResponseParser;
import com.berniesanders.fieldthebern.parsing.PageContentDeserializer;
import com.berniesanders.fieldthebern.repositories.AddressRepo;
import com.berniesanders.fieldthebern.repositories.CollectionRepo;
import com.berniesanders.fieldthebern.repositories.FieldOfficeRepo;
import com.berniesanders.fieldthebern.repositories.RankingsRepo;
import com.berniesanders.fieldthebern.repositories.StatesRepo;
import com.berniesanders.fieldthebern.repositories.TokenRepo;
import com.berniesanders.fieldthebern.repositories.UserRepo;
import com.berniesanders.fieldthebern.repositories.VisitRepo;
import com.berniesanders.fieldthebern.screens.MessageScreen;
import com.f2prateek.rx.preferences.RxSharedPreferences;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import dagger.Module;
import dagger.Provides;

import javax.inject.Singleton;

/**
 *
 */
@Module
@Singleton
public class MainModule {
    private final Context context;
    private final Gson gson;
    private final RxSharedPreferences rxPrefs;
    private final Config config;

    public MainModule(Context context) {
        this.context = context.getApplicationContext();

        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.setPrettyPrinting();
        gsonBuilder.disableHtmlEscaping();
        gsonBuilder.registerTypeAdapter(ApiItem.class, new CollectionDeserializer());
        gsonBuilder.registerTypeAdapter(Content.class, new PageContentDeserializer());
        gsonBuilder.registerTypeAdapter(CanvassData.class, new CanvassDataSerializer());
        gson = gsonBuilder.create();

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        rxPrefs = RxSharedPreferences.create(preferences);

        config = new ConfigImpl(context);
    }

    @Provides
    @Singleton
    public Gson provideGson() {
        return gson;
    }

    @Provides
    @Singleton
    public Context provideContext() {
        return context;
    }


    @Provides
    @Singleton
    public CollectionRepo provideCollectionRepo() {
        return new CollectionRepo(gson, context, config);
    }

    @Provides
    @Singleton
    public TokenRepo provideTokenRepo() {
        return new TokenRepo(this.gson, rxPrefs, config, context);
    }

    @Provides
    @Singleton
    public UserRepo provideUserRepo(TokenRepo tokenRepo) {
        return new UserRepo(gson, tokenRepo, rxPrefs, config, context);
    }

    @Provides
    @Singleton
    public AddressRepo provideAddressRepo(TokenRepo tokenRepo) {
        return new AddressRepo(gson, tokenRepo, rxPrefs, config, context);
    }

    @Provides
    @Singleton
    public StatesRepo provideStatesRepo() {
        return new StatesRepo(gson, context);
    }

    @Provides
    @Singleton
    public RxSharedPreferences provideRxPrefs() {
        return rxPrefs;
    }


    @Provides
    @Singleton
    public LocationManager provideLocationManager() {
        return (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
    }

    @Provides
    @Singleton
    public VisitRepo provideVisitRepo(TokenRepo tokenRepo) {
        return new VisitRepo(gson, tokenRepo, rxPrefs, config, context);
    }

    @Provides
    @Singleton
    public RankingsRepo provideRankingsRepo(TokenRepo tokenRepo) {
        return new RankingsRepo(gson, tokenRepo, rxPrefs, config, context);
    }

    @Provides
    @Singleton
    public FieldOfficeRepo provideFieldOfficeRepo(TokenRepo tokenRepo) {
        return new FieldOfficeRepo(gson, config, context);
    }
    @Provides
    @Singleton
    public MessageScreen provideMessageScreen(FieldOfficeRepo fieldOfficeRepo) {
        return new MessageScreen(fieldOfficeRepo, rxPrefs);
    }

    @Provides
    @Singleton
    public ErrorResponseParser provideErrorParser() {
        return new ErrorResponseParser(gson);
    }

}
