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

import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import android.view.View;
import butterknife.BindString;
import butterknife.ButterKnife;
import butterknife.OnClick;
import com.berniesanders.fieldthebern.FTBApplication;
import com.berniesanders.fieldthebern.R;
import com.berniesanders.fieldthebern.annotations.Layout;
import com.berniesanders.fieldthebern.controllers.ActionBarController;
import com.berniesanders.fieldthebern.controllers.ActionBarService;
import com.berniesanders.fieldthebern.dagger.FtbScreenScope;
import com.berniesanders.fieldthebern.dagger.MainComponent;
import com.berniesanders.fieldthebern.models.Person;
import com.berniesanders.fieldthebern.models.Score;
import com.berniesanders.fieldthebern.models.Visit;
import com.berniesanders.fieldthebern.models.VisitResult;
import com.berniesanders.fieldthebern.mortar.ParcelableScreen;
import com.berniesanders.fieldthebern.views.ScoreView;
import dagger.Provides;
import flow.Flow;
import flow.History;
import javax.inject.Inject;
import mortar.ViewPresenter;
import timber.log.Timber;

/**
 * Example for creating new Mortar Screen that helps explain how it all works
 *
 * Set the @Layout annotation to the resource id of the layout for the screen
 */
@Layout(R.layout.screen_score)
public class ScoreScreen extends ParcelableScreen {

  private final VisitResult visitResult;
  private final Visit visit;

  /**
   * @param visitResult
   * @param visit
   */
  public ScoreScreen(VisitResult visitResult, Visit visit) {
    this.visitResult = visitResult;
    this.visit = visit;
  }

  /**
   */
  @Override
  public Object createComponent() {
    return DaggerScoreScreen_Component.builder()
        .mainComponent(FTBApplication.getComponent())
        .module(new Module(visitResult, visit))
        .build();
  }

  /**
   */
  @Override
  public String getScopeName() {
    return ScoreScreen.class.getName();
  }

  @Override
  protected void doWriteToParcel(Parcel parcel, int flags) {
    parcel.writeParcelable(visitResult, 0);
    parcel.writeParcelable(visit, 0);
  }

  public static final Parcelable.Creator<ScoreScreen>
      CREATOR = new ScoreScreen.ScreenCreator<ScoreScreen>() {
    @Override
    protected ScoreScreen doCreateFromParcel(Parcel source) {
      VisitResult visitResult = source.readParcelable(VisitResult.class.getClassLoader());
      Visit visit = source.readParcelable(Visit.class.getClassLoader());
      return new ScoreScreen(visitResult, visit);
    }

    @Override
    public ScoreScreen[] newArray(int size) {
      return new ScoreScreen[size];
    }
  };

  @dagger.Module
  class Module {

    private final VisitResult visitResult;
    private final Visit visit;

    public Module(VisitResult visitResult, Visit visit) {
      this.visitResult = visitResult;
      this.visit = visit;
    }

    @Provides
    @FtbScreenScope
    public VisitResult provideVisitResult() {
      return visitResult;
    }

    @Provides
    @FtbScreenScope
    public Visit provideVisit() {
      return visit;
    }
  }

  /**
   */
  @FtbScreenScope
  @dagger.Component(modules = Module.class, dependencies = MainComponent.class)
  public interface Component {
    void inject(ScoreView t);

    VisitResult visitResult();

    Visit visit();
  }

  @FtbScreenScope
  static public class Presenter extends ViewPresenter<ScoreView> {

    private final VisitResult visitResult;
    private final Visit visit;
    @BindString(R.string.score)
    String screenTitleString;

    @Inject
    Presenter(VisitResult visitResult, Visit visit) {
      this.visitResult = visitResult;
      this.visit = visit;
    }

    @Override
    protected void onLoad(Bundle savedInstanceState) {
      Timber.v("onLoad");
      ButterKnife.bind(this, getView());
      setActionBar();

      //let the gif load a bit
      getView().post(() -> {
        String firstName = null;
        try {
          // TODO This doesn't account for who the canvasser actually talked to,
          // we just grab the first name
          Person person = (Person) visit.included().get(1);
          firstName = person.attributes().firstName();
        } catch (IndexOutOfBoundsException e) {
          //this can happen if someone asked us to leave without giving their name
          Timber.w(e, "no person update found");
        }

        Score score = visitResult.included().get(0);
        int points = score.attributes().pointsForKnock() + score.attributes().pointsForUpdates();
        getView().animateScore(points);
        getView().animateLabels(score.attributes().pointsForKnock(),
            score.attributes().pointsForUpdates(), firstName);
      });
    }

    void setActionBar() {
      ActionBarService.get(getView())
          .showToolbar()
          .closeAppbar()
          .setMainImage(null)
          .setConfig(new ActionBarController.Config(screenTitleString, null));
    }

    @Override
    protected void onSave(Bundle outState) {
    }

    @Override
    public void dropView(ScoreView view) {
      super.dropView(view);
      ButterKnife.unbind(this);
    }

    @OnClick(R.id.back_to_map)
    public void backToMap(final View v) {
      v.post(() -> Flow.get(v)
          .setHistory(History.single(new MapScreen()), Flow.Direction.BACKWARD));
    }
  }
}
