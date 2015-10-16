package org.feelthebern.android.dagger;

import android.content.Context;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.feelthebern.android.adapters.CollectionTypeAdapter;
import org.feelthebern.android.adapters.PageContentTypeAdapter;
import org.feelthebern.android.models.Collection;
import org.feelthebern.android.models.Content;

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
        gsonBuilder.registerTypeAdapter(Collection.class, new CollectionTypeAdapter());
        gsonBuilder.registerTypeAdapter(Content.class, new PageContentTypeAdapter());

        mGson = gsonBuilder.create();
    }

    @Provides
    public Gson provideGson() {
        return mGson;
    }

}
