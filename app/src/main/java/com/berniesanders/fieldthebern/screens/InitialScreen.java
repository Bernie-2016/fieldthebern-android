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

package com.berniesanders.fieldthebern.screens;

import com.berniesanders.fieldthebern.models.Token;
import com.berniesanders.fieldthebern.models.User;
import com.f2prateek.rx.preferences.Preference;
import com.f2prateek.rx.preferences.RxSharedPreferences;
import com.google.gson.Gson;
import javax.inject.Inject;

/**
 * Quickly checks if the user is logged in and returns the screen to show the user.
 */
public class InitialScreen {

  @Inject RxSharedPreferences rxPrefs;

  @Inject Gson gson;

  /**
   * Quickly checks if the user is logged in and returns the screen to show the user.
   */
  public Object get() {

    //Temp commented out logic until login is completed

    Preference<String> userPref = rxPrefs.getString(User.PREF_NAME);
    String userString = userPref.get();
    User user = null;

    if (userString != null) {
      user = gson.fromJson(userPref.get(), User.class);
    }

    if (user != null) { // user has registered previously
      Preference<String> tokenPref = rxPrefs.getString(Token.PREF_NAME);
      Token token = gson.fromJson(tokenPref.get(), Token.class);

      if (token != null) { //user logged in at some point

        if (token.isExpired(System.currentTimeMillis())) {
          //TODO try to auth with the token?

          return new LoginScreen(user);
        }

        return new HomeScreen();
      }

      return new LoginScreen(new User());
    }

    boolean hasSeenIntro = rxPrefs.getBoolean(User.PREF_SEEN_APP_INTRO, false).get();
    if (hasSeenIntro) {
      return new ChooseSignupScreen();
    } else {
      return new AppIntroScreen();
    }
  }
}
