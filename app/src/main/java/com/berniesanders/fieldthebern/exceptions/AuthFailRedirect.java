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

package com.berniesanders.fieldthebern.exceptions;


import android.view.View;

import com.berniesanders.fieldthebern.models.User;
import com.berniesanders.fieldthebern.screens.LoginScreen;
import com.crashlytics.android.Crashlytics;

import flow.Flow;
import flow.History;
import timber.log.Timber;

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
            Crashlytics.logException(throwable);
            return true;
        }

        return false;
    }
}
