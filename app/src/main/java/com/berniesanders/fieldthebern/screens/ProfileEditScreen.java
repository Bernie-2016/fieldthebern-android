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

import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.View;
import android.widget.EditText;
import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import com.berniesanders.fieldthebern.FTBApplication;
import com.berniesanders.fieldthebern.R;
import com.berniesanders.fieldthebern.annotations.Layout;
import com.berniesanders.fieldthebern.controllers.ToastService;
import com.berniesanders.fieldthebern.dagger.FtbScreenScope;
import com.berniesanders.fieldthebern.dagger.MainComponent;
import com.berniesanders.fieldthebern.events.LoginEvent;
import com.berniesanders.fieldthebern.models.CreateUserRequest;
import com.berniesanders.fieldthebern.models.User;
import com.berniesanders.fieldthebern.mortar.ParcelableScreen;
import com.berniesanders.fieldthebern.repositories.UserRepo;
import com.berniesanders.fieldthebern.repositories.specs.UserSpec;
import com.berniesanders.fieldthebern.views.PhotoEditView;
import com.berniesanders.fieldthebern.views.ProfileEditView;
import com.crashlytics.android.Crashlytics;
import flow.Flow;
import javax.inject.Inject;
import mortar.ViewPresenter;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import timber.log.Timber;

/**
 * Profile Screen for updating user profiles
 */
@Layout(R.layout.screen_profile_edit)
public class ProfileEditScreen extends ParcelableScreen {

  /**
   * Constructor called by Flow throughout the app
   * <p/>
   * Example:
   * Flow.get(context).set(new ExampleScreen("Some Data To Pass");
   * <p/>
   * Note:
   * Generally common types like "String" are not injected because injection works based on type
   */
  public ProfileEditScreen() {
  }

  /**
   * Create the component defined as an inner class below.
   * This component will inject the presenter on the view, and dependencies/module on the
   * presenter.
   * You can pass data (someData) from the Screen to its Presenter through this component.
   * Remember you must run the gradle 'build' class for Dagger to generate to component code
   * <p/>
   * Note:
   * Generally common types like "String" are not injected because injection works based on type
   */
  @Override
  public Object createComponent() {
    return DaggerProfileEditScreen_Component.builder()
        .mainComponent(
            FTBApplication.getComponent()) //must set if module has (dependencies = MainComponent.class)
        .profileEditModule(new ProfileEditModule()) //pass data to the presenter here
        .build();
  }

  @Override
  public String getScopeName() {
    return ProfileEditScreen.class.getName();
  }

  public static final Parcelable.Creator<ProfileEditScreen>
      CREATOR = zeroArgsScreenCreator(ProfileEditScreen.class);

  @dagger.Module
  class ProfileEditModule {

    /**
     * pass variables to the component that will then be injected to the presenter
     */
    public ProfileEditModule() {
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
  @dagger.Component(modules = ProfileEditModule.class, dependencies = MainComponent.class)
  public interface Component {
    /**
     * injection target = the view (ProfileEditView) to have the presented injected on it
     */
    void inject(ProfileEditView t);

    // Expose UserRepo through injection
    @SuppressWarnings("unused")
    UserRepo userRepo();
  }

  @FtbScreenScope
  static public class Presenter extends ViewPresenter<ProfileEditView> {

    /**
     * Since the presenter is static it should survive rotation
     */
    private final UserRepo userRepo;

    private Bitmap userPhoto;
    private String base64PhotoData;

    @Bind(R.id.first_name)
    EditText firstNameEditText;

    @Bind(R.id.last_name)
    EditText lastNameEditText;

    //        @Bind(R.id.email)
    //        EditText emailEditText;

    @Bind(R.id.photo_edit)
    PhotoEditView photoEditView;

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
     * @param savedInstanceState This bundle is only passed on rotation not passed on navigating
     * back
     */
    @Override
    protected void onLoad(Bundle savedInstanceState) {
      Timber.v("onLoad");
      ButterKnife.bind(this, getView());
      userRepo.getMe()
          .subscribeOn(Schedulers.io())
          .observeOn(AndroidSchedulers.mainThread())
          .subscribe(user -> {
            String firstName = user.getData().attributes().getFirstName();
            String lastName = user.getData().attributes().getLastName();
            //                    String email = user.getData().attributes().getEmail();
            firstNameEditText.setText(firstName);
            lastNameEditText.setText(lastName);
            //                    emailEditText.setText(email);
            photoEditView.load(user.getData().attributes(), userPhoto);
          }, throwable -> {
            Timber.e(throwable, "Unable to retrieve profile");
            Crashlytics.logException(throwable);
            ProfileEditView view1 = getView();
            if (view1 != null) {
              ToastService.get(view1)
                  .bern(view1.getContext().getString(R.string.error_retrieving_profile));
            } else {
              Timber.w("getView() null, can not notify user of failed profile retrieval.");
            }
          });

      photoEditView.setPhotoChangeListener((bitmap, base64PhotoData1) -> {
        userPhoto = bitmap;
        Presenter.this.base64PhotoData = base64PhotoData1;
      });

      getView().postDelayed(() -> photoEditView.toggleAvatarWidget(true), 300);
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
     * ((ProfileEditView)Path.get(view.getContext())).somePublicField = "Something you want to
     * save"
     */
    @Override
    public void dropView(ProfileEditView view) {
      super.dropView(view);
      ButterKnife.unbind(this);
    }

    @OnClick(R.id.submit_profile)
    void onSaveProfileClicked(final View v) {
      Timber.v("Attempting to save profile");
      String firstName = firstNameEditText.getText().toString();
      String lastName = lastNameEditText.getText().toString();
      //            String email = emailEditText.getText().toString();
      UserSpec spec = new UserSpec();
      final User user = new User();
      user.getData().attributes().firstName(firstName);
      user.getData().attributes().lastName(lastName);
      user.getData().attributes().base64PhotoData(base64PhotoData);
      //            user.getData().attributes().email(email);
      spec.create(new CreateUserRequest());
      spec.update(user);
      userRepo.update(spec)
          .subscribeOn(Schedulers.io())
          .observeOn(AndroidSchedulers.mainThread())
          .subscribe(user1 -> {
            Timber.v("Profile saved");
            ProfileEditView view1 = getView();
            if (view1 != null) {
              ToastService.get(view1).bern(view1.getContext().getString(R.string.profile_saved));
              FTBApplication.getEventBus().post(new LoginEvent(LoginEvent.LOGIN, user1));
              Flow.get(view1.getContext()).goBack();
            } else {
              Timber.w("getView() null, cannot notify user of successful profile save");
            }
          }, throwable -> {
            Timber.e(throwable, "Unable to save profile");
            Crashlytics.logException(throwable);
            ProfileEditView view1 = getView();
            if (view1 != null) {
              ToastService.get(view1)
                  .bern(view1.getContext().getString(R.string.error_saving_profile));
            } else {
              Timber.w("getView() null, cannot notify user of failed profile save");
            }
          });
    }
  }
}
