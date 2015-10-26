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
package org.feelthebern.android;

import android.app.Application;

import com.squareup.otto.Bus;

import org.feelthebern.android.dagger.DaggerMainComponent;
import org.feelthebern.android.dagger.MainComponent;
import org.feelthebern.android.dagger.MainModule;
import org.feelthebern.android.mortar.DaggerService;

import mortar.MortarScope;
import timber.log.Timber;
import uk.co.chrisjenx.calligraphy.CalligraphyConfig;

/**
 *
 */
public class FTBApplication extends Application {

    private MortarScope rootScope;
    static MainComponent component;
    static Bus bus;

    @Override
    public Object getSystemService(String name) {
        if (rootScope == null) {
            rootScope = MortarScope.buildRootScope()
                    .withService(DaggerService.DAGGER_SERVICE, getComponent())
                            .build("Root");
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
                .build();

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

    public static MainComponent getComponent() {
        return component;
    }
}
