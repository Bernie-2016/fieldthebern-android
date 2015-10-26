package org.feelthebern.android.dagger;

import android.content.Context;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.feelthebern.android.parsing.CollectionDeserializer;
import org.feelthebern.android.parsing.PageContentDeserializer;
import org.feelthebern.android.models.Collection;
import org.feelthebern.android.models.Content;
import org.feelthebern.android.repositories.HomeRepo;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 *
 */
@Module
@Singleton
public class MainModule {
    private final Context mContext;
    private final Gson mGson;

    public MainModule(Context context) {
        mContext = context.getApplicationContext();

        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.registerTypeAdapter(Collection.class, new CollectionDeserializer());
        gsonBuilder.registerTypeAdapter(Content.class, new PageContentDeserializer());

        mGson = gsonBuilder.create();
    }

    @Provides
    @Singleton
    public Gson provideGson() {
        return mGson;
    }

    @Provides
    @Singleton
    public HomeRepo provideRepo(Gson gson) {
        return new HomeRepo(gson);
    }

}
