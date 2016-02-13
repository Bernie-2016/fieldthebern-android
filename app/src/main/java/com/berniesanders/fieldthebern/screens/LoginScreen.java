/*
 * Copyright (c) 2016 - Bernie 2016, Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */

package com.berniesanders.fieldthebern.screens;

import android.content.Intent;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import android.provider.Settings;
import android.support.design.widget.Snackbar;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.ImageView;
import butterknife.Bind;
import butterknife.BindString;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnTouch;
import com.berniesanders.fieldthebern.FTBApplication;
import com.berniesanders.fieldthebern.R;
import com.berniesanders.fieldthebern.annotations.Layout;
import com.berniesanders.fieldthebern.controllers.ActionBarController;
import com.berniesanders.fieldthebern.controllers.ActionBarService;
import com.berniesanders.fieldthebern.controllers.DialogController;
import com.berniesanders.fieldthebern.controllers.DialogService;
import com.berniesanders.fieldthebern.controllers.LocationService;
import com.berniesanders.fieldthebern.controllers.PermissionService;
import com.berniesanders.fieldthebern.controllers.ProgressDialogService;
import com.berniesanders.fieldthebern.controllers.ToastService;
import com.berniesanders.fieldthebern.dagger.FtbScreenScope;
import com.berniesanders.fieldthebern.dagger.MainComponent;
import com.berniesanders.fieldthebern.events.LoginEvent;
import com.berniesanders.fieldthebern.exceptions.NetworkUnavailableException;
import com.berniesanders.fieldthebern.models.LoginEmailRequest;
import com.berniesanders.fieldthebern.models.LoginFacebookRequest;
import com.berniesanders.fieldthebern.models.Token;
import com.berniesanders.fieldthebern.models.User;
import com.berniesanders.fieldthebern.models.UserAttributes;
import com.berniesanders.fieldthebern.mortar.ParcelableScreen;
import com.berniesanders.fieldthebern.parsing.ErrorResponseParser;
import com.berniesanders.fieldthebern.parsing.FormValidator;
import com.berniesanders.fieldthebern.repositories.TokenRepo;
import com.berniesanders.fieldthebern.repositories.UserRepo;
import com.berniesanders.fieldthebern.repositories.specs.TokenSpec;
import com.berniesanders.fieldthebern.views.LoginView;
import com.f2prateek.rx.preferences.Preference;
import com.f2prateek.rx.preferences.RxSharedPreferences;
import com.google.gson.Gson;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;
import dagger.Provides;
import flow.Flow;
import flow.History;
import javax.inject.Inject;
import mortar.ViewPresenter;
import retrofit2.HttpException;
import rx.Observer;
import rx.Subscription;
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
@Layout(R.layout.screen_login)
public class LoginScreen extends ParcelableScreen {

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
    return DaggerLoginScreen_Component.builder()
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

  @Override protected void doWriteToParcel(Parcel parcel, int flags) {
    parcel.writeParcelable(user, 0);
  }

  public static final Parcelable.Creator<LoginScreen>
      CREATOR = new ParcelableScreen.ScreenCreator<LoginScreen>() {
    @Override protected LoginScreen doCreateFromParcel(Parcel source) {
      User user = source.readParcelable(User.class.getClassLoader());
      return new LoginScreen(user);
    }

    @Override public LoginScreen[] newArray(int size) {
      return new LoginScreen[size];
    }
  };

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

    @BindString(R.string.login_title)
    String screenTitleString;
    @BindString(R.string.err_email_blank)
    String emailBlank;
    @BindString(R.string.err_password_blank)
    String passwordBlank;
    @BindString(R.string.location_disabled_title)
    String locationDisableTitle;
    @BindString(R.string.location_disabled_message)
    String locationDisabledBody;

    private final User user;
    private final UserRepo userRepo;
    private final TokenRepo tokenRepo;
    private final ErrorResponseParser errorResponseParser;
    private final RxSharedPreferences rxPrefs;
    private final Gson gson;
    private final MessageScreen messageScreen;
    private final UserAttributes userAttributes;
    private boolean stateCodeRequestCompleted;
    private boolean locationRequestCompleted;
    private Location location;
    private String stateCode;

    @Bind(R.id.password)
    EditText passwordEditText;

    @Bind(R.id.email)
    AutoCompleteTextView emailEditText;

    @Bind(R.id.user_photo)
    ImageView userImageView;

    @Bind(R.id.mask)
    ImageView mask;
    private Subscription stateCodeSubscription;
    private Subscription locationSubscription;
    private boolean showPleaseWait = false;

    @Inject
    Presenter(User user, UserRepo userRepo, TokenRepo tokenRepo,
        ErrorResponseParser errorResponseParser, RxSharedPreferences rxPrefs, Gson gson,
        MessageScreen messageScreen) {
      this.user = user;
      this.userRepo = userRepo;
      this.tokenRepo = tokenRepo;
      this.errorResponseParser = errorResponseParser;
      this.rxPrefs = rxPrefs;
      this.gson = gson;
      this.messageScreen = messageScreen;
      this.userAttributes = user.getData().attributes();
    }

    @Override
    protected void onLoad(Bundle savedInstanceState) {
      Timber.v("onLoad");
      ButterKnife.bind(this, getView());
      setActionBar();

      PermissionService.get(getView()).requestPermission();

      if (LocationService.get(getView()).isLocationEnabled()) {
        attemptLoginViaRefresh();
      } else {
        showEnableLocationDialog();
      }

      getView().loadUserEmailAccounts(emailEditText);

      if (userAttributes.isFacebookUser()) {
        getView().showFacebook(userAttributes);
        loadPhoto();
      }

      if (showPleaseWait) {
        ProgressDialogService.get(getView()).show(R.string.please_wait);
      }
    }

    private void showEnableLocationDialog() {
      DialogController.DialogAction confirmAction =
          new DialogController.DialogAction().label(android.R.string.ok).action(new Action0() {
            @Override
            public void call() {
              Timber.d("ok button click");
              Intent myIntent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
              getView().getContext().startActivity(myIntent);
            }
          });

      DialogService.get(getView())
          .setDialogConfig(new DialogController.DialogConfig().title(locationDisableTitle)
              .message(locationDisabledBody)
              .withActions(confirmAction));
    }

    private void attemptLoginViaRefresh() {
      //if the permission hasn't been granted the user should just login again
      if (!PermissionService.get(getView()).isGranted()) {
        return;
      }

      Preference<String> tokenPref = rxPrefs.getString(Token.PREF_NAME);

      if (tokenPref.get() == null) {
        return;
      }

      Token token = gson.fromJson(tokenPref.get(), Token.class);

      if (token == null) {
        return;
      } // if we don't have a token, we cant refresh

      tokenRepo.refresh()
          .subscribeOn(Schedulers.io())
          .observeOn(AndroidSchedulers.mainThread())
          .subscribe(refreshObserver);

      ProgressDialogService.get(getView()).show(R.string.please_wait);
      showPleaseWait = true;
    }

    void setActionBar() {
      ActionBarService.get(getView())
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

    @OnTouch(R.id.email_input_layout)
    boolean showEmailsInputLayout() {
      if (Build.VERSION.SDK_INT >= 21) {
        emailEditText.showDropDown();
        emailEditText.setFocusable(true);
        emailEditText.setShowSoftInputOnFocus(true);
      }
      return false;
    }

    @OnTouch(R.id.email)
    boolean showEmails() {
      if (Build.VERSION.SDK_INT >= 21) {
        emailEditText.showDropDown();
        emailEditText.setFocusable(true);
        emailEditText.setShowSoftInputOnFocus(true);
      }
      return false;
    }

    private void loadPhoto() {
      Picasso.with(getView().getContext())
          .load(userAttributes.getPhotoLargeUrl())
          .into(userImageView, new Callback() {
            @Override
            public void onSuccess() {
              mask.setVisibility(View.VISIBLE);
            }

            @Override
            public void onError() {
              Timber.e("error loading facebook image");
            }
          });
    }

    @OnClick(R.id.login_email)
    void loginEmail(final View v) {

      if (PermissionService.get(v).isGranted()) {

        requestLocation();
      } else {
        // Display a SnackBar with an explanation and a button to trigger the request.
        Snackbar.make(v, R.string.permission_contacts_rationale, Snackbar.LENGTH_INDEFINITE)
            .setAction(android.R.string.ok, new View.OnClickListener() {
              @Override
              public void onClick(View view) {
                PermissionService.get(view).requestPermission();
              }
            })
            .show();
      }
    }

    private void login() {
      if (!formIsValid()) {
        return;
      }

      ProgressDialogService.get(getView()).show(R.string.please_wait);
      showPleaseWait = true;

      if (user.getData().attributes().isFacebookUser()) {

        TokenSpec spec = new TokenSpec().facebook(
            new LoginFacebookRequest().password(passwordEditText.getText().toString())
                .username(emailEditText.getText().toString()));

        tokenRepo.loginFacebook(spec)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(observer);
      } else {

        TokenSpec spec = new TokenSpec().email(
            new LoginEmailRequest().password(passwordEditText.getText().toString())
                .username(emailEditText.getText().toString()));

        tokenRepo.loginEmail(spec)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(observer);
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
        showPleaseWait = false;
      }

      @Override
      public void onError(Throwable e) {
        if (getView() == null) {
          Timber.e(e, "loginEmail onError");
          return;
        }

        if (e instanceof HttpException && ((HttpException) e).code() == 401) {
          ToastService.get(getView()).bern(getView().getResources().getString(R.string.err_incorrect_email_pass));
        } else {
          ToastService.get(getView()).bern(getView().getResources().getString(R.string.err_login_failed_generic));
        }
        ProgressDialogService.get(getView()).dismiss();
        showPleaseWait = false;
      }

      @Override
      public void onNext(Token token) {
        Timber.d("loginEmail onNext: %s", token.toString());

        userRepo.getMe()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(new Action1<User>() {
              @Override
              public void call(User user) {
                ProgressDialogService.get(getView()).dismiss();
                showPleaseWait = false;
                FTBApplication.getEventBus().post(new LoginEvent(LoginEvent.LOGIN, user));
                Flow.get(getView())
                    .setHistory(History.single(messageScreen.getMessageOrHome(location, stateCode)),
                        Flow.Direction.FORWARD);
              }
            });
      }
    };

    private boolean addressAndLocationSet() {
      return (stateCodeRequestCompleted && locationRequestCompleted);
    }

    private Observer<Location> locationObserver = new Observer<Location>() {
      @Override
      public void onCompleted() {
        locationRequestCompleted = true;
        if (addressAndLocationSet()) {
          login();
        }
      }

      @Override
      public void onError(Throwable e) {
        locationRequestCompleted = true;
        Timber.e(e, "locationObserver onError");
        if (addressAndLocationSet()) {
          login();
        }
      }

      @Override
      public void onNext(Location location) {
        Timber.v("location on next" + location);
        Presenter.this.location = location;
      }
    };

    private Observer<String> stateCodeObserver = new Observer<String>() {
      @Override
      public void onCompleted() {
        stateCodeRequestCompleted = true;
        if (addressAndLocationSet()) {
          login();
        }
      }

      @Override
      public void onError(Throwable e) {
        stateCodeRequestCompleted = true;
        Timber.e(e, "stateCodeObserver onError");
        if (addressAndLocationSet()) {
          login();
        }
      }

      @Override
      public void onNext(String stateCode) {
        Timber.v("stateCodeObserver on next" + stateCode);
        Presenter.this.stateCode = stateCode;
      }
    };

    private void requestLocation() {
      stateCodeSubscription =
          LocationService.get(getView()).getAddress().subscribe(stateCodeObserver);
      locationSubscription = LocationService.get(getView()).get().subscribe(locationObserver);
    }

    Observer<Token> refreshObserver = new Observer<Token>() {
      @Override
      public void onCompleted() {
        Timber.d("refreshObserver done.");
        if (getView() == null) {
          return;
        }
        ProgressDialogService.get(getView()).dismiss();
        showPleaseWait = false;
      }

      @Override
      public void onError(Throwable e) {
        if (getView() == null) {
          Timber.e(e, "refreshObserver onError");
          return;
        }
        ProgressDialogService.get(getView()).dismiss();
        showPleaseWait = false;

        if (e instanceof NetworkUnavailableException) {
          ToastService.get(getView())
              .bern(getView().getResources().getString(R.string.err_internet_not_available));
        }
      }

      @Override
      public void onNext(Token token) {
        Timber.d("refreshObserver onNext: %s", token.toString());
        userRepo.getMe()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(new Action1<User>() {
              @Override
              public void call(User user) {
                if (getView() == null) { return; } //TODO need some kind of better error handling here

                ProgressDialogService.get(getView()).dismiss();
                showPleaseWait = false;
                FTBApplication.getEventBus().post(new LoginEvent(LoginEvent.LOGIN, user));

                Flow.get(getView())
                    .setHistory(History.single(new HomeScreen()), Flow.Direction.FORWARD);
              }
            });
      }
    };

    @OnClick(R.id.no_account)
    void noAccount(final View v) {
      Flow.get(v)
          .setHistory(History.single(new ChooseSignupScreen()), Flow.Direction.BACKWARD);
    }
  }
}
