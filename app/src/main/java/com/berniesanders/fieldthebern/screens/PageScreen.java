package com.berniesanders.fieldthebern.screens;

import android.os.Bundle;
import android.os.Parcelable;

import com.berniesanders.fieldthebern.R;
import com.berniesanders.fieldthebern.adapters.PageRecyclerAdapter;
import com.berniesanders.fieldthebern.annotations.Layout;
import com.berniesanders.fieldthebern.dagger.FtbScreenScope;
import com.berniesanders.fieldthebern.models.Page;
import com.berniesanders.fieldthebern.controllers.ActionBarController;
import com.berniesanders.fieldthebern.controllers.ActionBarService;
import com.berniesanders.fieldthebern.mortar.FlowPathBase;
import com.berniesanders.fieldthebern.views.PageView;

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
public class PageScreen extends FlowPathBase {

    private final Page page;

    //hack... see:https://github.com/square/flow/issues/11
    public Parcelable savedState;

    public PageScreen(Page page) {
        this.page = page;
    }

    @Override
    public Object createComponent() {
        return DaggerPageScreen_Component
                .builder()
                .pageModule(new PageModule(page))
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
    }

    @FtbScreenScope
    @dagger.Component(modules = PageModule.class)
    public interface Component {
        void inject(PageView t);
        Page getPage();
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
            setActionBar();
        }

        private void setData() {
            Timber.v("onCompleted page: %s", page.getTitle());
            getView().setAdapter(new PageRecyclerAdapter(page));

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
        }

        void setActionBar() {
            ActionBarController.MenuAction menu =
                    new ActionBarController
                            .MenuAction()
                            .setIsSearch();
            ActionBarService
                    .getActionbarController(getView())
                    .setMainImage(page.getImageUrlFull())
                    .setConfig(new ActionBarController.Config(page.getTitle(), menu));
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
            recyclerViewState = getView().getLayoutManager().onSaveInstanceState();
            outState.putParcelable(BUNDLE_RECYCLER_LAYOUT, recyclerViewState);
            ((PageScreen)Path.get(getView().getContext())).savedState = recyclerViewState;

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

    }
}
