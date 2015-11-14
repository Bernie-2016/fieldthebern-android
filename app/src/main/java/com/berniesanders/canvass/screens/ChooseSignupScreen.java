package com.berniesanders.canvass.screens;

import android.os.Bundle;

import com.berniesanders.canvass.R;
import com.berniesanders.canvass.annotations.Layout;
import com.berniesanders.canvass.dagger.FtbScreenScope;
import com.berniesanders.canvass.controllers.ActionBarController;
import com.berniesanders.canvass.controllers.ActionBarService;
import com.berniesanders.canvass.models.CreateUserAttributes;
import com.berniesanders.canvass.models.CreateUserRequest;
import com.berniesanders.canvass.models.User;
import com.berniesanders.canvass.mortar.FlowPathBase;
import com.berniesanders.canvass.repositories.UserRepo;
import com.berniesanders.canvass.repositories.specs.UserSpec;
import com.berniesanders.canvass.views.ChooseSignupView;
import com.google.gson.Gson;

import javax.inject.Inject;

import butterknife.BindString;
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
 * Example for creating new Mortar Screen that helps explain how it all works
 *
 * Set the @Layout annotation to the resource id of the layout for the screen
 */
@Layout(R.layout.screen_choose_signup)
public class ChooseSignupScreen extends FlowPathBase {

    /**
     */
    public ChooseSignupScreen() {
    }

    /**
     */
    @Override
    public Object createComponent() {
        return DaggerChooseSignupScreen_Component
                .builder()
                .build();
    }

    /**
     */
    @Override
    public String getScopeName() {
        return ChooseSignupScreen.class.getName();
    }


    @dagger.Module
    class Module {
    }

    /**
     */
    @FtbScreenScope
    @dagger.Component
    public interface Component {
        void inject(ChooseSignupView t);
    }

    @FtbScreenScope
    static public class Presenter extends ViewPresenter<ChooseSignupView> {

        @BindString(R.string.signup_title) String screenTitleString;

        UserRepo repo;

        @Inject
        Presenter() {
        }

        @Override
        protected void onLoad(Bundle savedInstanceState) {
            Timber.v("onLoad");
            ButterKnife.bind(this, getView());
            setActionBar();
            CreateUserRequest createUserRequest =
                    new CreateUserRequest()
                            .withAttributes(new CreateUserAttributes
                                            .Builder()
                                            .email("pj2@example.com")
                                            .firstName("pj")
                                            .lastName("c")
                                            .password("123456testpwd")
                                            .lat(40.7127)
                                            .lng(74.0059)
                                            .stateCode("NY")
                                            .build()
                            );
            repo = new UserRepo(new Gson());
            UserSpec spec = new UserSpec().create(createUserRequest);
            repo.create(spec)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .doOnNext(new Action1<User>() {
                        @Override
                        public void call(User user) {
                            Timber.d("user: %s", user.toString());
                        }
                    }).doOnCompleted(new Action0() {
                @Override
                public void call() {
                    Timber.d("createUserRequest done.");
                }
            }).doOnError(new Action1<Throwable>() {
                @Override
                public void call(Throwable throwable) {
                    Timber.e(throwable, "createUserRequest error");
                }
            }).subscribe();
        }


        void setActionBar() {
            ActionBarService
                    .getActionbarController(getView())
                    .showToolbar()
                    .lockDrawer()
                    .closeAppbar()
                    .setMainImage(null)
                    .setConfig(new ActionBarController.Config(screenTitleString, null));
        }

        @Override
        protected void onSave(Bundle outState) {
        }

        @Override
        public void dropView(ChooseSignupView view) {
            super.dropView(view);
            ButterKnife.unbind(this);
        }

        @OnClick(R.id.sign_up_email)
        void signUpEmail() {
            Flow.get(getView().getContext()).set(new SignupScreen());
        }

        @OnClick(R.id.sign_up_facebook)
        void signUpFacebook() {
            Flow.get(getView().getContext()).set(new SignupScreen());
        }

        @OnClick(R.id.have_an_account)
        void haveAccount() {
            Flow.get(getView().getContext()).set(new ChooseLoginScreen());
        }
    }
}
