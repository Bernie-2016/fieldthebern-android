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
import android.support.design.widget.Snackbar;
import android.view.View;
import butterknife.BindString;
import butterknife.ButterKnife;
import butterknife.OnClick;
import com.berniesanders.fieldthebern.R;
import com.berniesanders.fieldthebern.annotations.Layout;
import com.berniesanders.fieldthebern.controllers.ActionBarController;
import com.berniesanders.fieldthebern.controllers.ActionBarService;
import com.berniesanders.fieldthebern.controllers.PermissionService;
import com.berniesanders.fieldthebern.dagger.FtbScreenScope;
import com.berniesanders.fieldthebern.mortar.FlowPathBase;
import com.berniesanders.fieldthebern.views.HomeView;
import flow.Flow;
import javax.inject.Inject;
import mortar.ViewPresenter;
import timber.log.Timber;

/**
 */
@Layout(R.layout.screen_home)
public class HomeScreen extends FlowPathBase {
  /**
   */
  public HomeScreen() {
  }

  /**
   */
  @Override
  public Object createComponent() {
    return DaggerHomeScreen_Component.builder().build();
  }

  /**
   */
  @Override
  public String getScopeName() {
    // note someData.hashCode() makes the screen unique
    return HomeScreen.class.getName();
  }

  /**
   */
  @FtbScreenScope
  @dagger.Component()
  public interface Component {
    /**
     */
    void inject(HomeView t);
  }

  @FtbScreenScope
  static public class Presenter extends ViewPresenter<HomeView> {

    @BindString(R.string.app_name)
    String screenTitle;

    /**
     */
    @Inject
    Presenter() {
    }

    /**
     */
    @Override
    protected void onLoad(Bundle savedInstanceState) {
      Timber.v("onLoad");
      ButterKnife.bind(this, getView());
      ActionBarService.get(getView())
          .showToolbar()
          .closeAppbar()
          .unlockDrawer()
          .setConfig(new ActionBarController.Config(screenTitle, null));

      PermissionService.get(getView()).requestPermission();
    }

    /**
     * called on rotation only
     */
    @Override
    protected void onSave(Bundle outState) {
    }

    /**
     */
    @Override
    public void dropView(HomeView view) {
      super.dropView(view);
      ButterKnife.unbind(this);
    }

    @OnClick(R.id.screen_home_canvass)
    void onCanvassClicked(final View v) {

      if (PermissionService.get(v).isGranted()) {
        v.post(new Runnable() {
          @Override
          public void run() {
            Flow.get(v).set(new MapScreen());
          }
        });
      } else {
        // Display a SnackBar with an explanation and a button to trigger the request.
        Snackbar.make(v, R.string.permission_contacts_rationale, Snackbar.LENGTH_INDEFINITE)
            .setAction(android.R.string.ok, new View.OnClickListener() {
              @Override
              public void onClick(View view) {
                PermissionService.get(getView()).requestPermission();
              }
            })
            .show();
      }
    }

    @OnClick(R.id.screen_home_issues)
    void onIssuesClicked(final View v) {
      v.post(new Runnable() {
        @Override
        public void run() {
          Flow.get(v).set(new Main());
        }
      });
    }

    @OnClick(R.id.screen_home_learn)
    void onLearnClicked(final View v) {
      v.post(new Runnable() {
        @Override
        public void run() {
          Flow.get(v).set(new LearnScreen());
        }
      });
    }
  }
}
