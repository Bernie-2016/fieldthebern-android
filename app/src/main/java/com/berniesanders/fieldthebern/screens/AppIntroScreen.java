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
import com.berniesanders.fieldthebern.FTBApplication;
import com.berniesanders.fieldthebern.R;
import com.berniesanders.fieldthebern.annotations.Layout;
import com.berniesanders.fieldthebern.controllers.ActionBarService;
import com.berniesanders.fieldthebern.dagger.FtbScreenScope;
import com.berniesanders.fieldthebern.dagger.MainComponent;
import com.berniesanders.fieldthebern.mortar.ParcelableScreen;
import com.berniesanders.fieldthebern.views.AppIntroView;
import javax.inject.Inject;
import mortar.ViewPresenter;
import timber.log.Timber;

@Layout(R.layout.app_intro)
public class AppIntroScreen extends ParcelableScreen {

  public AppIntroScreen() {
  }

  @Override
  public Object createComponent() {
    return DaggerAppIntroScreen_Component.builder()
        .mainComponent(FTBApplication.getComponent())
        .appIntroModule(new AppIntroModule())
        .build();
  }

  @Override
  public String getScopeName() {
    return AppIntroScreen.class.getName();
  }

  @dagger.Module
  class AppIntroModule {

    public AppIntroModule() {
    }
  }

  public static final Creator<AppIntroScreen> CREATOR = zeroArgsScreenCreator(AppIntroScreen.class);

  @FtbScreenScope
  @dagger.Component(modules = AppIntroModule.class, dependencies = MainComponent.class)
  public interface Component {
    void inject(AppIntroView t);
  }

  @FtbScreenScope
  static public class Presenter extends ViewPresenter<AppIntroView> {

    @Inject
    Presenter() {
    }

    @Override
    protected void onLoad(Bundle savedInstanceState) {
      Timber.v("onLoad");
      setActionBar();
    }

    void setActionBar() {
      ActionBarService.get(getView()).hideToolbar().closeAppbar().lockDrawer();
    }

    @Override
    protected void onSave(Bundle outState) {
    }

    @Override
    public void dropView(AppIntroView view) {
      super.dropView(view);
    }
  }
}
