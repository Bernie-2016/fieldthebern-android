package org.feelthebern.android.screens;

import android.os.Bundle;

import org.feelthebern.android.FTBApplication;
import org.feelthebern.android.R;
import org.feelthebern.android.adapters.CollectionRecyclerAdapter;
import org.feelthebern.android.adapters.PageRecyclerAdapter;
import org.feelthebern.android.annotations.Layout;
import org.feelthebern.android.events.ChangePageEvent;
import org.feelthebern.android.models.Collection;
import org.feelthebern.android.models.Page;
import org.feelthebern.android.mortar.FlowPathBase;
import org.feelthebern.android.views.CollectionView;
import org.feelthebern.android.views.PageView;

import javax.inject.Inject;
import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import mortar.MortarScope;
import mortar.ViewPresenter;
import timber.log.Timber;

/**
 *
 */
@Layout(R.layout.screen_collection)
public class CollectionScreen extends FlowPathBase{

    private final Collection collection;

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
        return CollectionScreen.class.getName();// TODO temp scope name?
    }


    @dagger.Module
    class Module {
        private final Collection collection;

        public Module(Collection collection) {
            this.collection = collection;
        }

        @Provides
        public Collection providePage() {
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

        private Collection collection;

        @Inject
        Presenter(Collection collection) {
            this.collection = collection;
        }


        @Override
        protected void onLoad(Bundle savedInstanceState) {

            Timber.v("onLoad passed collection: %s", collection);

            if (collection == null) {
                collection = savedInstanceState.getParcelable(Collection.COLLECTION_PARCEL);
                Timber.v("onLoad savedInstanceState collection: %s", collection);
            }

            Timber.v("onLoad collection: %s", collection.getTitle());
            getView().setAdapter(new CollectionRecyclerAdapter(collection));

            new ChangePageEvent()
                    .with(FTBApplication.getEventBus())
                    .img(collection.getImageUrlFull())
                    .title(collection.getTitle())
                    .dispatch();
        }


        @Override
        protected void onSave(Bundle outState) {
            outState.putParcelable(Collection.COLLECTION_PARCEL, collection);
        }

        @Override
        public void dropView(CollectionView view) {
            super.dropView(view);
        }

        @Override
        protected void onEnterScope(MortarScope scope) {
            super.onEnterScope(scope);
            Timber.v("onEnterScope: %s", scope);
        }
    }
}
