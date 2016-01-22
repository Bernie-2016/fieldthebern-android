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
import android.widget.RelativeLayout;
import com.berniesanders.fieldthebern.mortar.DaggerService;
import com.berniesanders.fieldthebern.screens.HomeScreen;
import javax.inject.Inject;
import timber.log.Timber;

/**
 */
public class HomeView extends RelativeLayout {

  /**
   * Make sure you are pointing at the correct presenter type
   * YourScreen.Presenter
   */
  @Inject
  HomeScreen.Presenter presenter;

  public HomeView(Context context) {
    super(context);
    injectSelf(context);
  }

  public HomeView(Context context, AttributeSet attrs) {
    super(context, attrs);
    injectSelf(context);
  }

  public HomeView(Context context, AttributeSet attrs, int defStyleAttr) {
    super(context, attrs, defStyleAttr);
    injectSelf(context);
  }

  /**
   * This is how the presenter is injected on to this view
   *
   * Important to note component type is how the DaggerService finds the right component
   */
  private void injectSelf(Context context) {
    if (isInEditMode()) {
      return;
    }
    DaggerService.<HomeScreen.Component>getDaggerComponent(context,
        DaggerService.DAGGER_SERVICE).inject(this);
  }

  @Override
  protected void onFinishInflate() {
    super.onFinishInflate();
    if (isInEditMode()) {
      return;
    }
    Timber.v("onFinishInflate");
  }

  @Override
  protected void onAttachedToWindow() {
    super.onAttachedToWindow();
    if (isInEditMode()) {
      return;
    }
    presenter.takeView(this);
  }

  @Override
  protected void onDetachedFromWindow() {
    super.onDetachedFromWindow();
    if (isInEditMode()) {
      return;
    }
    presenter.dropView(this);
  }
}
