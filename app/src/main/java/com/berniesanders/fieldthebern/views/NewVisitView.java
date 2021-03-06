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

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.AppCompatButton;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import butterknife.Bind;
import butterknife.ButterKnife;
import com.berniesanders.fieldthebern.R;
import com.berniesanders.fieldthebern.media.PartyIcon;
import com.berniesanders.fieldthebern.models.ApiAddress;
import com.berniesanders.fieldthebern.models.CanvassData;
import com.berniesanders.fieldthebern.models.Person;
import com.berniesanders.fieldthebern.models.StatePrimaryResponse;
import com.berniesanders.fieldthebern.models.Visit;
import com.berniesanders.fieldthebern.mortar.DaggerService;
import com.berniesanders.fieldthebern.parsing.CanvassResponseEvaluator;
import com.berniesanders.fieldthebern.screens.AddPersonScreen;
import com.berniesanders.fieldthebern.screens.NewVisitScreen;
import com.crashlytics.android.Crashlytics;
import flow.Flow;
import javax.inject.Inject;
import timber.log.Timber;

/**
 * Example mortar screen.
 * Change what it extends as needed. Any View/Layout type is fine to extend
 */
public class NewVisitView extends RelativeLayout {

  /**
   * Make sure you are pointing at the correct presenter type
   * YourScreen.Presenter
   */
  @Inject
  NewVisitScreen.Presenter presenter;

  @Bind(R.id.person_container)
  ViewGroup personContainer;

  @Bind(R.id.view_state_primary)
  AppCompatButton primariesButton;

  public NewVisitView(Context context) {
    super(context);
    injectSelf(context);
  }

  public NewVisitView(Context context, AttributeSet attrs) {
    super(context, attrs);
    injectSelf(context);
  }

  public NewVisitView(Context context, AttributeSet attrs, int defStyleAttr) {
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
    DaggerService.<NewVisitScreen.Component>getDaggerComponent(context,
        DaggerService.DAGGER_SERVICE).inject(this);
  }

  @Override
  protected void onFinishInflate() {
    super.onFinishInflate();
    if (isInEditMode()) {
      return;
    }
    Timber.v("onFinishInflate");
    ButterKnife.bind(this, this);
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
    presenter.dropView(this);
  }

  public void showPrimary(StatePrimaryResponse.StatePrimary[] states, ApiAddress apiAddress) {
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

    Context context = getContext();
    String name = userState.getState();

    try {
      if (name != null) {
        String stateName = name.toLowerCase().replace(" ", "_");
        String packageName = context.getPackageName();
        int resId = getResources().getIdentifier(stateName, "drawable", packageName);
        Drawable img = context.getResources().getDrawable(resId);
        if (img != null) {
          primariesButton.setCompoundDrawablesWithIntrinsicBounds(img, null, null, null);
        }
      }

      primariesButton.setText(name);
      primariesButton.setVisibility(VISIBLE);
    } catch (Exception e) {

      Timber.e(e, "exception setting state icon/text");
      Crashlytics.logException(e);
      primariesButton.setVisibility(GONE);
    }
  }

  public void showPeople(final Visit visit) {

    personContainer.removeAllViews();

    for (CanvassData canvassItem : visit.included()) {
      if (canvassItem.type().equals(Person.TYPE)) {
        showPerson((Person) canvassItem);
      }
    }
  }

  @SuppressLint("SetTextI18n")
  private void showPerson(Person person) {
    View personRow =
        LayoutInflater.from(getContext()).inflate(R.layout.row_person, personContainer, false);

    TextView personName = (TextView) personRow.findViewById(R.id.person);
    ImageView partyIcon = (ImageView) personRow.findViewById(R.id.party);
    TextView supportLevel = (TextView) personRow.findViewById(R.id.interest);

    //personRow.findViewById(R.id.edit) //edit btn

    partyIcon.setImageResource(PartyIcon.get(person.attributes().party()));

    personName.setText(person.attributes().firstName() + " " + person.attributes().lastName());

    //TODO: canvass response is the machine readable format, this is not correct for i18n
    supportLevel.setText(CanvassResponseEvaluator.getText(person.attributes().canvassResponse(),
        getContext().getResources().getStringArray(R.array.interest)));

    personContainer.addView(personRow);

    personRow.findViewById(R.id.edit).setOnClickListener(onClickListener);
    personRow.findViewById(R.id.edit).setTag(person);

    ((CheckBox) personRow.findViewById(R.id.canvassed_checkbox)).setChecked(person.spokenTo());
    ((CheckBox) personRow.findViewById(R.id.canvassed_checkbox)).setOnCheckedChangeListener(
        onCheckChange);
    personRow.findViewById(R.id.canvassed_checkbox).setTag(person);
  }

  CompoundButton.OnCheckedChangeListener onCheckChange =
      new CompoundButton.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
          Person person = (Person) buttonView.getTag();
          person.spokenTo(isChecked);
        }
      };

  OnClickListener onClickListener = new OnClickListener() {
    @Override
    public void onClick(View v) {
      editPerson((Person) v.getTag());
    }
  };

  void editPerson(Person person) {
    Flow.get(this).set(new AddPersonScreen(person));
  }

  public void clearPersonCheckboxes() {
    for (int i = 0; i < personContainer.getChildCount(); i++) {
      View child = personContainer.getChildAt(i);
      ((CheckBox) child.findViewById(R.id.canvassed_checkbox)).setChecked(false);
      //the listeners should update the model, but let's be explicit
      Person person = (Person) child.findViewById(R.id.canvassed_checkbox).getTag();
      person.spokenTo(false);
    }
  }
}
