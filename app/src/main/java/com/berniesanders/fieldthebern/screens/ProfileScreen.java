package com.berniesanders.fieldthebern.screens;

import android.os.Bundle;
import android.widget.EditText;

import com.berniesanders.fieldthebern.FTBApplication;
import com.berniesanders.fieldthebern.R;
import com.berniesanders.fieldthebern.annotations.Layout;
import com.berniesanders.fieldthebern.controllers.ToastService;
import com.berniesanders.fieldthebern.dagger.FtbScreenScope;
import com.berniesanders.fieldthebern.dagger.MainComponent;
import com.berniesanders.fieldthebern.models.CreateUserRequest;
import com.berniesanders.fieldthebern.models.User;
import com.berniesanders.fieldthebern.mortar.FlowPathBase;
import com.berniesanders.fieldthebern.repositories.UserRepo;
import com.berniesanders.fieldthebern.repositories.specs.UserSpec;
import com.berniesanders.fieldthebern.views.ProfileView;
import com.crashlytics.android.Crashlytics;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import flow.Flow;
import mortar.ViewPresenter;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action0;
import rx.functions.Action1;
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
            userRepo.getMe().subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .doOnNext(new Action1<User>() {
                        @Override
                        public void call(User user) {
                            String firstName = user.getData().attributes().getFirstName();
                            String lastName = user.getData().attributes().getLastName();
                            firstNameEditText.setText(firstName);
                            lastNameEditText.setText(lastName);
                        }
                    }).subscribe();
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
            Timber.v("Attempting to save profile");
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
                    .observeOn(AndroidSchedulers.mainThread())
                    .doOnCompleted(
                            new Action0() {
                                @Override
                                public void call() {
                                    Timber.v("Profile saved");
                                    ProfileView view = getView();
                                    if (view != null) {
                                        ToastService.get(view).bern(view.getContext().getString(R.string.profile_saved));
                                        Flow.get(view.getContext()).set(new HomeScreen());
                                    } else {
                                        Timber.w("getView() null, cannot notify user of successful profile save");
                                    }
                                }
                            }
                    )
                    .doOnError(
                            new Action1<Throwable>() {
                                @Override
                                public void call(Throwable throwable) {
                                    Timber.e(throwable, "Unable to save profile");
                                    Crashlytics.logException(throwable);
                                    ProfileView view = getView();
                                    if (view != null) {
                                        ToastService.get(view).bern(view.getContext().getString(R.string.error_saving_profile));
                                    } else {
                                        Timber.w("getView() null, cannot notify user of failed profile save");
                                    }
                                }
                            }
                    )
                    .subscribe();
        }
    }
}
