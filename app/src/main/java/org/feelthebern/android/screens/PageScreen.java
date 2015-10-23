package org.feelthebern.android.screens;

import android.os.Bundle;

import org.feelthebern.android.R;
import org.feelthebern.android.models.Page;
import org.feelthebern.android.mortar.HasComponent;
import org.feelthebern.android.mortar.Layout;
import org.feelthebern.android.views.PageView;

import javax.inject.Inject;
import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import mortar.ViewPresenter;
import timber.log.Timber;

/**
 *
 */
@Layout(R.layout.screen_page)
public class PageScreen implements HasComponent{

    private final Page page;

    public PageScreen(Page page) {
        this.page = page;
    }

    public Object getComponent() {
        return DaggerPageScreen_Component
                .builder()
                .pageModule(new PageModule(page))
                .build();
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

    @Singleton
    @dagger.Component(modules = PageModule.class)
    public interface Component {
        void inject(PageView t);
        Page getPage();
    }

    @Singleton
    static public class Presenter extends ViewPresenter<PageView> {

        private final Page page;

        @Inject
        Presenter(Page page) {
            this.page = page;
        }


        @Override
        protected void onLoad(Bundle savedInstanceState) {
            Timber.v("onLoad page: %s", page.getTitle());
        }


        @Override
        protected void onSave(Bundle outState) {
        }

        @Override
        public void dropView(PageView view) {
        }
    }
}
