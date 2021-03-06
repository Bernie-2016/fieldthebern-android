/*
 * Copyright (c) 2016 - Bernie 2016, Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */

package com.berniesanders.fieldthebern.dagger;

import android.content.Context;
import com.berniesanders.fieldthebern.MainActivity;
import com.berniesanders.fieldthebern.controllers.ActionBarController;
import com.berniesanders.fieldthebern.controllers.DialogController;
import com.berniesanders.fieldthebern.controllers.FacebookController;
import com.berniesanders.fieldthebern.controllers.LocationController;
import com.berniesanders.fieldthebern.controllers.PermissionController;
import com.berniesanders.fieldthebern.controllers.PhotoController;
import com.berniesanders.fieldthebern.controllers.ProgressDialogController;
import com.berniesanders.fieldthebern.controllers.ToastController;
import com.berniesanders.fieldthebern.db.SearchMatrixCursor;
import com.berniesanders.fieldthebern.parsing.ErrorResponseParser;
import com.berniesanders.fieldthebern.repositories.AddressRepo;
import com.berniesanders.fieldthebern.repositories.CollectionRepo;
import com.berniesanders.fieldthebern.repositories.FieldOfficeRepo;
import com.berniesanders.fieldthebern.repositories.RankingsRepo;
import com.berniesanders.fieldthebern.repositories.StatesRepo;
import com.berniesanders.fieldthebern.repositories.TokenRepo;
import com.berniesanders.fieldthebern.repositories.UserRepo;
import com.berniesanders.fieldthebern.repositories.VisitRepo;
import com.berniesanders.fieldthebern.screens.AppIntroScreen;
import com.berniesanders.fieldthebern.screens.InitialScreen;
import com.berniesanders.fieldthebern.screens.MessageScreen;
import com.f2prateek.rx.preferences.RxSharedPreferences;
import com.google.gson.Gson;
import dagger.Component;
import javax.inject.Singleton;

/**
 */
@Singleton
@Component(
    modules = {
        MainModule.class, ActionBarController.ActionBarModule.class,
        DialogController.DialogModule.class, FacebookController.FacebookModule.class,
        ProgressDialogController.ProgressDialogModule.class,
        LocationController.LocationModule.class, PermissionController.PermissionModule.class,
        ToastController.ToastModule.class, PhotoController.PhotoModule.class
    })
public interface MainComponent {
  void inject(SearchMatrixCursor smc);

  void inject(MainActivity mainActivity);

  void inject(InitialScreen initialScreen);

  void inject(AppIntroScreen appIntroScreen);

  Gson gson();

  ToastController toastController();

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

  ErrorResponseParser errorParser();

  PhotoController photoController();

  RankingsRepo rankingsRepo();

  StatesRepo statesRepo();

  FieldOfficeRepo fieldOfficeRepo();

  MessageScreen messageScreen();
}
