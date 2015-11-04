package org.feelthebern.android.dagger;

import com.google.gson.Gson;

import org.feelthebern.android.MainActivity;
import org.feelthebern.android.db.SearchMatrixCursor;
import org.feelthebern.android.repositories.CollectionRepo;
import org.feelthebern.android.repositories.PageRepo;

import javax.inject.Singleton;

import dagger.Component;

/**
 */
@Singleton
@Component(modules = MainModule.class)
public interface MainComponent {
    void inject(MainActivity mainActivity);
    void inject(SearchMatrixCursor smc);
    Gson gson();
    CollectionRepo collectionRepo();
    PageRepo pageRepo();
}
