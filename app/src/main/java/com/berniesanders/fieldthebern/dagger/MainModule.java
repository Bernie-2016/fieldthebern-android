package com.berniesanders.fieldthebern.dagger;

import android.content.Context;
import android.content.SharedPreferences;
import android.location.LocationManager;
import android.preference.PreferenceManager;
import com.berniesanders.fieldthebern.config.Config;
import com.berniesanders.fieldthebern.config.ConfigImpl;
import com.berniesanders.fieldthebern.controllers.ErrorToastController;
import com.berniesanders.fieldthebern.models.ApiItem;
import com.berniesanders.fieldthebern.models.Content;
import com.berniesanders.fieldthebern.parsing.CollectionDeserializer;
import com.berniesanders.fieldthebern.parsing.PageContentDeserializer;
import com.berniesanders.fieldthebern.repositories.AddressRepo;
import com.berniesanders.fieldthebern.repositories.CollectionRepo;
import com.berniesanders.fieldthebern.repositories.TokenRepo;
import com.berniesanders.fieldthebern.repositories.UserRepo;
import com.berniesanders.fieldthebern.repositories.VisitRepo;
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
        gsonBuilder.registerTypeAdapter(ApiItem.class, new CollectionDeserializer());
        gsonBuilder.registerTypeAdapter(Content.class, new PageContentDeserializer());

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
        return new TokenRepo(this.gson, rxPrefs, config);
    }

    @Provides
    @Singleton
    public UserRepo provideUserRepo(TokenRepo tokenRepo) {
        return new UserRepo(gson, tokenRepo, rxPrefs, config);
    }

    @Provides
    @Singleton
    public AddressRepo provideAddressRepo(TokenRepo tokenRepo) {
        return new AddressRepo(gson, tokenRepo, rxPrefs, config);
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
    public VisitRepo provideVisitRepo(TokenRepo tokenRepo) {
        return new VisitRepo(gson, tokenRepo, rxPrefs, config);
    }

}
