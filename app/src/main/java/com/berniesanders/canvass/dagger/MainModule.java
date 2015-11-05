package com.berniesanders.canvass.dagger;

import android.content.Context;

import com.berniesanders.canvass.models.Content;
import com.berniesanders.canvass.parsing.CollectionDeserializer;
import com.berniesanders.canvass.parsing.PageContentDeserializer;
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
    private final Gson mGson;

    public MainModule(Context context) {
        this.context = context.getApplicationContext();

        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.registerTypeAdapter(ApiItem.class, new CollectionDeserializer());
        gsonBuilder.registerTypeAdapter(Content.class, new PageContentDeserializer());

        mGson = gsonBuilder.setPrettyPrinting().create();
    }

    @Provides
    @Singleton
    public Gson provideGson() {
        return mGson;
    }

    @Provides
    @Singleton
    public CollectionRepo provideCollectionRepo(Gson gson) {
        return new CollectionRepo(gson, context);
    }

    @Provides
    @Singleton
    public PageRepo providePageRepo(Gson gson) {
        return new PageRepo(gson, context);
    }

}
