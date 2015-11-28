/*
 * Copyright 2014 Square Inc.
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
 * Copyright 2015 FeelTheBern.org
 */
package com.berniesanders.fieldthebern;

import android.app.Application;

import com.berniesanders.fieldthebern.controllers.ActionBarController;
import com.berniesanders.fieldthebern.controllers.DialogController;
import com.berniesanders.fieldthebern.dagger.DaggerMainComponent;
import com.berniesanders.fieldthebern.dagger.MainComponent;
import com.berniesanders.fieldthebern.dagger.MainModule;
import com.squareup.otto.Bus;

import com.berniesanders.fieldthebern.mortar.DaggerService;

import mortar.MortarScope;
import timber.log.Timber;

/**
 *
 */
public class FTBApplication extends Application {

    private MortarScope rootScope;
    static MainComponent component;
    static Bus bus;

    @Override
    public Object getSystemService(String name) {
        if (rootScope == null && component!=null) {
            rootScope = MortarScope.buildRootScope()
                    .withService(DaggerService.DAGGER_SERVICE, getComponent())
                            .build("Root");
        }

        if (rootScope == null) {
            return super.getSystemService(name);
        }
        return rootScope.hasService(name) ? rootScope.getService(name) : super.getSystemService(name);

    }

    @Override
    public void onCreate() {
        super.onCreate();

        if (BuildConfig.DEBUG) {
            Timber.plant(new Timber.DebugTree());
        }
        component = DaggerMainComponent.builder()
                .mainModule(new MainModule(getApplicationContext()))
                .actionBarModule(new ActionBarController.ActionBarModule())
                .dialogModule(new DialogController.DialogModule())
                .build();

//How to set a default application font
//        CalligraphyConfig.initDefault(new CalligraphyConfig.Builder()
//                .setDefaultFontPath("fonts/Dosis-Regular.otf")
//                .setFontAttrId(R.attr.fontPath)
//                .build()
//        );

        bus = new Bus();
    }

    public static Bus getEventBus() {
        return bus;
    }

    //TODO this is a bit of an anti-pattern
    public static MainComponent getComponent() {
        return component;
    }
}
