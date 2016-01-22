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

import android.content.Context;
import android.util.AttributeSet;
import android.widget.LinearLayout;
import com.berniesanders.fieldthebern.FTBApplication;
import com.berniesanders.fieldthebern.screens.DaggerNavigationScreen_Component;
import com.berniesanders.fieldthebern.screens.NavigationScreen;
import javax.inject.Inject;
import timber.log.Timber;

/**
 * Example mortar screen.
 * Change what it extends as needed. Any View/Layout type is fine to extend
 */
public class NavigationView extends LinearLayout {

  /**
   * Make sure you are pointing at the correct presenter type
   * YourScreen.Presenter
   */
  @Inject NavigationScreen.Presenter presenter;

  NavigationScreen.Component daggerComponent;

  public NavigationView(Context context) {
    super(context);
    injectSelf(context);
  }

  public NavigationView(Context context, AttributeSet attrs) {
    super(context, attrs);
    injectSelf(context);
  }

  public NavigationView(Context context, AttributeSet attrs, int defStyleAttr) {
    super(context, attrs, defStyleAttr);
    injectSelf(context);
  }

  /**
   * This is a bit unusual method of injection because
   * the nav sits outside the normal flow container/process
   */
  private void injectSelf(Context context) {
    if (isInEditMode()) {
      return;
    }
    createComponent().inject(this);
  }

  private NavigationScreen.Component createComponent() {
    return DaggerNavigationScreen_Component.builder()
        .mainComponent(FTBApplication.getComponent())
        .build();
  }

  @Override protected void onFinishInflate() {
    super.onFinishInflate();
    Timber.v("onFinishInflate");
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
}
