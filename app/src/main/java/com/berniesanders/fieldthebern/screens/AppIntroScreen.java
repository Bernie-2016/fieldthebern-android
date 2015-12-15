package com.berniesanders.fieldthebern.screens;

import android.os.Bundle;

import com.berniesanders.fieldthebern.FTBApplication;
import com.berniesanders.fieldthebern.R;
import com.berniesanders.fieldthebern.annotations.Layout;
import com.berniesanders.fieldthebern.controllers.ActionBarService;
import com.berniesanders.fieldthebern.dagger.FtbScreenScope;
import com.berniesanders.fieldthebern.dagger.MainComponent;
import com.berniesanders.fieldthebern.mortar.FlowPathBase;
import com.berniesanders.fieldthebern.views.AppIntroView;

import javax.inject.Inject;

import mortar.ViewPresenter;
import timber.log.Timber;


@Layout(R.layout.app_intro)
public class AppIntroScreen extends FlowPathBase {


    public AppIntroScreen() {
    }

    @Override
    public Object createComponent() {
        return DaggerAppIntroScreen_Component
                .builder()
                .mainComponent(FTBApplication.getComponent())
                .appIntroModule(new AppIntroModule())
                .build();
    }

    @Override
    public String getScopeName() {
        return AppIntroScreen.class.getName();
    }


    @dagger.Module
    class AppIntroModule {

        public AppIntroModule() {
        }

    }

    @FtbScreenScope
    @dagger.Component(modules = AppIntroModule.class, dependencies = MainComponent.class)
    public interface Component {
        void inject(AppIntroView t);
    }

    @FtbScreenScope
    static public class Presenter extends ViewPresenter<AppIntroView> {

        @Inject
        Presenter() {
        }

        @Override
        protected void onLoad(Bundle savedInstanceState) {
            Timber.v("onLoad");
            setActionBar();
        }

        void setActionBar() {
            ActionBarService
                    .get(getView())
                    .hideToolbar()
                    .closeAppbar()
                    .lockDrawer();
        }

        @Override
        protected void onSave(Bundle outState) {
        }

        @Override
        public void dropView(AppIntroView view) {
            super.dropView(view);
        }

    }
}
