package org.feelthebern.android.screens;

import android.content.res.Configuration;
import android.os.Bundle;
import android.os.Parcelable;

import org.feelthebern.android.FTBApplication;
import org.feelthebern.android.R;
import org.feelthebern.android.adapters.CollectionRecyclerAdapter;
import org.feelthebern.android.annotations.Layout;
import org.feelthebern.android.events.ChangePageEvent;
import org.feelthebern.android.models.Collection;
import org.feelthebern.android.mortar.FlowPathBase;
import org.feelthebern.android.views.CollectionView;

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
public class CollectionScreen extends FlowPathBase{

    private final Collection collection;
    public Parcelable savedState;

    public CollectionScreen(Collection collection) {
        this.collection = collection;
    }

    @Override
    public int getLayout() {
        return R.layout.screen_collection;
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

                new ChangePageEvent()
                        .with(FTBApplication.getEventBus())
                        .img(collection.getImageUrlFull())
                        .title(collection.getTitle())
                        .remain(hasOnlyVisibleChildren())
                        //.close(!hasOnlyVisibleChildren())
                        .dispatch();
            } else {
                new ChangePageEvent()
                        .with(FTBApplication.getEventBus())
                        .img(collection.getImageUrlFull())
                        .title(collection.getTitle())
                        .dispatch();
            }
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
