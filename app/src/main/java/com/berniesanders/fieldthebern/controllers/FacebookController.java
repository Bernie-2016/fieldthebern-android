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

package com.berniesanders.fieldthebern.controllers;

/**
 *
 */

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;

import java.util.Arrays;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import mortar.Presenter;
import mortar.bundler.BundleService;
import rx.functions.Action0;
import timber.log.Timber;

import static mortar.bundler.BundleService.getBundleService;

/**
 * Provides a route to coordinate facebook auth with the MainActivity
 */
public class FacebookController extends Presenter<FacebookController.Activity> {

    public interface Activity {
        AppCompatActivity getActivity();
    }

    CallbackManager callbackManager;
    Action0 onSuccess;

    FacebookController() {
    }

    @Override
    public void onLoad(Bundle savedInstanceState) {
        Timber.v("FacebookController.onLoad()");
    }


    @Override
    protected BundleService extractBundleService(Activity activity) {
        return getBundleService(activity.getActivity());
    }



    public void loginWithFacebook(Action0 onSuccess) {

        Timber.v("FacebookController.loginWithFacebook()");

        this.onSuccess = onSuccess;
        setupFacebookAuth();

        LoginManager.getInstance()
                .logInWithReadPermissions(
                        getView().getActivity(),
                        Arrays.asList("public_profile", "email", "user_friends"));
    }

    public void setupFacebookAuth() {
        Timber.v("FacebookController.setupFacebookAuth()");

        callbackManager = CallbackManager.Factory.create();

        LoginManager.getInstance().registerCallback(callbackManager,
                new FacebookCallback<LoginResult>() {
                    @Override
                    public void onSuccess(LoginResult loginResult) {
                        Timber.v("FacebookCallback.onSuccess()");
                        AccessToken accessToken = loginResult.getAccessToken();
                        onSuccess.call();
                    }

                    @Override
                    public void onCancel() {
                        Timber.v("FacebookCallback.onCancel()");
                    }

                    @Override
                    public void onError(FacebookException exception) {
                        Timber.e(exception, "FacebookCallback.onError()");
                    }
                });
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        Timber.v("FacebookController.onActivityResult()");
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }


    @Module
    public static class FacebookModule {

        @Provides
        @Singleton
        FacebookController provideFacebookController() {
            return new FacebookController();
        }
    }
}
