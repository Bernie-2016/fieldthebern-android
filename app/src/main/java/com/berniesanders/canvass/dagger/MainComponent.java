package com.berniesanders.canvass.dagger;

import com.berniesanders.canvass.MainActivity;
import com.berniesanders.canvass.db.SearchMatrixCursor;
import com.google.gson.Gson;

import com.berniesanders.canvass.repositories.CollectionRepo;
import com.berniesanders.canvass.repositories.PageRepo;

import javax.inject.Singleton;

import dagger.Component;

/**
 */
@Singleton
@Component(modules = MainModule.class)
public interface MainComponent {
    void inject(SearchMatrixCursor smc);
    Gson gson();
    CollectionRepo collectionRepo();
    PageRepo pageRepo();
}
