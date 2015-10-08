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
package org.feelthebern.android.screens;

import android.os.Bundle;
import android.widget.GridView;

import com.google.gson.Gson;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import org.feelthebern.android.R;
import org.feelthebern.android.api.Api;
import org.feelthebern.android.dagger.MainComponent;
import org.feelthebern.android.dagger.MainModule;
import org.feelthebern.android.issues.IssuesAdapter;
import org.feelthebern.android.models.Collection;
import org.feelthebern.android.views.MainView;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;

import javax.inject.Inject;
import javax.inject.Singleton;

import mortar.ViewPresenter;
import mortar.MortarScope;
import mortar.bundler.BundleServiceRunner;

import static mortar.MortarScope.buildChild;
import static mortar.MortarScope.findChild;

public class Main {

    public Main(){
    }

    @Singleton
    @dagger.Component(dependencies = MainComponent.class)
    public interface Component {
        void inject(MainView t);
        Gson gson();
    }

    @Singleton
    static public class Presenter extends ViewPresenter<MainView> {

        Api mApi;
        Gson mGson;

        @Inject
        Presenter(Gson gson) {
            //this.mApi = mApi;
            this.mGson = gson;
        }


        private GridView mGridView;

        @Override protected void onLoad(Bundle savedInstanceState) {
            //if (savedInstanceState != null && serial == -1) serial = savedInstanceState.getInt("serial");
            //getView().show("Update #" + ++serial + " at " + format.format(new Date()));

            mGridView = (GridView) getView().findViewById(R.id.issues_GridView);
            mApi.loadFeed(new Callback() {
                @Override
                public void onFailure(Request request, IOException e) {

                }

                @Override
                public void onResponse(final Response response) throws IOException {
                    if (!response.isSuccessful()) {
                        return;
                    }

                    final String responseString = response.body().string();
                    final Collection homeCollection = mGson.fromJson(responseString, Collection.class);
                    mGridView.post(new Runnable() {
                        @Override
                        public void run() {
                            mGridView.setAdapter(new IssuesAdapter(getView().getContext(), homeCollection.getApiItems()));
                        }
                    });
                }
            });
        }

        @Override protected void onSave(Bundle outState) {
            //outState.putInt("serial", serial);
        }
    }
}
