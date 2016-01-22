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

package com.berniesanders.fieldthebern.views;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.content.Context;
import android.support.v7.widget.AppCompatEditText;
import android.util.AttributeSet;
import android.util.Patterns;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import butterknife.Bind;
import butterknife.ButterKnife;
import com.berniesanders.fieldthebern.R;
import com.berniesanders.fieldthebern.models.UserAttributes;
import com.berniesanders.fieldthebern.mortar.DaggerService;
import com.berniesanders.fieldthebern.screens.SignupScreen;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;
import javax.inject.Inject;
import timber.log.Timber;

/**
 */
public class SignupView extends RelativeLayout {

  /**
   */
  @Inject SignupScreen.Presenter presenter;

  @Bind(R.id.first_name) AppCompatEditText firstName;

  @Bind(R.id.last_name) AppCompatEditText lastName;

  @Bind(R.id.email) AutoCompleteTextView email;

  @Bind(R.id.password) AppCompatEditText password;

  @Bind(R.id.user_photo) ImageView userImageView;

  @Bind(R.id.mask) ImageView mask;

  public SignupView(Context context) {
    super(context);
    injectSelf(context);
  }

  public SignupView(Context context, AttributeSet attrs) {
    super(context, attrs);
    injectSelf(context);
  }

  public SignupView(Context context, AttributeSet attrs, int defStyleAttr) {
    super(context, attrs, defStyleAttr);
    injectSelf(context);
  }

  /**
   */
  private void injectSelf(Context context) {
    if (isInEditMode()) {
      return;
    }
    DaggerService.<SignupScreen.Component>getDaggerComponent(context,
        DaggerService.DAGGER_SERVICE).inject(this);
  }

  public void loadUserEmailAccounts(AutoCompleteTextView emailAutocompleteTV) {
    Pattern emailPattern = Patterns.EMAIL_ADDRESS; // API level 8+
    Account[] accounts = AccountManager.get(getContext()).getAccountsByType("com.google");

    final List<String> possibleEmails = new ArrayList<>();

    for (Account account : accounts) {
      if (emailPattern.matcher(account.name).matches()) {
        String possibleEmail = account.name;
        possibleEmails.add(possibleEmail);
      }
    }

    ArrayAdapter<String> adapter =
        new ArrayAdapter<>(getContext(), android.R.layout.simple_dropdown_item_1line,
            possibleEmails);

    emailAutocompleteTV.setAdapter(adapter);
    emailAutocompleteTV.setThreshold(0);
  }

  @Override protected void onFinishInflate() {
    super.onFinishInflate();
    if (isInEditMode()) {
      return;
    }
    Timber.v("onFinishInflate");
    ButterKnife.bind(this, this);
  }

  @Override protected void onAttachedToWindow() {
    super.onAttachedToWindow();
    if (isInEditMode()) {
      return;
    }
    presenter.takeView(this);
  }

  @Override protected void onDetachedFromWindow() {
    super.onDetachedFromWindow();
    presenter.dropView(this);
  }

  public void showFacebook(UserAttributes userAttributes) {

    firstName.setText(userAttributes.getFirstName());
    lastName.setText(userAttributes.getLastName());
    email.setText(userAttributes.getEmail());
  }

  public ImageView getUserImageView() {
    return userImageView;
  }

  public UserAttributes getInput(UserAttributes userAttributes) {

    userAttributes.email(email.getText().toString())
        .password(password.getText().toString())
        .firstName(firstName.getText().toString());

    //last name is optional
    if (lastName.getText() != null) {
      userAttributes.lastName(lastName.getText().toString());
    }

    return userAttributes;
  }

  public AppCompatEditText getFirstName() {
    return firstName;
  }

  public AppCompatEditText getLastName() {
    return lastName;
  }

  public AutoCompleteTextView getEmail() {
    return email;
  }

  public AppCompatEditText getPassword() {
    return password;
  }

  public void showMask() {
    mask.setVisibility(VISIBLE);
  }
}
