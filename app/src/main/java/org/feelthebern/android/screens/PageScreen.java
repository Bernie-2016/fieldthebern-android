package org.feelthebern.android.screens;

import android.os.Bundle;
import android.os.Parcelable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.google.gson.Gson;

import org.feelthebern.android.FTBApplication;
import org.feelthebern.android.R;
import org.feelthebern.android.adapters.PageRecyclerAdapter;
import org.feelthebern.android.annotations.Layout;
import org.feelthebern.android.dagger.FtbScreenScope;
import org.feelthebern.android.dagger.MainComponent;
import org.feelthebern.android.events.ChangePageEvent;
import org.feelthebern.android.models.Page;
import org.feelthebern.android.mortar.FlowPathBase;
import org.feelthebern.android.views.PageView;


import javax.inject.Inject;

import dagger.Module;
import dagger.Provides;
import flow.path.Path;
import mortar.MortarScope;
import mortar.ViewPresenter;
import timber.log.Timber;

/**
 *
 */
@Layout(R.layout.screen_page)
public class PageScreen extends FlowPathBase{

    private final Page page;

    //hack... see:https://github.com/square/flow/issues/11
    public Parcelable savedState;

    public PageScreen(Page page) {
        this.page = page;
    }

    @Override
    public int getLayout() {
        return R.layout.screen_page;
    }

    @Override
    public Object createComponent() {
        return DaggerPageScreen_Component
                .builder()
                .pageModule(new PageModule(page))
                .mainComponent(FTBApplication.getComponent())
                .build();
    }

    @Override
    public String getScopeName() {
        return PageScreen.class.getName() + page.getTitle();// TODO temp scope name?
    }


    @Module
    class PageModule {
        private final Page p;

        public PageModule(Page p) {
            this.p = p;
        }

        @Provides
        public Page providePage() {
            return p;
        }

        @Provides
        Presenter providePresenter() {
            return new Presenter(p);
        }
    }

    @FtbScreenScope
    @dagger.Component(dependencies = MainComponent.class, modules = PageModule.class)
    public interface Component {
        void inject(PageView t);
        Page getPage();
        Presenter getPresenter();
    }

    @FtbScreenScope
    static public class Presenter extends ViewPresenter<PageView> {

        final Page page;
        Parcelable recyclerViewState;


        private static final String BUNDLE_RECYCLER_LAYOUT = "PageScreen.recycler.layout";

        @Inject
        Presenter(Page page) {
            this.page = page;
        }



        @Override
        protected void onLoad(Bundle savedInstanceState) {
            Timber.v("onLoad");
            super.onLoad(savedInstanceState);

            try {
                if (savedInstanceState != null) {
                    recyclerViewState = savedInstanceState.getParcelable(BUNDLE_RECYCLER_LAYOUT);
                } else {
                    recyclerViewState = ((PageScreen)Path.get(getView().getContext())).savedState;
                }
            } catch (Exception e) {
                Timber.e(e, "Error loading parcels");
            }

            setData();
        }

        private void setData() {
            Timber.v("onCompleted page: %s", page.getTitle());
            getView().setAdapter(new PageRecyclerAdapter(page));

            if (recyclerViewState!=null) {
                getView().getLayoutManager().onRestoreInstanceState(recyclerViewState);

                new ChangePageEvent()
                        .with(FTBApplication.getEventBus())
                        .img(page.getImageUrlFull())
                        .title(page.getTitle())
                        .remain(true)
                        .dispatch();
            } else {
                new ChangePageEvent()
                        .with(FTBApplication.getEventBus())
                        .img(page.getImageUrlFull())
                        .title(page.getTitle())
                        .dispatch();
            }
        }


        /**
         * Called on rotation only
         */
        @Override
        protected void onSave(Bundle outState) {
            saveState(outState);
        }

        private void saveState(Bundle outState) {
            if (getView()==null) { return; }
            if (getView().getLayoutManager()==null) { return; }
            outState.putParcelable(BUNDLE_RECYCLER_LAYOUT, getView().getLayoutManager().onSaveInstanceState());
            ((PageScreen)Path.get(getView().getContext())).savedState = getView().getLayoutManager().onSaveInstanceState();
        }

        /**
         * Called on navigation
         */
        @Override
        public void dropView(PageView view) {
            saveState(new Bundle());
            super.dropView(view);
        }

        @Override
        protected void onEnterScope(MortarScope scope) {
            super.onEnterScope(scope);
            Timber.v("onEnterScope: %s", scope);
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (!(o instanceof Presenter)) return false;

            Presenter presenter = (Presenter) o;

            return !(page != null ? !page.equals(presenter.page) : presenter.page != null);

        }

        @Override
        public int hashCode() {
            return page != null ? page.hashCode() : 0;
        }
    }
}
