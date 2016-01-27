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
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.widget.Button;
import android.widget.FrameLayout;
import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import com.berniesanders.fieldthebern.R;
import com.berniesanders.fieldthebern.adapters.AppIntroPagerAdapter;
import com.berniesanders.fieldthebern.models.User;
import com.berniesanders.fieldthebern.mortar.DaggerService;
import com.berniesanders.fieldthebern.screens.AppIntroScreen;
import com.berniesanders.fieldthebern.screens.ChooseSignupScreen;
import com.f2prateek.rx.preferences.RxSharedPreferences;
import flow.Flow;
import flow.History;
import javax.inject.Inject;
import me.relex.circleindicator.CircleIndicator;

public class AppIntroView extends FrameLayout {

  @Bind(R.id.viewpager)
  ViewPager viewPager;
  @Bind(R.id.circleIndicator)
  CircleIndicator circleIndicator;
  @Bind(R.id.doneButton)
  Button doneButton;

  @Inject
  AppIntroScreen.Presenter presenter;
  @Inject
  RxSharedPreferences rxPrefs;

  public AppIntroView(Context context) {
    super(context);
    injectSelf(context);
  }

  public AppIntroView(Context context, AttributeSet attrs) {
    super(context, attrs);
    injectSelf(context);
  }

  public AppIntroView(Context context, AttributeSet attrs, int defStyleAttr) {
    super(context, attrs, defStyleAttr);
    injectSelf(context);
  }

  private void injectSelf(Context context) {
    if (isInEditMode()) {
      return;
    }
    DaggerService.<AppIntroScreen.Component>getDaggerComponent(context,
        DaggerService.DAGGER_SERVICE).inject(this);
  }

  @Override
  protected void onFinishInflate() {
    super.onFinishInflate();
    ButterKnife.bind(this, this);
    viewPager.setAdapter(new AppIntroPagerAdapter(getContext()));
    circleIndicator.setViewPager(viewPager);

    viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
      @Override
      public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

      }

      @Override
      public void onPageSelected(int position) {
        if (position == 4) {
          done();
        }
        doneButton.setText(position == 3 ? R.string.done : R.string.skip);
      }

      @Override
      public void onPageScrollStateChanged(int state) {

      }
    });
  }

  @Override
  protected void onAttachedToWindow() {
    super.onAttachedToWindow();
    presenter.takeView(this);
  }

  @Override
  protected void onDetachedFromWindow() {
    super.onDetachedFromWindow();
    presenter.dropView(this);
  }

  @OnClick(R.id.doneButton)
  public void done() {
    Flow.get(this).setHistory(History.single(new ChooseSignupScreen()), Flow.Direction.REPLACE);
    rxPrefs.getBoolean(User.PREF_SEEN_APP_INTRO).set(true);
  }
}
