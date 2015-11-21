package com.berniesanders.canvass.screens;

import android.os.Bundle;

import com.berniesanders.canvass.FTBApplication;
import com.berniesanders.canvass.R;
import com.berniesanders.canvass.annotations.Layout;
import com.berniesanders.canvass.controllers.ActionBarController;
import com.berniesanders.canvass.controllers.ActionBarService;
import com.berniesanders.canvass.dagger.FtbScreenScope;
import com.berniesanders.canvass.dagger.MainComponent;
import com.berniesanders.canvass.mortar.FlowPathBase;
import com.berniesanders.canvass.views.TemplateView;

import javax.inject.Inject;

import dagger.Provides;
import mortar.ViewPresenter;
import timber.log.Timber;

/**
 * Example for creating new Mortar Screen that helps explain how it all works.  
 * Set the @Layout annotation to the resource id of the layout for the screen
 */
@Layout(R.layout.screen_template)
public class TemplateScreen extends FlowPathBase {

    private final String someData;

    /**
     * Constructor called by Flow throughout the app
     *
     * Example:
     * Flow.get(context).set(new TemplateScreen("Some Data To Pass");
     */
    public TemplateScreen(String someData) {
        this.someData = someData;
    }

    /**
     * Create the component defined as an inner class below.
     * This component will inject the presenter on the view, and dependencies/module on the presenter.
     * You can pass data (someData) from the Screen to its Presenter through this component.
     * Remember you must run the gradle 'build' class for Dagger to generate to component code
     */
    @Override
    public Object createComponent() {
        return DaggerTemplateScreen_Component
                .builder()
                .mainComponent(FTBApplication.getComponent()) //must set if module has (dependencies = MainComponent.class)
                .templateModule(new TemplateModule(someData)) //pass data to the presenter here
                .build();
    }

    @Override
    public String getScopeName() {
        return TemplateScreen.class.getName();
    }


    @dagger.Module
    class TemplateModule {

        private final String someDataToInject;

        /**
         * pass variables to the component that will then be injected to the presenter
         */
        public TemplateModule(String someDataToInject) {
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
     * Only use "modules = TemplateModule.class" if you need a module
     */
    @FtbScreenScope
    @dagger.Component(modules = TemplateModule.class, dependencies = MainComponent.class)
    public interface Component {
        /**
         * injection target = the view (TemplateView) to have the presented injected on it
         */
        void inject(TemplateView t);

        // Expose anything you want injected to the presenter here
        // such as Gson from the MainComponent

        // Gson exposeGson();
    }

    @FtbScreenScope
    static public class Presenter extends ViewPresenter<TemplateView> {

        /**
         * Since the presenter is static it should survive rotation
         */
        private final String someInjectedData;

        /**
         * When the view is inflated, this presented is automatically injected to the TemplateView
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
        }

//        void setActionBar() {
//            ActionBarController.MenuAction menu =
//                    new ActionBarController
//                            .MenuAction()
//                            .setIsSearch();
//            ActionBarService
//                    .getActionbarController(getView())
//                    .setMainImage(null)
//                    .setConfig(new ActionBarController.ConfigImpl("actionbar title", menu));
//        }

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
         * ((TemplateScreen)Path.get(view.getContext())).somePublicField = "Something you want to save"
         */
        @Override
        public void dropView(TemplateView view) {
            super.dropView(view);
        }

    }
}
