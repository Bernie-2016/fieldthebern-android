package com.berniesanders.fieldthebern.screens;

import android.os.Bundle;

import com.berniesanders.fieldthebern.FTBApplication;
import com.berniesanders.fieldthebern.R;
import com.berniesanders.fieldthebern.annotations.Layout;
import com.berniesanders.fieldthebern.controllers.ActionBarService;
import com.berniesanders.fieldthebern.dagger.FtbScreenScope;
import com.berniesanders.fieldthebern.dagger.MainComponent;
import com.berniesanders.fieldthebern.location.EarlyState;
import com.berniesanders.fieldthebern.mortar.FlowPathBase;
import com.berniesanders.fieldthebern.repositories.FieldOfficeRepo;
import com.berniesanders.fieldthebern.views.EarlyStateView;
import com.berniesanders.fieldthebern.views.ExampleView;

import javax.inject.Inject;

import butterknife.ButterKnife;
import butterknife.OnClick;
import dagger.Provides;
import flow.Flow;
import flow.History;
import mortar.ViewPresenter;
import timber.log.Timber;

/**
 */
@Layout(R.layout.screen_early_state)
public class EarlyStateScreen extends FlowPathBase {


    private final EarlyState earlyState;

    /**
     * @param earlyState
     */
    public EarlyStateScreen(EarlyState earlyState) {
        this.earlyState = earlyState;
    }

    /**
     */
    @Override
    public Object createComponent() {
        return DaggerEarlyStateScreen_Component
                .builder()
                .mainComponent(FTBApplication.getComponent())
                .module(new Module(earlyState))
                .build();
    }

    @Override
    public String getScopeName() {
        return EarlyStateScreen.class.getName();
    }


    @dagger.Module
    class Module {

        private final EarlyState earlyState;

        public Module(EarlyState earlyState) {
            this.earlyState = earlyState;
        }

        @Provides
        @FtbScreenScope
        EarlyState provideEarlyState() {
            return earlyState;
        }
    }

    @FtbScreenScope
    @dagger.Component(dependencies = MainComponent.class, modules = Module.class)
    public interface Component {
        void inject(EarlyStateView t);
    }

    @FtbScreenScope
    static public class Presenter extends ViewPresenter<EarlyStateView> {


        private final EarlyState earlyState;

        /**
         */
        @Inject
        Presenter(EarlyState earlyState) {
            this.earlyState = earlyState;
        }

        /**
         */
        @Override
        protected void onLoad(Bundle savedInstanceState) {
            Timber.v("onLoad");
            ButterKnife.bind(this, getView());
            setActionBar();
        }

        void setActionBar() {
            ActionBarService
                    .get(getView())
                    .hideToolbar();
        }


        /**
         */
        @Override
        public void dropView(EarlyStateView view) {
            super.dropView(view);
            ButterKnife.unbind(this);
        }

        @OnClick(R.id.continueButton)
        void onContinueClick() {
            Flow.get(getView()).setHistory(History.single(new HomeScreen()), Flow.Direction.FORWARD);
        }

    }
}
