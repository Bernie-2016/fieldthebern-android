package com.berniesanders.fieldthebern;

import android.os.Bundle;

import com.berniesanders.fieldthebern.annotations.Layout;
import com.berniesanders.fieldthebern.controllers.ActionBarController;
import com.berniesanders.fieldthebern.controllers.ActionBarService;
import com.berniesanders.fieldthebern.dagger.FtbScreenScope;
import com.berniesanders.fieldthebern.dagger.MainComponent;
import com.berniesanders.fieldthebern.mortar.FlowPathBase;

import javax.inject.Inject;

import dagger.Provides;
import mortar.ViewPresenter;
import timber.log.Timber;


@Layout(R.layout.app_intro)
public class AppIntroScreen extends FlowPathBase {

    private final String someData;

    public AppIntroScreen(String someData) {
        this.someData = someData;
    }

    /**
     * Create the component defined as an inner class below.
     * This component will inject the presenter on the view, and dependencies/module on the presenter.
     * You can pass data (someData) from the Screen to its Presenter through this component.
     * Remember you must run the gradle 'build' class for Dagger to generate to component code
     *
     * Note:
     * Generally common types like "String" are not injected because injection works based on type
     */
    @Override
    public Object createComponent() {
        return DaggerAppIntroScreen_Component
                .builder()
                .mainComponent(FTBApplication.getComponent()) //must set if module has (dependencies = MainComponent.class)
                .appIntroModule(new AppIntroModule(someData)) //pass data to the presenter here
                .build();
    }

    @Override
    public String getScopeName() {
        return AppIntroScreen.class.getName();
    }


    @dagger.Module
    class AppIntroModule {

        private final String someDataToInject;

        /**
         * pass variables to the component that will then be injected to the presenter
         */
        public AppIntroModule(String someDataToInject) {
            this.someDataToInject = someDataToInject;
        }

        @Provides
        String provideSomeData() {
            return someDataToInject;
        }
    }

    /**
     * This component is used to inject the view with the presenter once the view is inflated.
     * The view will injected itself using this component on inflate.
     * Expose anything you want injected to the presenter here
     * Only use "dependencies = MainComponent.class" if you need something from the main component
     * Only use "modules = ExampleModule.class" if you need a module
     */
    @FtbScreenScope
    @dagger.Component(modules = AppIntroModule.class, dependencies = MainComponent.class)
    public interface Component {
        /**
         * injection target = the view (ExampleView) to have the presented injected on it
         */
        void inject(AppIntroView t);

    }

    @FtbScreenScope
    static public class Presenter extends ViewPresenter<AppIntroView> {

        /**
         * Since the presenter is static it should survive rotation
         */
        private final String someInjectedData;

        /**
         * When the view is inflated, this presented is automatically injected to the ExampleView
         * Constructor parameters here are injected automatically
         */
        @Inject
        Presenter(String someInjectedData) {
            this.someInjectedData = someInjectedData;
        }

        /**
         * called when the presenter and view are ready.
         * getView() will not be null
         *
         * @param savedInstanceState  This bundle is only passed on rotation not passed on navigating back
         */
        @Override
        protected void onLoad(Bundle savedInstanceState) {
            Timber.v("onLoad");
            setActionBar();
        }

        void setActionBar() {
            ActionBarService
                    .getActionbarController(getView())
                    .showToolbar()
                    .closeAppbar()
                    .lockDrawer()
                    .setMainImage(null)
                    .setConfig(new ActionBarController.Config(someInjectedData, null));
        }

        /**
         * Called on rotation only
         */
        @Override
        protected void onSave(Bundle outState) {
        }


        /**
         * Last chance at the view before it is detached.
         * You can save state with hack, (restore it the same way by reading the field).
         * objects saved with be "parceled" by gson. Example:
         *
         * ((ExampleScreen)Path.get(view.getContext())).somePublicField = "Something you want to save"
         */
        @Override
        public void dropView(AppIntroView view) {
            super.dropView(view);
        }

    }
}