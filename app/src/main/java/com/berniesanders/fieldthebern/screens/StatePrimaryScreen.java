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
import butterknife.BindString;
import butterknife.ButterKnife;
import com.berniesanders.fieldthebern.FTBApplication;
import com.berniesanders.fieldthebern.R;
import com.berniesanders.fieldthebern.annotations.Layout;
import com.berniesanders.fieldthebern.controllers.ActionBarController;
import com.berniesanders.fieldthebern.controllers.ActionBarService;
import com.berniesanders.fieldthebern.dagger.FtbScreenScope;
import com.berniesanders.fieldthebern.dagger.MainComponent;
import com.berniesanders.fieldthebern.models.ApiAddress;
import com.berniesanders.fieldthebern.models.StatePrimaryResponse;
import com.berniesanders.fieldthebern.mortar.ParcelableScreen;
import com.berniesanders.fieldthebern.repositories.StatesRepo;
import com.berniesanders.fieldthebern.repositories.VisitRepo;
import com.berniesanders.fieldthebern.views.StatePrimaryView;
import dagger.Provides;
import flow.Flow;
import flow.History;
import javax.inject.Inject;
import mortar.ViewPresenter;
import rx.Observer;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action0;
import rx.schedulers.Schedulers;
import timber.log.Timber;

/**
 * Mortar screen for State primaries
 */
@Layout(R.layout.screen_state_primary)
public class StatePrimaryScreen extends ParcelableScreen {

  private final ApiAddress apiAddress;

  public StatePrimaryScreen(ApiAddress apiAddress) {
    this.apiAddress = apiAddress;
  }

  @Override
  public Object createComponent() {
    return DaggerStatePrimaryScreen_Component.builder()
        .mainComponent(FTBApplication.getComponent())
        .module(new Module(apiAddress))
        .build();
  }

  @Override
  public String getScopeName() {
    return StatePrimaryScreen.class.getName();
  }

  @Override protected void doWriteToParcel(Parcel parcel, int flags) {
    parcel.writeParcelable(apiAddress, 0);
  }

  public static final Parcelable.Creator<StatePrimaryScreen>
      CREATOR = new ParcelableScreen.ScreenCreator<StatePrimaryScreen>() {
    @Override protected StatePrimaryScreen doCreateFromParcel(Parcel source) {
      ApiAddress apiAddress = source.readParcelable(ApiAddress.class.getClassLoader());
      return new StatePrimaryScreen(apiAddress);
    }

    @Override public StatePrimaryScreen[] newArray(int size) {
      return new StatePrimaryScreen[size];
    }
  };

  @dagger.Module
  class Module {
    private final ApiAddress apiAddress;

    Module(ApiAddress apiAddress) {
      this.apiAddress = apiAddress;
    }

    @Provides
    @FtbScreenScope
    public ApiAddress provideApiAddress() {
      return apiAddress;
    }
  }

  @FtbScreenScope
  @dagger.Component(dependencies = MainComponent.class, modules = Module.class)
  public interface Component {
    void inject(StatePrimaryView t);

    ApiAddress apiAddress();

    VisitRepo visitRepo();

    StatesRepo statesRepo();
  }

  @FtbScreenScope
  static public class Presenter extends ViewPresenter<StatePrimaryView> {

    private final VisitRepo visitRepo;
    private final ApiAddress apiAddress;
    private final StatesRepo statesRepo;

    Subscription subscription;
    private StatePrimaryResponse.StatePrimary[] statePrimaries;

    @BindString(android.R.string.cancel)
    String cancel;

    @Inject
    Presenter(ApiAddress apiAddress, VisitRepo visitRepo, StatesRepo statesRepo) {
      this.visitRepo = visitRepo;
      this.apiAddress = apiAddress;
      this.statesRepo = statesRepo;
    }

    @Override
    protected void onLoad(Bundle savedInstanceState) {
      Timber.v("onLoad");
      ButterKnife.bind(this, getView());
      setActionBar();

      subscription = statesRepo.getStatePrimaries()
          .observeOn(AndroidSchedulers.mainThread())
          .subscribeOn(Schedulers.io())
          .subscribe(observer);
    }

    Observer<StatePrimaryResponse.StatePrimary[]> observer =
        new Observer<StatePrimaryResponse.StatePrimary[]>() {
          @Override
          public void onCompleted() {
            if (getView() == null) {
              return;
            }
            getView().populateStateInfo(statePrimaries, apiAddress);
          }

          @Override
          public void onError(Throwable e) {
            Timber.e("Error on stateprimaryobserver");
          }

          @Override
          public void onNext(StatePrimaryResponse.StatePrimary[] statePrimary) {
            Presenter.this.statePrimaries = statePrimary;
          }
        };

    @Override
    public void dropView(StatePrimaryView view) {
      super.dropView(view);
      ButterKnife.unbind(this);
    }

    void setActionBar() {
      ActionBarController.MenuAction menu =
          new ActionBarController.MenuAction().label(cancel).action(new Action0() {
            @Override
            public void call() {
              visitRepo.clear();
              Flow.get(getView())
                  .setHistory(History.single(new HomeScreen()), Flow.Direction.BACKWARD);
            }
          });
      ActionBarService.get(getView())
          .showToolbar()
          .closeAppbar()
          .setMainImage(null)
          .setConfig(new ActionBarController.Config("Primary Information", menu));
    }
  }
}
