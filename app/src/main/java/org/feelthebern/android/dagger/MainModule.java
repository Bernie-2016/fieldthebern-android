package org.feelthebern.android.dagger;

import android.content.Context;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.feelthebern.android.api.adapters.CollectionTypeAdapter;
import org.feelthebern.android.api.models.Collection;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * Created by AndrewOrobator on 8/29/15.
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

        mGson = gsonBuilder.create();
    }

    @Provides
    public Gson provideGson() {
        return mGson;
    }

}
