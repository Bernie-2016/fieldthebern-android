package com.berniesanders.canvass.screens;

import android.os.Bundle;

import com.berniesanders.canvass.FTBApplication;
import com.berniesanders.canvass.R;
import com.berniesanders.canvass.annotations.Layout;
import com.berniesanders.canvass.dagger.FtbScreenScope;
import com.berniesanders.canvass.dagger.MainComponent;
import com.berniesanders.canvass.mortar.ActionBarController;
import com.berniesanders.canvass.mortar.ActionBarService;
import com.berniesanders.canvass.mortar.FlowPathBase;
import com.berniesanders.canvass.views.ScoreView;

import javax.inject.Inject;

import butterknife.BindString;
import butterknife.ButterKnife;
import mortar.ViewPresenter;
import timber.log.Timber;

/**
 * Example for creating new Mortar Screen that helps explain how it all works
 *
 * Set the @Layout annotation to the resource id of the layout for the screen
 */
@Layout(R.layout.screen_score)
public class ScoreScreen extends FlowPathBase {

    /**
     */
    public ScoreScreen() {
    }

    /**
     */
    @Override
    public Object createComponent() {
        return DaggerScoreScreen_Component
                .builder()
                .mainComponent(FTBApplication.getComponent())
                .build();
    }

    /**
     */
    @Override
    public String getScopeName() {
        return ScoreScreen.class.getName();
    }


    @dagger.Module
    class Module {
    }

    /**
     */
    @FtbScreenScope
    @dagger.Component(dependencies = MainComponent.class)
    public interface Component {
        void inject(ScoreView t);
    }

    @FtbScreenScope
    static public class Presenter extends ViewPresenter<ScoreView> {

        @BindString(R.string.score) String screenTitleString;

        @Inject
        Presenter() {
        }

        @Override
        protected void onLoad(Bundle savedInstanceState) {
            Timber.v("onLoad");
            ButterKnife.bind(this, getView());
            setActionBar();
            getView().animateScore();
            getView().animateLabels();
        }


        void setActionBar() {
            ActionBarService
                    .getActionbarController(getView())
                    .showToolbar()
                    .closeAppbar()
                    .setMainImage(null)
                    .setConfig(new ActionBarController.Config(screenTitleString, null));
        }

        @Override
        protected void onSave(Bundle outState) {
        }

        @Override
        public void dropView(ScoreView view) {
            super.dropView(view);
        }

    }
}
