package com.berniesanders.fieldthebern.screens;

import android.os.Bundle;

import com.berniesanders.fieldthebern.FTBApplication;
import com.berniesanders.fieldthebern.R;
import com.berniesanders.fieldthebern.annotations.Layout;
import com.berniesanders.fieldthebern.dagger.FtbScreenScope;
import com.berniesanders.fieldthebern.dagger.MainComponent;
import com.berniesanders.fieldthebern.mortar.FlowPathBase;
import com.berniesanders.fieldthebern.repositories.UserRepo;
import com.berniesanders.fieldthebern.views.ProfileSettingsView;

import javax.inject.Inject;

import butterknife.ButterKnife;
import butterknife.OnClick;
import flow.Flow;
import mortar.ViewPresenter;
import timber.log.Timber;

/**
 * Profile Screen for updating user profiles
 */
@Layout(R.layout.screen_profile_settings)
public class ProfileSettingsScreen extends FlowPathBase {

    /**
     * Constructor called by Flow throughout the app
     * <p/>
     * Example:
     * Flow.get(context).set(new ExampleScreen("Some Data To Pass");
     * <p/>
     * Note:
     * Generally common types like "String" are not injected because injection works based on type
     */
    public ProfileSettingsScreen() {
    }

    /**
     * Create the component defined as an inner class below.
     * This component will inject the presenter on the view, and dependencies/module on the presenter.
     * You can pass data (someData) from the Screen to its Presenter through this component.
     * Remember you must run the gradle 'build' class for Dagger to generate to component code
     * <p/>
     * Note:
     * Generally common types like "String" are not injected because injection works based on type
     */
    @Override
    public Object createComponent() {
        return DaggerProfileSettingsScreen_Component
                .builder()
                .mainComponent(FTBApplication.getComponent()) //must set if module has (dependencies = MainComponent.class)
                .profileSettingsModule(new ProfileSettingsModule()) //pass data to the presenter here
                .build();
    }

    @Override
    public String getScopeName() {
        return ProfileSettingsScreen.class.getName();
    }


    @dagger.Module
    class ProfileSettingsModule {

        /**
         * pass variables to the component that will then be injected to the presenter
         */
        public ProfileSettingsModule() {
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
    @dagger.Component(modules = ProfileSettingsModule.class, dependencies = MainComponent.class)
    public interface Component {
        /**
         * injection target = the view (ProfileSettingsView) to have the presented injected on it
         */
        void inject(ProfileSettingsView t);

        // Expose UserRepo through injection
        @SuppressWarnings("unused")
        UserRepo userRepo();
    }

    @FtbScreenScope
    static public class Presenter extends ViewPresenter<ProfileSettingsView> {
        private final UserRepo userRepo;

        /**
         * When the view is inflated, this presented is automatically injected to the ProfileEditView
         * Constructor parameters here are injected automatically
         */
        @Inject
        Presenter(UserRepo userRepo) {
            this.userRepo = userRepo;
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
         * <p/>
         * ((ProfileEditView)Path.get(view.getContext())).somePublicField = "Something you want to save"
         */
        @Override
        public void dropView(ProfileSettingsView view) {
            super.dropView(view);
            ButterKnife.unbind(this);
        }

        @OnClick(R.id.submit_edit_profile)
        void onEditProfileClicked() {
            Flow.get(getView()).set(new ProfileEditScreen());
        }

        @OnClick(R.id.submit_logout)
        void onLogoutClicked() {
            userRepo.logout();
            Flow.get(getView()).set(new ChooseSignupScreen());
        }
    }
}
