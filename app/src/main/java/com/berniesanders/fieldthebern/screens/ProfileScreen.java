package com.berniesanders.fieldthebern.screens;

import android.os.Bundle;
import android.widget.ListView;
import android.widget.TextView;

import com.berniesanders.fieldthebern.FTBApplication;
import com.berniesanders.fieldthebern.R;
import com.berniesanders.fieldthebern.annotations.Layout;
import com.berniesanders.fieldthebern.dagger.FtbScreenScope;
import com.berniesanders.fieldthebern.dagger.MainComponent;
import com.berniesanders.fieldthebern.models.Rankings;
import com.berniesanders.fieldthebern.models.User;
import com.berniesanders.fieldthebern.mortar.FlowPathBase;
import com.berniesanders.fieldthebern.repositories.RankingsRepo;
import com.berniesanders.fieldthebern.repositories.UserRepo;
import com.berniesanders.fieldthebern.repositories.specs.RankingSpec;
import com.berniesanders.fieldthebern.views.ProfileView;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import flow.Flow;
import mortar.ViewPresenter;
import rx.android.schedulers.AndroidSchedulers;
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
        RankingsRepo rankingsRepo();
    }

    @FtbScreenScope
    static public class Presenter extends ViewPresenter<ProfileView> {

        /**
         * Since the presenter is static it should survive rotation
         */
        private final UserRepo userRepo;

        private final RankingsRepo rankingsRepo;

        @Bind(R.id.full_name)
        TextView fullNameTextView;

        @Bind(R.id.ranking_listview)
        ListView rankingsListView;

        /**
         * When the view is inflated, this presented is automatically injected to the ProfileView
         * Constructor parameters here are injected automatically
         */
        @Inject
        Presenter(UserRepo userRepo, RankingsRepo rankingRepo) {
            this.userRepo = userRepo;
            this.rankingsRepo = rankingRepo;
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
            ProfileView view = this.getView();
            if (view != null) {
                ButterKnife.bind(this, view);
                userRepo.getMe().subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .doOnNext(new Action1<User>() {
                            @Override
                            public void call(User user) {
                                String firstName = user.getData().attributes().getFirstName();
                                String lastName = user.getData().attributes().getLastName();
                                if (fullNameTextView != null) {
                                    fullNameTextView.setText(firstName + " " + lastName);
                                }
                            }
                        }).subscribe();
                rankingsRepo.get(new RankingSpec(RankingSpec.EVERYONE)).subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .doOnNext(new Action1<Rankings>() {
                            @Override
                            public void call(Rankings rankings) {
                                rankingsListView.setAdapter(new RankingAdapter(getView().getContext(), rankings.included(), rankings.data()));
                            }
                        })
                        .doOnError(new Action1<Throwable>() {
                            @Override
                            public void call(Throwable throwable) {
                                Timber.wtf(throwable, "rankings failed");
                            }
                        })
                        .subscribe();
            } else {
                Timber.w("ProfileScreen.onLoad view is unavailable");
            }
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

        @OnClick(R.id.submit_profile_settings)
        void onEditProfileClicked() {
            Flow.get(getView()).set(new ProfileSettingsScreen());
        }
    }
}
