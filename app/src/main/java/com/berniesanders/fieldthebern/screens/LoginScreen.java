package com.berniesanders.fieldthebern.screens;

import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.view.View;
import android.widget.EditText;

import com.berniesanders.fieldthebern.FTBApplication;
import com.berniesanders.fieldthebern.R;
import com.berniesanders.fieldthebern.annotations.Layout;
import com.berniesanders.fieldthebern.controllers.ActionBarController;
import com.berniesanders.fieldthebern.controllers.ActionBarService;
import com.berniesanders.fieldthebern.controllers.PermissionService;
import com.berniesanders.fieldthebern.controllers.ProgressDialogService;
import com.berniesanders.fieldthebern.controllers.ToastService;
import com.berniesanders.fieldthebern.dagger.FtbScreenScope;
import com.berniesanders.fieldthebern.dagger.MainComponent;
import com.berniesanders.fieldthebern.models.ErrorResponse;
import com.berniesanders.fieldthebern.models.LoginEmailRequest;
import com.berniesanders.fieldthebern.models.Token;
import com.berniesanders.fieldthebern.models.User;
import com.berniesanders.fieldthebern.mortar.FlowPathBase;
import com.berniesanders.fieldthebern.parsing.ErrorResponseParser;
import com.berniesanders.fieldthebern.parsing.FormValidator;
import com.berniesanders.fieldthebern.repositories.TokenRepo;
import com.berniesanders.fieldthebern.repositories.specs.TokenSpec;
import com.berniesanders.fieldthebern.views.LoginView;
import com.f2prateek.rx.preferences.Preference;
import com.f2prateek.rx.preferences.RxSharedPreferences;
import com.google.gson.Gson;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.BindString;
import butterknife.ButterKnife;
import butterknife.OnClick;
import dagger.Provides;
import flow.Flow;
import mortar.ViewPresenter;
import retrofit.HttpException;
import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import timber.log.Timber;

/**
 * Example for creating new Mortar Screen that helps explain how it all works
 *
 * Set the @Layout annotation to the resource id of the layout for the screen
 */
@Layout(R.layout.screen_login)
public class LoginScreen extends FlowPathBase {

    private final User user;

    /**
     */
    public LoginScreen(User user) {
        this.user = user;
    }

    /**
     */
    @Override
    public Object createComponent() {
        return DaggerLoginScreen_Component
                .builder()
                .mainComponent(FTBApplication.getComponent())
                .module(new Module(user))
                .build();
    }

    /**
     */
    @Override
    public String getScopeName() {
        return LoginScreen.class.getName();
    }


    @dagger.Module
    class Module {
        private final User user;

        Module(User user) {
            this.user = user;
        }

        @Provides
        @FtbScreenScope
        public User provideUser() {
            return user;
        }
    }

    /**
     */
    @FtbScreenScope
    @dagger.Component(modules = Module.class, dependencies = MainComponent.class)
    public interface Component {
        void inject(LoginView t);
        User user();
        ErrorResponseParser errorParser();
    }

    @FtbScreenScope
    static public class Presenter extends ViewPresenter<LoginView> {

        @BindString(R.string.login_title) String screenTitleString;
        @BindString(R.string.err_email_blank) String emailBlank;
        @BindString(R.string.err_password_blank) String passwordBlank;

        private final User user;
        private final TokenRepo tokenRepo;
        private final ErrorResponseParser errorResponseParser;
        private final RxSharedPreferences rxPrefs;
        private final Gson gson;

        @Bind(R.id.password)
        EditText passwordEditText;

        @Bind(R.id.email)
        EditText emailEditText;

        @Inject
        Presenter(User user,
                  TokenRepo tokenRepo,
                  ErrorResponseParser errorResponseParser,
                  RxSharedPreferences rxPrefs,
                  Gson gson) {
            this.user = user;
            this.tokenRepo = tokenRepo;
            this.errorResponseParser = errorResponseParser;
            this.rxPrefs = rxPrefs;
            this.gson = gson;
        }

        @Override
        protected void onLoad(Bundle savedInstanceState) {
            Timber.v("onLoad");
            ButterKnife.bind(this, getView());
            setActionBar();

            PermissionService
                    .get(getView())
                    .requestPermission();

            attemptLoginViaRefresh();
        }

        private void attemptLoginViaRefresh() {
            //if the permission hasn't been granted the user should just login again
            if (!PermissionService.get(getView()).isGranted()) { return; }

            Preference<String> tokenPref = rxPrefs.getString(Token.PREF_NAME);

            if (tokenPref.get()==null) { return; }

            Token token = gson.fromJson(tokenPref.get(), Token.class);

            if (token == null) { return; } // if we don't have a token, we cant refresh

            tokenRepo.refresh()
                     .subscribeOn(Schedulers.io())
                     .observeOn(AndroidSchedulers.mainThread())
                     .subscribe(refreshObserver);

            ProgressDialogService.get(getView()).show(R.string.please_wait);
        }


        void setActionBar() {
            ActionBarService
                    .get(getView())
                    .showToolbar()
                    .closeAppbar()
                    .lockDrawer()
                    .setMainImage(null)
                    .setConfig(new ActionBarController.Config(screenTitleString, null));
        }

        @Override
        protected void onSave(Bundle outState) {
        }

        @Override
        public void dropView(LoginView view) {
            super.dropView(view);
            ButterKnife.unbind(this);
        }

        @OnClick(R.id.login_email)
        void loginEmail() {

            if (PermissionService.get(getView()).isGranted()) {

                if(!formIsValid()) { return; }

                if (!user.getData().attributes().isFacebookUser()) {

                    TokenSpec spec = new TokenSpec()
                            .email(new LoginEmailRequest()
                                    .password(passwordEditText.getText().toString())
                                    .username(emailEditText.getText().toString()));

                    tokenRepo
                            .loginEmail(spec)
                            .subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe(observer);
                    ProgressDialogService.get(getView()).show(R.string.please_wait);
                }

            } else {
                // Display a SnackBar with an explanation and a button to trigger the request.
                Snackbar.make(getView(), R.string.permission_contacts_rationale,
                        Snackbar.LENGTH_INDEFINITE)
                        .setAction(android.R.string.ok, new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                PermissionService.get(getView()).requestPermission();
                            }
                        })
                        .show();
            }

        }

        private boolean formIsValid() {
            if (FormValidator.isNullOrBlank(emailEditText)) {
                ToastService.get(getView()).bern(emailBlank);
                return false;
            } else if (FormValidator.isNullOrBlank(passwordEditText)) {
                ToastService.get(getView()).bern(passwordBlank);
                return false;
            }
            return true;
        }

        Observer<Token> observer = new Observer<Token>() {
            @Override
            public void onCompleted() {
                Timber.d("loginEmail done.");
                if (getView() == null) {
                    return;
                }
                ProgressDialogService.get(getView()).dismiss();
            }

            @Override
            public void onError(Throwable e) {
                if (getView() == null) {
                    Timber.e(e, "loginEmail onError");
                    return;
                }

                if (e instanceof HttpException) {
                    ErrorResponse errorResponse = errorResponseParser.parse((HttpException) e);
                    ToastService.get(getView()).bern(errorResponse.getAllDetails());
                }
                ProgressDialogService.get(getView()).dismiss();
            }

            @Override
            public void onNext(Token token) {
                Timber.d("loginEmail onNext: %s", token.toString());
                ProgressDialogService.get(getView()).dismiss();
                Flow.get(getView().getContext()).set(new HomeScreen());
            }
        };

        Observer<Token> refreshObserver = new Observer<Token>() {
            @Override
            public void onCompleted() {
                Timber.d("refreshObserver done.");
                if (getView() == null) {
                    return;
                }
                ProgressDialogService.get(getView()).dismiss();
            }

            @Override
            public void onError(Throwable e) {
                if (getView() == null) {
                    Timber.e(e, "refreshObserver onError");
                    return;
                }
                ProgressDialogService.get(getView()).dismiss();
            }

            @Override
            public void onNext(Token token) {
                Timber.d("refreshObserver onNext: %s", token.toString());
                ProgressDialogService.get(getView()).dismiss();
                Flow.get(getView().getContext()).set(new HomeScreen());
            }
        };

        @OnClick(R.id.no_account)
        void haveAccount() {
            Flow.get(getView().getContext()).set(new ChooseSignupScreen());
        }
    }
}
