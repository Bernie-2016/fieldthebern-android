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
import android.content.Context;

import org.feelthebern.android.dagger.MainComponent;
import org.feelthebern.android.dagger.MainModule;
import org.feelthebern.android.mortar.DaggerService;

import dagger.Component;
import mortar.MortarScope;

/**
 *
 */
public class FTBApplication extends Application {

    private MortarScope rootScope;


    @Override
    public Object getSystemService(String name) {
        if (rootScope == null) {
            rootScope = MortarScope.buildRootScope()
                    .withService(DaggerService.SERVICE_NAME,
                            DaggerService.createComponent(MainComponent.class,
                                    new MainModule(getApplicationContext())))
                            .build("Root");
        }

        return rootScope.hasService(name) ? rootScope.getService(name) : super.getSystemService(name);
    }



}
