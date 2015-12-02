package com.berniesanders.fieldthebern.screens;

import android.os.Bundle;

import com.berniesanders.fieldthebern.R;
import com.berniesanders.fieldthebern.annotations.Layout;
import com.berniesanders.fieldthebern.dagger.FtbScreenScope;
import com.berniesanders.fieldthebern.controllers.ActionBarController;
import com.berniesanders.fieldthebern.controllers.ActionBarService;
import com.berniesanders.fieldthebern.models.ApiAddress;
import com.berniesanders.fieldthebern.mortar.FlowPathBase;
import com.berniesanders.fieldthebern.views.NewVisitView;

import javax.inject.Inject;

import butterknife.BindString;
import butterknife.ButterKnife;
import flow.Flow;
import flow.History;
import mortar.ViewPresenter;
import rx.functions.Action0;
import timber.log.Timber;

/**
 * Example for creating new Mortar Screen that helps explain how it all works
 *
 * Set the @Layout annotation to the resource id of the layout for the screen
 */
@Layout(R.layout.screen_new_visit)
public class NewVisitScreen extends FlowPathBase {


    private final ApiAddress apiAddress;

    /**
     */
    public NewVisitScreen(ApiAddress apiAddress) {
        this.apiAddress = apiAddress;
    }

    /**
     * Create the component defined as an inner class below.
     * This component will inject the presenter on the view, and dependencies/module on the presenter.
     * You can pass data (someData) from the Screen to its Presenter through this component.
     * Remember you must run the gradle 'build' class for Dagger to generate to component code
     */
    @Override
    public Object createComponent() {
        return DaggerNewVisitScreen_Component
                .builder()
                .build();
    }

    /**
     * Important: make this unique to the data you want cages or displayed.
     * If you show "someData" then add "someData.hashCode()" to the end of this method
     * This will define the uniqueness that mortar and flow will see this screen for comparisons
     */
    @Override
    public String getScopeName() {
        return NewVisitScreen.class.getName();
    }


//    @dagger.Module
//    class Module {
//    }

    /**
     */
    @FtbScreenScope
    @dagger.Component
    public interface Component {
        void inject(NewVisitView t);

    }

    @FtbScreenScope
    static public class Presenter extends ViewPresenter<NewVisitView> {

        @BindString(android.R.string.cancel) String cancel;

        @BindString(R.string.new_visit) String newVisit;

        @Inject
        Presenter() {
        }

        @Override
        protected void onLoad(Bundle savedInstanceState) {
            Timber.v("onLoad");
            ButterKnife.bind(this, getView());
            setActionBar();
        }


        void setActionBar() {
            ActionBarController.MenuAction menu =
                    new ActionBarController.MenuAction()
                            .label(cancel)
                            .action(new Action0() {
                                @Override
                                public void call() {
                                    Flow.get(getView()).setHistory(History.single(new Main()), Flow.Direction.BACKWARD);
                                }
                            });
            ActionBarService
                    .getActionbarController(getView())
                    .showToolbar()
                    .closeAppbar()
                    .setMainImage(null)
                    .setConfig(new ActionBarController.Config(newVisit, menu));
        }

        @Override
        protected void onSave(Bundle outState) {
        }

        @Override
        public void dropView(NewVisitView view) {
            super.dropView(view);
        }

    }
}
