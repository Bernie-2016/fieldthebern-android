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
import android.graphics.Bitmap;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.design.widget.Snackbar;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.Toast;
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
import com.berniesanders.fieldthebern.models.CreateUserRequest;
import com.berniesanders.fieldthebern.models.ErrorResponse;
import com.berniesanders.fieldthebern.models.User;
import com.berniesanders.fieldthebern.models.UserAttributes;
import com.berniesanders.fieldthebern.mortar.FlowPathBase;
import com.berniesanders.fieldthebern.parsing.ErrorResponseParser;
import com.berniesanders.fieldthebern.repositories.UserRepo;
import com.berniesanders.fieldthebern.repositories.specs.UserSpec;
import com.berniesanders.fieldthebern.views.PhotoEditView;
import com.berniesanders.fieldthebern.views.SignupView;
import dagger.Provides;
import flow.Flow;
import flow.History;
import java.util.concurrent.TimeUnit;
import javax.inject.Inject;
import mortar.ViewPresenter;
import org.apache.commons.lang3.StringUtils;
import retrofit2.HttpException;
import rx.Observer;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action0;
import rx.schedulers.Schedulers;
import timber.log.Timber;

import static com.berniesanders.fieldthebern.parsing.FormValidator.isEmailValid;
import static com.berniesanders.fieldthebern.parsing.FormValidator.isNullOrBlank;

/**
 * Example for creating new Mortar Screen that helps explain how it all works
 *
 * Set the @Layout annotation to the resource id of the layout for the screen
 */
@Layout(R.layout.screen_signup)
public class SignupScreen extends FlowPathBase {

  private final UserAttributes userAttributes;

  /**
   */
  public SignupScreen(UserAttributes userAttributes) {
    this.userAttributes = userAttributes;
  }

  /**
   */
  @Override
  public Object createComponent() {
    return DaggerSignupScreen_Component.builder()
        .mainComponent(FTBApplication.getComponent())
        .module(new Module(userAttributes))
        .build();
  }

  /**
   */
  @Override
  public String getScopeName() {
    return SignupScreen.class.getName();
  }

  @dagger.Module
  class Module {
    private final UserAttributes userAttributes;

    public Module(UserAttributes userAttributes) {
      this.userAttributes = userAttributes;
    }

    @FtbScreenScope
    @Provides
    public UserAttributes provideUserAttributes() {
      return userAttributes;
    }
  }

  /**
   */
  @FtbScreenScope
  @dagger.Component(dependencies = MainComponent.class, modules = Module.class)
  public interface Component {
    void inject(SignupView t);

    UserRepo userRepo();

    ErrorResponseParser errorParser();
  }

  @FtbScreenScope
  static public class Presenter extends ViewPresenter<SignupView> {

    @BindString(R.string.signup_title)
    String screenTitleString;
    @BindString(R.string.err_email_blank)
    String emailBlank;
    @BindString(R.string.err_password_blank)
    String passwordBlank;
    @BindString(R.string.err_your_first_name_blank)
    String firstNameBlank;
    @BindString(R.string.err_your_last_name_blank)
    String lastNameBlank;
    @BindString(R.string.err_invalid_email)
    String invalidEmailError;
    @BindString(R.string.location_disabled_title)
    String locationDisableTitle;
    @BindString(R.string.location_disabled_message)
    String locationDisabledBody;

    private final UserRepo repo;
    private UserAttributes userAttributes;
    private final ErrorResponseParser errorResponseParser;
    private final MessageScreen messageScreen;
    Bitmap userBitmap;

    private String stateCode;
    private Location location;
    private Subscription stateCodeSubscription;
    private Subscription locationSubscription;

    private boolean stateCodeRequestCompleted = false;
    private boolean locationRequestCompleted = false;

    @Bind(R.id.avatar_buttons)
    View avatarButtons;

    @Bind(R.id.avatar_container)
    View avatarContainer;

    @Bind(R.id.email)
    AutoCompleteTextView emailAutocompleteTV;

    @Bind(R.id.photo_edit)
    PhotoEditView photoEditView;
    private boolean showPleaseWait = false;

    @Inject
    Presenter(UserRepo repo, UserAttributes userAttributes, ErrorResponseParser errorResponseParser,
        MessageScreen messageScreen) {
      this.repo = repo;
      this.userAttributes = userAttributes;
      this.errorResponseParser = errorResponseParser;
      this.messageScreen = messageScreen;
    }

    @Override
    protected void onLoad(Bundle savedInstanceState) {
      Timber.v("onLoad");
      ButterKnife.bind(this, getView());
      setActionBar();

      if (userAttributes.isFacebookUser()) {
        getView().showFacebook(userAttributes);
        photoEditView.load(userAttributes, userBitmap);
      } else if (userBitmap != null) {
        // User may have set their own photo and persist through rotation
        photoEditView.load(userAttributes, userBitmap);
      } else {
        getView().postDelayed(new Runnable() {
          @Override
          public void run() {
            photoEditView.toggleAvatarWidget(true);
          }
        }, 300);
      }

      photoEditView.setPhotoChangeListener(new PhotoEditView.PhotoChangeListener() {
        @Override
        public void onPhotoChanged(Bitmap bitmap, String base64PhotoData) {
          userBitmap = bitmap;
          userAttributes.base64PhotoData(base64PhotoData);
        }
      });

      if (PermissionService.get(getView()).isGranted()) {

        if (!LocationService.get(getView()).isLocationEnabled()) {
          showEnableLocationDialog();
        }
      } else {
        PermissionService.get(getView()).requestPermission();
      }

      getView().loadUserEmailAccounts(emailAutocompleteTV);

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

    @OnTouch(R.id.email_input_layout)
    boolean showEmailsInputLayout() {
      if (Build.VERSION.SDK_INT >= 21) {
        emailAutocompleteTV.showDropDown();
        emailAutocompleteTV.setFocusable(true);
        emailAutocompleteTV.setShowSoftInputOnFocus(true);
      }
      return false;
    }

    @OnTouch(R.id.email)
    boolean showEmails() {
      if (Build.VERSION.SDK_INT >= 21) {
        emailAutocompleteTV.showDropDown();
        emailAutocompleteTV.setFocusable(true);
        emailAutocompleteTV.setShowSoftInputOnFocus(true);
      }
      return false;
    }

    void setActionBar() {
      ActionBarService.get(getView())
          .showToolbar()
          .closeAppbar()
          .lockDrawer()
          .setMainImage(null)
          .setConfig(new ActionBarController.Config(screenTitleString, null));
    }

    @OnClick(R.id.submit)
    void onSubmit() {
      if (PermissionService.get(getView()).isGranted()) {
        if (!formIsValid()) {
          return;
        }
        if (!LocationService.get(getView()).isLocationEnabled()) {
          showEnableLocationDialog();
          return;
        }
        ProgressDialogService.get(getView()).show(R.string.please_wait);
        showPleaseWait = true;
        userAttributes = getView().getInput(userAttributes);
        requestLocation();
      } else {
        // Display a SnackBar with an explanation and a button to trigger the request.
        Snackbar.make(getView(), R.string.permission_contacts_rationale, Snackbar.LENGTH_INDEFINITE)
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
      //last name is optional
      if (isNullOrBlank(getView().getFirstName())) {
        ToastService.get(getView()).bern(firstNameBlank, Toast.LENGTH_SHORT);
        return false;
      } else if (isNullOrBlank(getView().getLastName())) {
        ToastService.get(getView()).bern(lastNameBlank, Toast.LENGTH_SHORT);
        return false;
      } else if (isNullOrBlank(getView().getEmail())) {
        ToastService.get(getView()).bern(emailBlank, Toast.LENGTH_SHORT);
        return false;
      } else if (!isEmailValid(getView().getEmail().getText())) {
        ToastService.get(getView()).bern(invalidEmailError, Toast.LENGTH_SHORT);
        return false;
      } else if (isNullOrBlank(getView().getPassword())) {
        ToastService.get(getView()).bern(passwordBlank, Toast.LENGTH_SHORT);
        return false;
      }
      return true;
    }

    private boolean addressAndLocationSet() {
      return (stateCodeRequestCompleted && locationRequestCompleted);
    }

    private Observer<Location> locationObserver = new Observer<Location>() {
      @Override
      public void onCompleted() {
        locationRequestCompleted = true;
        if (addressAndLocationSet()) {
          createEmailUser();
        }
      }

      @Override
      public void onError(Throwable e) {
        locationRequestCompleted = true;
        Timber.e(e, "locationObserver onError");
        if (addressAndLocationSet()) {
          createEmailUser();
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
          createEmailUser();
        }
      }

      @Override
      public void onError(Throwable e) {
        stateCodeRequestCompleted = true;
        Timber.e(e, "stateCodeObserver onError");
        if (addressAndLocationSet()) {
          createEmailUser();
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

    private void createEmailUser() {

      //if validate form...
      Timber.v("createEmailUser");

      if (location != null) {
        userAttributes.lat(location.getLatitude()).lng(location.getLongitude());
      }

      if (StringUtils.isNotBlank(stateCode)) {
        userAttributes.stateCode(stateCode);
      }

      CreateUserRequest createUserRequest = new CreateUserRequest().withAttributes(userAttributes);

      final UserSpec spec = new UserSpec().create(createUserRequest);

      repo.create(spec)
          .subscribeOn(Schedulers.io())
          .observeOn(AndroidSchedulers.mainThread())
          .timeout(15, TimeUnit.SECONDS)
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
        if (getView() == null) {
          return;
        }
        ProgressDialogService.get(getView()).dismiss();
        showPleaseWait = false;
        if (e instanceof HttpException) {
          ErrorResponse errorResponse = errorResponseParser.parse((HttpException) e);
          ToastService.get(getView()).bern(errorResponse.getAllDetails(), Toast.LENGTH_SHORT);
        }
        if (e instanceof NetworkUnavailableException) {
          ToastService.get(getView())
              .bern(getView().getResources().getString(R.string.err_internet_not_available));
        }
      }

      @Override
      public void onNext(User user) {
        Timber.d("user created! user: %s", user.toString());
        ProgressDialogService.get(getView()).dismiss();
        showPleaseWait = false;
        FTBApplication.getEventBus().post(new LoginEvent(LoginEvent.LOGIN, user));

        //Flow.get(getView()).setHistory(History.single(new HomeScreen()), Flow.Direction.FORWARD);

        Flow.get(getView())
            .setHistory(History.single(messageScreen.getMessageOrHome(location, stateCode)),
                Flow.Direction.FORWARD);
      }
    };

    @Override
    protected void onSave(Bundle outState) {
    }

    @Override
    public void dropView(SignupView view) {
      super.dropView(view);
      ButterKnife.unbind(this);
    }
  }
}
