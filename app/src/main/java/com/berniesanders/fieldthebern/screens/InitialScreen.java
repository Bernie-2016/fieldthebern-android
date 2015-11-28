package com.berniesanders.fieldthebern.screens;

import com.f2prateek.rx.preferences.RxSharedPreferences;
import com.google.gson.Gson;

import javax.inject.Inject;

/**
 * Quickly checks if the user is logged in and returns the screen to show the user.
 */
public class InitialScreen {


    @Inject
    RxSharedPreferences rxPrefs;

    @Inject
    Gson gson;

    /**
     * Quickly checks if the user is logged in and returns the screen to show the user.
     */
    public Object get() {

//Temp commented out logic until login is completed
//
//        Preference<String> userPref = rxPrefs.getString(User.PREF_NAME);
//
//        User user = gson.fromJson(userPref.get(), User.class);
//
//        if (user != null) { // user has registered previously
//            Preference<String> tokenPref = rxPrefs.getString(Token.PREF_NAME);
//            Token token = gson.fromJson(tokenPref.get(), Token.class);
//
//            if (token != null) { //user logged in at some point
//
//                if (token.isExpired(System.currentTimeMillis())) {
//                    //TODO try to auth with the token?
//                    return new LoginScreen(user);
//                }
//
//                return new HomeScreen();
//            }
//
//            return new LoginScreen(user);
//        }

        return new ChooseSignupScreen();
    }
}
