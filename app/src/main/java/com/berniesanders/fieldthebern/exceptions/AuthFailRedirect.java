package com.berniesanders.fieldthebern.exceptions;


import android.view.View;

import com.berniesanders.fieldthebern.models.User;
import com.berniesanders.fieldthebern.screens.LoginScreen;
import com.bugsnag.android.Bugsnag;

import flow.Flow;
import flow.History;
import timber.log.Timber;

/*
 * Made with love by volunteers
 * Copyright 2015 BernieSanders.com, FeelTheBern.org, 
 * Coderly, LostPacketSoftware and the volunteers
 * License: GNU AGPLv3 - https://gnu.org/licenses/agpl.html 
 */
public class AuthFailRedirect {

    /**
     * Sends the user to the LoginScreen if they got a "too many redirects" oauth failure.
     *
     * Use this as a fallback on all authenticated API requests
     *
     * @param throwable     The Error message passed to an Observer's onError method
     * @param view          The view currently displayed to the user, used to get a copy of flow.
     * @return              True if there was an auth failure.
     */
    public static boolean redirectOnFailure(Throwable throwable, final View view) {
        if (throwable instanceof java.net.ProtocolException) {
            if (view==null){
                Timber.e(throwable, "got ProtocolException but cannot redirect safely view is null");
                return true;
            }
            Flow.get(view).setHistory(History.single(new LoginScreen(new User())), Flow.Direction.BACKWARD);
            Timber.e(throwable, "");
            Bugsnag.notify(throwable);
            return true;
        }

        return false;
    }
}
