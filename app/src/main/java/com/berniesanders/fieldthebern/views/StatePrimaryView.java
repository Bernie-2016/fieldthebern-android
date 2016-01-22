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
import android.graphics.drawable.Drawable;
import android.support.v7.widget.AppCompatImageView;
import android.util.AttributeSet;
import android.widget.RelativeLayout;
import android.widget.TextView;
import butterknife.Bind;
import butterknife.ButterKnife;
import com.berniesanders.fieldthebern.R;
import com.berniesanders.fieldthebern.models.ApiAddress;
import com.berniesanders.fieldthebern.models.StatePrimaryResponse;
import com.berniesanders.fieldthebern.mortar.DaggerService;
import com.berniesanders.fieldthebern.screens.StatePrimaryScreen;
import javax.inject.Inject;
import timber.log.Timber;

/**
 * Custom view for State primaries
 */
public class StatePrimaryView extends RelativeLayout {

  @Inject StatePrimaryScreen.Presenter presenter;

  @Bind(R.id.state_primary_image) AppCompatImageView primaryImage;

  @Bind(R.id.state_primary_name) TextView stateName;

  @Bind(R.id.state_primary_type) TextView primaryType;

  @Bind(R.id.state_primary_date) TextView primaryDate;

  @Bind(R.id.state_primary_deadline) TextView primaryDeadline;

  @Bind(R.id.state_primary_description) TextView primaryDescription;

  public StatePrimaryView(Context context) {
    super(context);
    injectSelf(context);
  }

  public StatePrimaryView(Context context, AttributeSet attrs) {
    super(context, attrs);
    injectSelf(context);
  }

  public StatePrimaryView(Context context, AttributeSet attrs, int defStyleAttr) {
    super(context, attrs, defStyleAttr);
    injectSelf(context);
  }

  /**
   * This is how the presenter is injected on to this view
   *
   * Component type is how the DaggerService finds the right component
   */
  private void injectSelf(Context context) {
    if (isInEditMode()) {
      return;
    }
    DaggerService.<StatePrimaryScreen.Component>getDaggerComponent(context,
        DaggerService.DAGGER_SERVICE) // get component
        .inject(this); // inject presenter into this view
  }

  public void populateStateInfo(StatePrimaryResponse.StatePrimary[] states, ApiAddress apiAddress) {

    // first find the state we desire
    StatePrimaryResponse.StatePrimary userState = null;

    for (int i = 0; i < states.length; i++) {

      if (states[i].getCode().equalsIgnoreCase(apiAddress.attributes().state())) {
        userState = states[i];
        break;
      }
    }

    if (userState == null) {
      Timber.e("Unable to extract state primary");
      return;
    }

    String name = userState.getState();
    String type = userState.getType();
    String date = "On " + userState.getDate();
    String deadline = "Voter Registration Deadline: " + userState.getDeadline();
    String description = userState.getDetails();

    Context context = getContext();

    String packageName = context.getPackageName();
    if (name != null) {
      String stateName = name.toLowerCase();

      if (stateName.equalsIgnoreCase("new york")) {
        stateName = "new_york";
      } else if (stateName.equalsIgnoreCase("new jersey")) {
        stateName = "new_jersey";
      } else if (stateName.equalsIgnoreCase("new hampshire")) {
        stateName = "new_hampshire";
      } else if (stateName.equalsIgnoreCase("new mexico")) {
        stateName = "new_mexico";
      } else if (stateName.equalsIgnoreCase("north carolina")) {
        stateName = "north_carolina";
      } else if (stateName.equalsIgnoreCase("north dakota")) {
        stateName = "north_dakota";
      } else if (stateName.equalsIgnoreCase("south carolina")) {
        stateName = "south_carolina";
      } else if (stateName.equalsIgnoreCase("south dakota")) {
        stateName = "south_dakota";
      } else if (stateName.equalsIgnoreCase("west virginia")) {
        stateName = "west_virginia";
      } else if (stateName.equalsIgnoreCase("rhode island")) {
        stateName = "rhode_island";
      }

      int resId = getResources().getIdentifier(stateName, "drawable", packageName);
      Drawable img = context.getResources().getDrawable(resId);
      if (img != null) {
        primaryImage.setImageDrawable(img);
      }
    }

    stateName.setText(name);
    primaryType.setText(type);
    primaryDate.setText(date);
    primaryDeadline.setText(deadline);
    primaryDescription.setText(description);
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
}
