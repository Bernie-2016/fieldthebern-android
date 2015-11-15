package com.berniesanders.canvass.dagger;

import android.content.Context;

import com.berniesanders.canvass.controllers.ErrorToastController;
import com.berniesanders.canvass.models.Content;
import com.berniesanders.canvass.parsing.CollectionDeserializer;
import com.berniesanders.canvass.parsing.PageContentDeserializer;
import com.berniesanders.canvass.repositories.TokenRepo;
import com.berniesanders.canvass.repositories.UserRepo;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import com.berniesanders.canvass.models.ApiItem;
import com.berniesanders.canvass.repositories.CollectionRepo;
import com.berniesanders.canvass.repositories.PageRepo;

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

    public MainModule(Context context) {
        this.context = context.getApplicationContext();

        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.registerTypeAdapter(ApiItem.class, new CollectionDeserializer());
        gsonBuilder.registerTypeAdapter(Content.class, new PageContentDeserializer());

        gson = gsonBuilder.setPrettyPrinting().create();
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
        return new TokenRepo(this.gson, context);
    }

    @Provides
    @Singleton
    public UserRepo provideUserRepo(TokenRepo tokenRepo) {
        return new UserRepo(gson, tokenRepo);
    }

    @Provides
    @Singleton
    public ErrorToastController provideErrorToastController() {
        return new ErrorToastController(context, gson);
    }

}
