package com.berniesanders.fieldthebern.screens;

import android.os.Bundle;
import android.widget.EditText;

import com.berniesanders.fieldthebern.FTBApplication;
import com.berniesanders.fieldthebern.R;
import com.berniesanders.fieldthebern.annotations.Layout;
import com.berniesanders.fieldthebern.dagger.FtbScreenScope;
import com.berniesanders.fieldthebern.dagger.MainComponent;
import com.berniesanders.fieldthebern.models.CreateUserRequest;
import com.berniesanders.fieldthebern.models.User;
import com.berniesanders.fieldthebern.mortar.FlowPathBase;
import com.berniesanders.fieldthebern.repositories.UserRepo;
import com.berniesanders.fieldthebern.repositories.specs.UserSpec;
import com.berniesanders.fieldthebern.views.ProfileView;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import mortar.ViewPresenter;
import rx.Observable;
import rx.schedulers.Schedulers;
import timber.log.Timber;

/**
 * Profile Screen for updating user profiles
 */
@Layout(R.layout.screen_profile)
public class ProfileScreen extends FlowPathBase {

    /**
     * Constructor called by Flow throughout the app
     * <p/>
     * Example:
     * Flow.get(context).set(new ExampleScreen("Some Data To Pass");
     * <p/>
     * Note:
     * Generally common types like "String" are not injected because injection works based on type
     */
    public ProfileScreen() {
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
        return DaggerProfileScreen_Component
                .builder()
                .mainComponent(FTBApplication.getComponent()) //must set if module has (dependencies = MainComponent.class)
                .profileModule(new ProfileModule()) //pass data to the presenter here
                .build();
    }

    @Override
    public String getScopeName() {
        return ProfileScreen.class.getName();
    }


    @dagger.Module
    class ProfileModule {

        /**
         * pass variables to the component that will then be injected to the presenter
         */
        public ProfileModule() {
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
    @dagger.Component(modules = ProfileModule.class, dependencies = MainComponent.class)
    public interface Component {
        /**
         * injection target = the view (ProfileView) to have the presented injected on it
         */
        void inject(ProfileView t);

        // Expose UserRepo through injection
        @SuppressWarnings("unused")
        UserRepo userRepo();
    }

    @FtbScreenScope
    static public class Presenter extends ViewPresenter<ProfileView> {

        /**
         * Since the presenter is static it should survive rotation
         */
        private final UserRepo userRepo;

        @Bind(R.id.first_name)
        EditText firstNameEditText;

        @Bind(R.id.last_name)
        EditText lastNameEditText;


        /**
         * When the view is inflated, this presented is automatically injected to the ProfileView
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
         * ((ProfileView)Path.get(view.getContext())).somePublicField = "Something you want to save"
         */
        @Override
        public void dropView(ProfileView view) {
            super.dropView(view);
            ButterKnife.unbind(this);
        }

        @OnClick(R.id.submit_profile)
        void onSaveProfileClicked() {
            String firstName = firstNameEditText.getText().toString();
            String lastName = lastNameEditText.getText().toString();
            UserSpec spec = new UserSpec();
            User user = new User();
            user.getData().attributes().firstName(firstName);
            user.getData().attributes().lastName(lastName);
            spec.create(new CreateUserRequest());
            spec.update(user);
            userRepo.update(spec)
                    .subscribeOn(Schedulers.io())
                    .subscribe();
        }
    }
}
