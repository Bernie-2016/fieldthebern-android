package com.berniesanders.canvass.dagger;

import android.content.Context;
import android.content.SharedPreferences;
import android.location.LocationManager;
import android.preference.PreferenceManager;

import com.berniesanders.canvass.controllers.ErrorToastController;
import com.berniesanders.canvass.controllers.FacebookController;
import com.berniesanders.canvass.location.LocationAdapter;
import com.berniesanders.canvass.models.Content;
import com.berniesanders.canvass.parsing.CollectionDeserializer;
import com.berniesanders.canvass.parsing.PageContentDeserializer;
import com.berniesanders.canvass.repositories.TokenRepo;
import com.berniesanders.canvass.repositories.UserRepo;
import com.f2prateek.rx.preferences.RxSharedPreferences;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import com.berniesanders.canvass.models.ApiItem;
import com.berniesanders.canvass.repositories.CollectionRepo;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 *
 */
@Module
@Singleton
public class MainModule {
    private final Context context;
    private final Gson gson;
    private final RxSharedPreferences rxPrefs;

    public MainModule(Context context) {
        this.context = context.getApplicationContext();

        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.registerTypeAdapter(ApiItem.class, new CollectionDeserializer());
        gsonBuilder.registerTypeAdapter(Content.class, new PageContentDeserializer());

        gson = gsonBuilder.setPrettyPrinting().create();

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        rxPrefs = RxSharedPreferences.create(preferences);
    }

    @Provides
    @Singleton
    public Gson provideGson() {
        return gson;
    }

    @Provides
    @Singleton
    public CollectionRepo provideCollectionRepo() {
        return new CollectionRepo(gson, context);
    }

    @Provides
    @Singleton
    public TokenRepo provideTokenRepo() {
        return new TokenRepo(this.gson, context, rxPrefs);
    }

    @Provides
    @Singleton
    public UserRepo provideUserRepo(TokenRepo tokenRepo) {
        return new UserRepo(gson, tokenRepo, rxPrefs);
    }

    @Provides
    @Singleton
    public ErrorToastController provideErrorToastController() {
        return new ErrorToastController(context, gson);
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
    public LocationAdapter provideLocationAdapter(LocationManager locationManager) {
        return new LocationAdapter(context, locationManager);
    }

}
