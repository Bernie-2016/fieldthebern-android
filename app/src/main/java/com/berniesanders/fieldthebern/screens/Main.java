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
package com.berniesanders.fieldthebern.screens;

import android.os.Bundle;
import android.os.Parcelable;

import com.berniesanders.fieldthebern.FTBApplication;
import com.berniesanders.fieldthebern.R;
import com.berniesanders.fieldthebern.annotations.Layout;
import com.berniesanders.fieldthebern.dagger.FtbScreenScope;
import com.berniesanders.fieldthebern.dagger.MainComponent;
import com.berniesanders.fieldthebern.models.Collection;
import com.berniesanders.fieldthebern.controllers.ActionBarController;
import com.berniesanders.fieldthebern.controllers.ActionBarService;
import com.berniesanders.fieldthebern.mortar.FlowPathBase;
import com.berniesanders.fieldthebern.repositories.CollectionRepo;
import com.berniesanders.fieldthebern.repositories.specs.CollectionSpec;
import com.berniesanders.fieldthebern.views.MainView;

import javax.inject.Inject;

import butterknife.BindString;
import butterknife.ButterKnife;
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
    public Object createComponent() {
        return DaggerMain_Component
                .builder()
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
        CollectionRepo collectionRepo();
    }

    @FtbScreenScope
    static public class Presenter extends ViewPresenter<MainView> {

        final CollectionRepo repo;
        Subscription subscription;

        private Collection collection;
        Parcelable recyclerViewState;

        @BindString(R.string.main_issues_screen_title) String issuesScreenTitle;

        private static final String BUNDLE_RECYCLER_LAYOUT = "Main.recycler.layout";

        @Inject
        Presenter(CollectionRepo repo) {
            this.repo = repo;
        }

        @Override
        protected void onLoad(Bundle savedInstanceState) {

            ButterKnife.bind(this, getView());

            ActionBarService.get(getView()).showToolbar();

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

            setActionBar();
        }

        private void setDataAndState() {
            getView().hideLoadingAnimation();
            getView().setData(collection);

            String pageName = getView().getResources().getString(R.string.app_name);

            if (recyclerViewState!=null) {
                getView().getLayoutManager().onRestoreInstanceState(recyclerViewState);
            }

            ActionBarService.get(getView()).closeAppbar();

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


        void setActionBar() {
            ActionBarController.MenuAction menu =
                    new ActionBarController
                            .MenuAction()
                            .setIsSearch();

            ActionBarService.get(getView())
                    .setMainImage(null)
                    .openAppbar()
                    .setConfig(new ActionBarController.Config(issuesScreenTitle, menu));
        }
    }
}
