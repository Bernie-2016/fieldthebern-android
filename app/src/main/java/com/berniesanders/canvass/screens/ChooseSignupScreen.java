package com.berniesanders.canvass.screens;

import android.os.Bundle;
import android.widget.Toast;

import com.berniesanders.canvass.FTBApplication;
import com.berniesanders.canvass.R;
import com.berniesanders.canvass.annotations.Layout;
import com.berniesanders.canvass.controllers.ActionBarController;
import com.berniesanders.canvass.controllers.ActionBarService;
import com.berniesanders.canvass.controllers.ErrorToastService;
import com.berniesanders.canvass.dagger.FtbScreenScope;
import com.berniesanders.canvass.dagger.MainComponent;
import com.berniesanders.canvass.models.ApiError;
import com.berniesanders.canvass.models.CreateUserAttributes;
import com.berniesanders.canvass.models.CreateUserRequest;
import com.berniesanders.canvass.models.LoginEmailRequest;
import com.berniesanders.canvass.models.User;
import com.berniesanders.canvass.mortar.FlowPathBase;
import com.berniesanders.canvass.repositories.TokenRepo;
import com.berniesanders.canvass.repositories.UserRepo;
import com.berniesanders.canvass.repositories.specs.TokenSpec;
import com.berniesanders.canvass.repositories.specs.UserSpec;
import com.berniesanders.canvass.views.ChooseSignupView;
import com.google.gson.Gson;

import java.io.IOException;
import java.util.Map;

import javax.inject.Inject;

import butterknife.BindString;
import butterknife.ButterKnife;
import butterknife.OnClick;
import flow.Flow;
import mortar.ViewPresenter;
import retrofit.HttpException;
import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
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
                .mainComponent(FTBApplication.getComponent())
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
    @dagger.Component(dependencies = MainComponent.class)
    public interface Component {
        void inject(ChooseSignupView t);
        UserRepo userRepo();
        TokenRepo tokenRepo();
    }

    @FtbScreenScope
    static public class Presenter extends ViewPresenter<ChooseSignupView> {

        @BindString(R.string.signup_title) String screenTitleString;

        private final TokenRepo tokenRepo;
        private final UserRepo repo;

        @Inject
        Presenter(UserRepo repo, TokenRepo tokenRepo) {
            this.tokenRepo = tokenRepo;
            this.repo = repo;
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
                                            .email("pj6@example.com")
                                            .firstName("pj")
                                            .lastName("c")
                                            .password("123456testpwd")
                                            .lat(40.7127)
                                            .lng(74.0059)
                                            .stateCode("NY")
                                            .build()
                            );
            final UserSpec spec = new UserSpec().create(createUserRequest);
            repo.create(spec)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(observer);
        }

        Observer<User> observer = new Observer<User>() {
            @Override
            public void onCompleted() {
                Timber.d("createUserRequest done.");
            }

            @Override
            public void onError(Throwable e) {
                Timber.e(e, "createUserRequest error");
                if (e instanceof HttpException) {
                    ErrorToastService.get(getView()).showApiError(e);
                }
            }

            @Override
            public void onNext(User user) {
                Timber.d("user: %s", user.toString());
            }
        };


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
