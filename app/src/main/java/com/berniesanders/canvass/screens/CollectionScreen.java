package com.berniesanders.canvass.screens;

import android.content.res.Configuration;
import android.os.Bundle;
import android.os.Parcelable;

import com.berniesanders.canvass.R;
import com.berniesanders.canvass.adapters.CollectionRecyclerAdapter;
import com.berniesanders.canvass.annotations.Layout;
import com.berniesanders.canvass.models.Collection;
import com.berniesanders.canvass.mortar.ActionBarController;
import com.berniesanders.canvass.mortar.ActionBarService;
import com.berniesanders.canvass.mortar.FlowPathBase;
import com.berniesanders.canvass.views.CollectionView;

import javax.inject.Inject;
import javax.inject.Singleton;

import dagger.Provides;
import flow.Flow;
import flow.History;
import flow.path.Path;
import mortar.MortarScope;
import mortar.ViewPresenter;
import rx.functions.Action0;
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
                        .getActionbarController(getView())
                        .showToolbar();
            } else {
                ActionBarService
                        .getActionbarController(getView())
                        .openAppbar();
            }

            setActionBar();
        }
        void setActionBar() {

            ActionBarController.MenuAction menu =
                    new ActionBarController
                            .MenuAction()
                            .setIsSearch();

            ActionBarService
                    .getActionbarController(getView())
                    .setMainImage(collection.getImageUrlFull())
                    .setConfig(new ActionBarController.Config(collection.getTitle(), menu));
        }

        /**
         * hacky
         */
        private boolean hasOnlyVisibleChildren() {
            int numCols = getView().getContext().getResources().getInteger(R.integer.num_cols);
            int orientation = getView().getContext().getResources().getConfiguration().orientation;

            if (orientation == Configuration.ORIENTATION_PORTRAIT) {
                return collection.getApiItems().size() <= numCols * 2;          //4 items usually visible in portrait
            } else if (orientation == Configuration.ORIENTATION_LANDSCAPE) {
                return collection.getApiItems().size() <= numCols;              //3 items usually visible in landscape
            } else {
                return false;
            }

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
