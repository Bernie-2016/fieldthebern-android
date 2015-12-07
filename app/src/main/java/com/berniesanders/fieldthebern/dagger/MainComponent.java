package com.berniesanders.fieldthebern.dagger;

import android.content.Context;

import com.berniesanders.fieldthebern.screens.AppIntroScreen;
import com.berniesanders.fieldthebern.MainActivity;
import com.berniesanders.fieldthebern.controllers.ActionBarController;
import com.berniesanders.fieldthebern.controllers.DialogController;
import com.berniesanders.fieldthebern.controllers.ErrorToastController;
import com.berniesanders.fieldthebern.controllers.FacebookController;
import com.berniesanders.fieldthebern.controllers.LocationController;
import com.berniesanders.fieldthebern.controllers.ProgressDialogController;
import com.berniesanders.fieldthebern.db.SearchMatrixCursor;
import com.berniesanders.fieldthebern.repositories.AddressRepo;
import com.berniesanders.fieldthebern.repositories.CollectionRepo;
import com.berniesanders.fieldthebern.repositories.TokenRepo;
import com.berniesanders.fieldthebern.repositories.UserRepo;
import com.berniesanders.fieldthebern.repositories.VisitRepo;
import com.berniesanders.fieldthebern.screens.InitialScreen;
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
            ProgressDialogController.ProgressDialogModule.class,
            LocationController.LocationModule.class
        }
)
public interface MainComponent {
    void inject(SearchMatrixCursor smc);
    void inject(MainActivity mainActivity);
    void inject(InitialScreen initialScreen);
    void inject(AppIntroScreen appIntroScreen);

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
    ProgressDialogController progressDialogController();
    AddressRepo addressRepo();
    VisitRepo visitRepo();
}
