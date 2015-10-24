/*
 * Copyright 2015 FeelTheBern.org
 *
 * Small portions of this file are Copyright 2014 Square Inc.
 * Originally licensed under the Apache License, Version 2.0 (the "License");
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
 */
package org.feelthebern.android.screens;

import android.os.Bundle;

import com.google.gson.Gson;

import org.feelthebern.android.FTBApplication;
import org.feelthebern.android.R;
import org.feelthebern.android.config.UrlConfig;
import org.feelthebern.android.dagger.CusomScreenScope;
import org.feelthebern.android.dagger.MainComponent;
import org.feelthebern.android.models.Collection;
import org.feelthebern.android.mortar.FlowPathBase;
import org.feelthebern.android.mortar.Layout;
import org.feelthebern.android.repositories.HomeRepo;
import org.feelthebern.android.repositories.specs.HomeIssueSpec;
import org.feelthebern.android.views.MainView;

import javax.inject.Inject;

import mortar.ViewPresenter;
import rx.Observer;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import timber.log.Timber;

@Layout(R.layout.main_view)
public class Main extends FlowPathBase {

    public Main() {
    }

    @Override
    public int getLayout() {
        return R.layout.main_view;
    }

    @Override
    public Object createComponent() {
        return DaggerMain_Component.builder()
                .mainComponent(FTBApplication.getComponent())
                .build();
    }

    @Override
    public String getScopeName() {
        return Main.class.getName();
    }

    @CusomScreenScope
    @dagger.Component(dependencies = MainComponent.class)
    public interface Component {
        void inject(MainView t);
        Gson gson();
        HomeRepo repo();
    }

    @CusomScreenScope
    static public class Presenter extends ViewPresenter<MainView> {

        final Gson gson;
        final HomeRepo repo;
        Subscription subscription;

        @Inject
        Presenter(Gson gson, HomeRepo repo) {
            this.gson = gson;
            this.repo = repo;
        }


        @Override
        protected void onLoad(Bundle savedInstanceState) {

            Timber.v("main repo loading");
            HomeIssueSpec spec = new HomeIssueSpec(UrlConfig.HOME_JSON_URL_STUB);

            subscription = repo.get(spec)
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeOn(Schedulers.io())
                    .subscribe(observer);
        }

        Observer<Collection> observer = new Observer<Collection>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                Timber.e(e, "Main presenter error in observer/rx");
            }

            @Override
            public void onNext(Collection collection) {
                getView().setData(collection);
                Timber.v("main repo returned the collection");
            }
        };

        @Override
        protected void onSave(Bundle outState) {
        }


        @Override
        public void dropView(MainView view) {
            super.dropView(view);
            if (subscription!=null && !subscription.isUnsubscribed()) {
                subscription.unsubscribe();
            }
        }
    }
}
