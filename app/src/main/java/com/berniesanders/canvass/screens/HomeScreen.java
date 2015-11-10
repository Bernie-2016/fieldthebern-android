package com.berniesanders.canvass.screens;

import android.os.Bundle;

import com.berniesanders.canvass.R;
import com.berniesanders.canvass.annotations.Layout;
import com.berniesanders.canvass.dagger.FtbScreenScope;
import com.berniesanders.canvass.mortar.FlowPathBase;
import com.berniesanders.canvass.views.HomeView;

import javax.inject.Inject;

import butterknife.ButterKnife;
import butterknife.OnClick;
import flow.Flow;
import mortar.ViewPresenter;
import timber.log.Timber;

/**
 * Created by Patrick on 11/9/2015.
 */
@Layout(R.layout.screen_home)
public class HomeScreen extends FlowPathBase {
    /**
     * Constructor called by Flow throughout the app
     * <p/>
     * Example:
     * Flow.get(context).set(new TemplateScreen("Some Data To Pass");
     */
    public HomeScreen() {
    }

    /**
     * Create the component defined as an inner class below.
     * <p/>
     * This component will inject the presenter on the view, and dependencies/module on the presenter.
     * <p/>
     * You can pass data (someData) from the Screen to its Presenter through this component.
     * <p/>
     * Remember you must run the gradle 'build' class for Dagger to generate to component code
     */
    @Override
    public Object createComponent() {
        return DaggerHomeScreen_Component
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
        // note someData.hashCode() makes the screen unique
        return HomeScreen.class.getName();
    }

    /**
     * This component is used to inject the view with the presenter once the view is inflated.
     * <p/>
     * The view will injected itself using this component on inflate.
     * <p/>
     * Expose anything you want injected to the presenter here
     * <p/>
     * Only use "dependencies = MainComponent.class" if you need something from the main component
     * Only use "modules = TemplateModule.class" if you need a module
     */
    @FtbScreenScope
    @dagger.Component()
    public interface Component {
        /**
         * injection target = the view (TemplateView) to have the presented injected on it
         */
        void inject(HomeView t);

        // Expose anything you want injected to the presenter here
        // such as Gson from the MainComponent

        // Gson exposeGson();
    }

    @FtbScreenScope
    static public class Presenter extends ViewPresenter<HomeView> {


        /**
         * When the view is inflated, this presented is automatically injected to the TemplateView
         * Constructor parameters here are injected automatically
         */
        @Inject
        Presenter() {
        }

        /**
         * called when the presenter and view are ready.
         * getView() will not be null
         *
         * @param savedInstanceState This bundle is only passed on rotation not passed on navigating back
         */
        @Override
        protected void onLoad(Bundle savedInstanceState) {
            Timber.v("onLoad");
            ButterKnife.bind(this, getView());
            // Some example Page events.

//            new ChangePageEvent()
//                    .with(FTBApplication.getEventBus())
//                    .close(true)
//                    .hideToolbar(true)
//                    .dispatch();

//            new ShowToolbarEvent()
//                    .with(FTBApplication.getEventBus())
//                    .showToolbar(true)
//                    .dispatch();
        }

        /**
         * called on rotation only
         */
        @Override
        protected void onSave(Bundle outState) {
        }


        /**
         * last chance at the view before it is detached
         * <p/>
         * You can save state with hack, (restore it the same way by reading the field).
         * objects saved with be "parceled" by gson. Example:
         * <p/>
         * ((TemplateScreen)Path.get(view.getContext())).somePublicField = "Something you want to save"
         */
        @Override
        public void dropView(HomeView view) {
            super.dropView(view);
        }

        @OnClick(R.id.screen_home_canvass)
        void onCanvassClicked() {
            Flow.get(getView().getContext()).set(new MapScreen());
        }

        @OnClick(R.id.screen_home_issues)
        void onIssuesClicked() {
            Flow.get(getView().getContext()).set(new Main());
        }

    }
}
