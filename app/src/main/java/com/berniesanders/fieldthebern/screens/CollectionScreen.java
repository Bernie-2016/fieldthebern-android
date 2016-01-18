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

package com.berniesanders.fieldthebern.screens;

import android.content.res.Configuration;
import android.os.Bundle;
import android.os.Parcelable;

import com.berniesanders.fieldthebern.R;
import com.berniesanders.fieldthebern.adapters.CollectionRecyclerAdapter;
import com.berniesanders.fieldthebern.annotations.Layout;
import com.berniesanders.fieldthebern.models.Collection;
import com.berniesanders.fieldthebern.controllers.ActionBarController;
import com.berniesanders.fieldthebern.controllers.ActionBarService;
import com.berniesanders.fieldthebern.mortar.FlowPathBase;
import com.berniesanders.fieldthebern.views.CollectionView;

import javax.inject.Inject;
import javax.inject.Singleton;

import dagger.Provides;
import flow.path.Path;
import mortar.MortarScope;
import mortar.ViewPresenter;
import timber.log.Timber;

/**
 *
 */
@Layout(R.layout.screen_collection)
public class CollectionScreen extends FlowPathBase {

    private final Collection collection;
    public Parcelable savedState;

    public CollectionScreen(Collection collection) {
        this.collection = collection;
    }

    @Override
    public Object createComponent() {
        return DaggerCollectionScreen_Component
                .builder()
                .module(new Module(collection))
                .build();
    }

    @Override
    public String getScopeName() {
        return CollectionScreen.class.getName() + collection.hashCode();
    }


    @dagger.Module
    class Module {
        private final Collection collection;

        public Module(Collection collection) {
            this.collection = collection;
        }

        @Provides
        public Collection provideCollection() {
            return collection;
        }
    }

    @Singleton
    @dagger.Component(modules = Module.class)
    public interface Component {
        void inject(CollectionView target);
        Collection getCollection();
    }

    @Singleton
    static public class Presenter extends ViewPresenter<CollectionView> {

        private final Collection collection;
        Parcelable recyclerViewState;

        private static final String BUNDLE_RECYCLER_LAYOUT = "CollectionScreen.recycler.layout";


        @Inject
        Presenter(Collection collection) {
            this.collection = collection;
        }


        @Override
        protected void onLoad(Bundle savedInstanceState) {

            Timber.v("onLoad passed collection: %s", collection);

            try {
                if (savedInstanceState != null) {
                    Timber.v("onLoad savedInstanceState collection: %s", collection);
                    recyclerViewState = savedInstanceState.getParcelable(BUNDLE_RECYCLER_LAYOUT);
                } else {
                    recyclerViewState = ((CollectionScreen) Path.get(getView().getContext())).savedState;
                }
            } catch (Exception e) {
                Timber.e(e, "Error loading parcels");
            }

            Timber.v("onLoad collection: %s", collection.getTitle());
            getView().setAdapter(new CollectionRecyclerAdapter(collection));

            if (recyclerViewState!=null) {
                getView().getLayoutManager().onRestoreInstanceState(recyclerViewState);
                ActionBarService
                        .get(getView())
                        .showToolbar();
            } 

            setActionBar();
        }
        void setActionBar() {

            ActionBarController.MenuAction menu =
                    new ActionBarController
                            .MenuAction()
                            .setIsSearch();

            ActionBarService
                    .get(getView())
                    .openAppbar()
                    .setMainImage(collection.getImageUrlFull())
                    .setConfig(new ActionBarController.Config(collection.getTitle(), menu));
        }


        @Override
        protected void onSave(Bundle outState) {
            Timber.v("onSave");
            saveState(outState);
        }

        private void saveState(Bundle outState) {

            if (getView()==null) { return; }
            if (getView().getLayoutManager()==null) { return; }
            outState.putParcelable(BUNDLE_RECYCLER_LAYOUT, getView().getLayoutManager().onSaveInstanceState());
            outState.putParcelable(Collection.COLLECTION_PARCEL, collection);
            ((CollectionScreen)Path.get(getView().getContext())).savedState = getView().getLayoutManager().onSaveInstanceState();
        }

        @Override
        public void dropView(CollectionView view) {
            saveState(new Bundle());
            super.dropView(view);
        }

        @Override
        protected void onEnterScope(MortarScope scope) {
            super.onEnterScope(scope);
            Timber.v("onEnterScope: %s", scope);
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof CollectionScreen)) return false;

        CollectionScreen that = (CollectionScreen) o;

        return collection.equals(that.collection);

    }

    @Override
    public int hashCode() {
        return collection.hashCode();
    }
}
