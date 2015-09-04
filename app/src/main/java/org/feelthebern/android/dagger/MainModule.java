package org.feelthebern.android.dagger;

import android.content.Context;

import com.google.gson.Gson;

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

    public MainModule(Context context) {
        mContext = context.getApplicationContext();
    }

    @Provides
    public Gson provideGson() {
        return new Gson();
    }

}
