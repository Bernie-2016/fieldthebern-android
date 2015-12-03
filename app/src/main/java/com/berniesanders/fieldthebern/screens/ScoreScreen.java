package com.berniesanders.fieldthebern.screens;

import android.os.Bundle;

import com.berniesanders.fieldthebern.FTBApplication;
import com.berniesanders.fieldthebern.R;
import com.berniesanders.fieldthebern.annotations.Layout;
import com.berniesanders.fieldthebern.dagger.FtbScreenScope;
import com.berniesanders.fieldthebern.dagger.MainComponent;
import com.berniesanders.fieldthebern.controllers.ActionBarController;
import com.berniesanders.fieldthebern.controllers.ActionBarService;
import com.berniesanders.fieldthebern.models.Score;
import com.berniesanders.fieldthebern.models.VisitResult;
import com.berniesanders.fieldthebern.mortar.FlowPathBase;
import com.berniesanders.fieldthebern.views.ScoreView;

import javax.inject.Inject;

import butterknife.BindString;
import butterknife.ButterKnife;
import dagger.Provides;
import mortar.ViewPresenter;
import timber.log.Timber;

/**
 * Example for creating new Mortar Screen that helps explain how it all works
 *
 * Set the @Layout annotation to the resource id of the layout for the screen
 */
@Layout(R.layout.screen_score)
public class ScoreScreen extends FlowPathBase {

    private final VisitResult visitResult;

    /**
     * @param visitResult
     */
    public ScoreScreen(VisitResult visitResult) {
        this.visitResult = visitResult;
    }

    /**
     */
    @Override
    public Object createComponent() {
        return DaggerScoreScreen_Component
                .builder()
                .mainComponent(FTBApplication.getComponent())
                .module(new Module(visitResult))
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

        private final VisitResult visitResult;

        public Module(VisitResult visitResult) {
            this.visitResult = visitResult;
        }

        @Provides
        @FtbScreenScope
        public VisitResult provideVisitResult() {
            return visitResult;
        }
    }

    /**
     */
    @FtbScreenScope
    @dagger.Component(modules = Module.class, dependencies = MainComponent.class)
    public interface Component {
        void inject(ScoreView t);
        VisitResult visitResult();
    }

    @FtbScreenScope
    static public class Presenter extends ViewPresenter<ScoreView> {

        private final VisitResult visitResult;
        @BindString(R.string.score) String screenTitleString;

        @Inject
        Presenter(VisitResult visitResult) {
            this.visitResult = visitResult;
        }

        @Override
        protected void onLoad(Bundle savedInstanceState) {
            Timber.v("onLoad");
            ButterKnife.bind(this, getView());
            setActionBar();

            Score score = visitResult.data().relationships().score();
            int points = score.attributes().pointsForKnock() + score.attributes().pointsForUpdates();
            getView().animateScore(points);
            getView().animateLabels(
                    score.attributes().pointsForKnock(),
                    score.attributes().pointsForUpdates(),
                    visitResult.data().relationships().user().getData().attributes().getFirstName()); //looks like swift?
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
