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
import android.os.Parcelable;

import com.google.gson.Gson;

import org.feelthebern.android.FTBApplication;
import org.feelthebern.android.R;
import org.feelthebern.android.annotations.Layout;
import org.feelthebern.android.dagger.FtbScreenScope;
import org.feelthebern.android.dagger.MainComponent;
import org.feelthebern.android.events.ChangePageEvent;
import org.feelthebern.android.models.Collection;
import org.feelthebern.android.mortar.FlowPathBase;
import org.feelthebern.android.repositories.CollectionRepo;
import org.feelthebern.android.repositories.specs.CollectionSpec;
import org.feelthebern.android.views.MainView;

import javax.inject.Inject;

import flow.path.Path;
import mortar.MortarScope;
import mortar.ViewPresenter;
import rx.Observer;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import timber.log.Timber;

@Layout(R.layout.main_view)
public class Main extends FlowPathBase {

    Parcelable savedState;

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

    @FtbScreenScope
    @dagger.Component(dependencies = MainComponent.class)
    public interface Component {
        void inject(MainView t);
        Gson gson();
        CollectionRepo collectionRepo();
    }

    @FtbScreenScope
    static public class Presenter extends ViewPresenter<MainView> {

        final Gson gson;
        final CollectionRepo repo;
        Subscription subscription;

        private Collection collection;
        Parcelable recyclerViewState;

        private static final String BUNDLE_RECYCLER_LAYOUT = "Main.recycler.layout";

        @Inject
        Presenter(Gson gson, CollectionRepo repo) {
            this.gson = gson;
            this.repo = repo;
        }

        @Override
        protected void onLoad(Bundle savedInstanceState) {

            try {
                if (savedInstanceState != null ) {
                    if (collection == null) {
                        collection = savedInstanceState.getParcelable(Collection.COLLECTION_PARCEL);
                        Timber.v("onLoad savedInstanceState collection: %s", collection);
                    }
                    recyclerViewState = savedInstanceState.getParcelable(BUNDLE_RECYCLER_LAYOUT);
                } else {
                    //page = flowSavedBundle.getParcelable(Page.PAGE_PARCEL);
                    recyclerViewState = ((Main) Path.get(getView().getContext())).savedState;
                }
            } catch (Exception e) {
                Timber.e(e, "Error loading parcels");
            }

            if (collection == null) {
                Timber.v("main repo loading");
                getView().showLoadingAnimation();
                CollectionSpec spec = new CollectionSpec();

                subscription = repo.get(spec)
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribeOn(Schedulers.io())
                        .subscribe(observer);
            } else {
                Timber.v("onLoad collection: %s", collection.getTitle());
                setDataAndState();
            }
        }

        private void setDataAndState() {
            getView().hideLoadingAnimation();
            getView().setData(collection);

            String pageName = getView().getResources().getString(R.string.app_name);

            if (recyclerViewState!=null) {
                getView().getLayoutManager().onRestoreInstanceState(recyclerViewState);
            }

            new ChangePageEvent()
                    .with(FTBApplication.getEventBus())
                    .title(pageName)
                    .close(true)
                    .dispatch();
        }

        Observer<Collection> observer = new Observer<Collection>() {
            @Override
            public void onCompleted() {
                setDataAndState();
            }

            @Override
            public void onError(Throwable e) {
                Timber.e(e, "Main presenter error in observer/rx");
            }

            @Override
            public void onNext(Collection collection) {
                Presenter.this.collection = collection;
                Timber.v("main repo returned the collection");
            }
        };

        @Override
        protected void onSave(Bundle outState) {
            saveState(outState);
        }

        private void saveState(Bundle outState) {
            if (getView()==null) { return; }
            if (getView().getLayoutManager()==null) { return; }
            outState.putParcelable(BUNDLE_RECYCLER_LAYOUT, getView().getLayoutManager().onSaveInstanceState());
            outState.putParcelable(Collection.COLLECTION_PARCEL, collection);
            ((Main) Path.get(getView().getContext())).savedState = getView().getLayoutManager().onSaveInstanceState();
        }

        @Override
        public void dropView(MainView view) {
            saveState(new Bundle());
            if (subscription!=null && !subscription.isUnsubscribed()) {
                subscription.unsubscribe();
            }
            super.dropView(view);
        }
        @Override
        protected void onEnterScope(MortarScope scope) {
            super.onEnterScope(scope);
            Timber.v("onEnterScope: %s", scope);
        }
    }
}
