package com.berniesanders.canvass.dagger;

import com.berniesanders.canvass.MainActivity;
import com.berniesanders.canvass.controllers.ActionBarController;
import com.berniesanders.canvass.controllers.DialogController;
import com.berniesanders.canvass.controllers.ErrorToastController;
import com.berniesanders.canvass.db.SearchMatrixCursor;
import com.google.gson.Gson;

import com.berniesanders.canvass.repositories.CollectionRepo;
import com.berniesanders.canvass.repositories.PageRepo;

import javax.inject.Singleton;

import dagger.Component;

/**
 */
@Singleton
@Component(
        modules = {
        ActionBarController.ActionBarModule.class,
        DialogController.DialogModule.class,
        MainModule.class}
)
public interface MainComponent {
    void inject(SearchMatrixCursor smc);
    void inject(MainActivity mainActivity);
    Gson gson();
    ErrorToastController errorToastController();
    CollectionRepo collectionRepo();
    PageRepo pageRepo();

    ActionBarController actionBarController();
    DialogController dialogController();
}
