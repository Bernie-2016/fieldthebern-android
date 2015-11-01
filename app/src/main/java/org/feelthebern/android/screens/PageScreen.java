package org.feelthebern.android.screens;

import android.os.Bundle;

import com.google.gson.Gson;

import org.feelthebern.android.FTBApplication;
import org.feelthebern.android.R;
import org.feelthebern.android.adapters.PageRecyclerAdapter;
import org.feelthebern.android.annotations.Layout;
import org.feelthebern.android.dagger.FtbScreenScope;
import org.feelthebern.android.dagger.MainComponent;
import org.feelthebern.android.events.ChangePageEvent;
import org.feelthebern.android.models.Content;
import org.feelthebern.android.models.Page;
import org.feelthebern.android.mortar.FlowPathBase;
import org.feelthebern.android.repositories.PageRepo;
import org.feelthebern.android.repositories.specs.PageSpec;
import org.feelthebern.android.views.PageView;

import java.util.List;

import javax.inject.Inject;

import dagger.Module;
import dagger.Provides;
import mortar.MortarScope;
import mortar.ViewPresenter;
import rx.Observer;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import timber.log.Timber;

/**
 *
 */
@Layout(R.layout.screen_page)
public class PageScreen extends FlowPathBase{

    private final PageSpec pageSpec;

    public PageScreen(Page page) {
        this.pageSpec = new PageSpec(page);
    }

    @Override
    public int getLayout() {
        return R.layout.screen_page;
    }

    @Override
    public Object createComponent() {
        return DaggerPageScreen_Component
                .builder()
                .pageModule(new PageModule(pageSpec))
                .mainComponent(FTBApplication.getComponent())
                .build();
    }

    @Override
    public String getScopeName() {
        return PageScreen.class.getName();// TODO temp scope name?
    }


    @Module
    class PageModule {
        private final PageSpec p;

        public PageModule(PageSpec p) {
            this.p = p;
        }

        @Provides
        public PageSpec providePageSpec() {
            return p;
        }
    }

    @FtbScreenScope
    @dagger.Component(dependencies = MainComponent.class, modules = PageModule.class)
    public interface Component {
        void inject(PageView t);
        PageSpec getPageSpec();
        PageRepo pageRepo();
        Gson gson();
    }

    @FtbScreenScope
    static public class Presenter extends ViewPresenter<PageView> {

        final PageSpec pageSpec;
        Page page;
        final Gson gson;
        final PageRepo repo;
        Subscription subscription;

        @Inject
        Presenter(PageRepo repo, PageSpec pageSpec, Gson gson) {
            this.repo = repo;
            this.pageSpec = pageSpec;
            this.gson = gson;
            this.page = pageSpec.page();
        }



        @Override
        protected void onLoad(Bundle savedInstanceState) {
            Timber.v("onLoad");
            if (savedInstanceState != null) {
                page = savedInstanceState.getParcelable(Page.PAGE_PARCEL);
                Timber.v("onLoad savedInstanceState page: %s", page);
            }


            //if (page.getContent() == null) {
                Timber.v("PageRepo loading");
                //getView().showLoadingAnimation();

                subscription = repo.get(pageSpec)
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribeOn(Schedulers.io())
                        .subscribe(observer);
            //}
        }

        Observer<List<Content>> observer = new Observer<List<Content>>() {
            @Override
            public void onCompleted() {

                Timber.v("onLoad page: %s", page.getImageUrlFull());
                getView().setAdapter(new PageRecyclerAdapter(page));

                new ChangePageEvent()
                        .with(FTBApplication.getEventBus())
                        .img(page.getImageUrlFull())
                        .title(page.getTitle())
                        .dispatch();

                //getView().hideLoadingAnimation();
            }

            @Override
            public void onError(Throwable e) {
                Timber.e(e, "Main presenter error in observer/rx");
            }

            @Override
            public void onNext(List<Content> contentList) {
                page.setContent(contentList);
                Timber.v("page repo returned the contentList");
            }
        };


        @Override
        protected void onSave(Bundle outState) {
            outState.putParcelable(Page.PAGE_PARCEL, page);
        }

        @Override
        public void dropView(PageView view) {
            super.dropView(view);
        }

        @Override
        protected void onEnterScope(MortarScope scope) {
            super.onEnterScope(scope);
            Timber.v("onEnterScope: %s", scope);
        }
    }
}
