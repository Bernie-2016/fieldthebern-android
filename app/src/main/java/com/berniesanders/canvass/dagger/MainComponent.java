package com.berniesanders.canvass.dagger;

import android.content.Context;

import com.berniesanders.canvass.MainActivity;
import com.berniesanders.canvass.controllers.ActionBarController;
import com.berniesanders.canvass.controllers.DialogController;
import com.berniesanders.canvass.controllers.ErrorToastController;
import com.berniesanders.canvass.controllers.FacebookController;
import com.berniesanders.canvass.controllers.LocationController;
import com.berniesanders.canvass.db.SearchMatrixCursor;
import com.berniesanders.canvass.repositories.CollectionRepo;
import com.berniesanders.canvass.repositories.TokenRepo;
import com.berniesanders.canvass.repositories.UserRepo;
import com.berniesanders.canvass.screens.InitialScreen;
import com.f2prateek.rx.preferences.RxSharedPreferences;
import com.google.gson.Gson;

import javax.inject.Singleton;

import dagger.Component;

/**
 */
@Singleton
@Component(
        modules = {
            MainModule.class,
            ActionBarController.ActionBarModule.class,
            DialogController.DialogModule.class,
            FacebookController.FacebookModule.class,
            LocationController.LocationModule.class
        }
)
public interface MainComponent {
    void inject(SearchMatrixCursor smc);
    void inject(MainActivity mainActivity);
    void inject(InitialScreen initialScreen);

    Gson gson();
    ErrorToastController errorToastController();
    CollectionRepo collectionRepo();
    TokenRepo tokenRepo();
    UserRepo userRepo();
    Context context();

    ActionBarController actionBarController();
    DialogController dialogController();
    RxSharedPreferences rxPrefs();
    FacebookController facebookController();
    LocationController locationController();
}
