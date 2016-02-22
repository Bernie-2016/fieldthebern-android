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
import android.support.v4.widget.DrawerLayout;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import com.berniesanders.fieldthebern.FTBApplication;
import com.berniesanders.fieldthebern.R;
import com.berniesanders.fieldthebern.adapters.NavigationAdapter;
import com.berniesanders.fieldthebern.annotations.Layout;
import com.berniesanders.fieldthebern.controllers.PermissionService;
import com.berniesanders.fieldthebern.dagger.FtbScreenScope;
import com.berniesanders.fieldthebern.dagger.MainComponent;
import com.berniesanders.fieldthebern.events.LoginEvent;
import com.berniesanders.fieldthebern.models.User;
import com.berniesanders.fieldthebern.mortar.ParcelableScreen;
import com.berniesanders.fieldthebern.repositories.UserRepo;
import com.berniesanders.fieldthebern.views.NavigationView;
import com.squareup.otto.Subscribe;
import com.squareup.picasso.Picasso;
import flow.Flow;
import flow.History;
import javax.inject.Inject;
import mortar.ViewPresenter;
import timber.log.Timber;

/**
 * Example for creating new Mortar Screen that helps explain how it all works
 *
 * Set the @Layout annotation to the resource id of the layout for the screen
 * Layout only really needed for Flow screens
 */
@Layout(R.layout.screen_navigation_drawer)
public class NavigationScreen extends ParcelableScreen {

  /**
   * Not used in this case as flow isn't involved to call this method.
   * NavigationView injects itself directly
   */
  @Override
  public Object createComponent() {
    return null;
  }

  @Override
  public String getScopeName() {
    return NavigationScreen.class.getName();
  }

  //    @dagger.Module
  //    class Module {
  //    }

  public static final Creator<NavigationScreen> CREATOR = zeroArgsScreenCreator(NavigationScreen.class);

  /**
   * This component is used to inject the view with the presenter once the view is inflated.
   * The view will injected itself using this component on inflate.
   * Expose anything you want injected to the presenter here
   *
   * Only use "dependencies = MainComponent.class" if you need something from the main component
   * Only use "modules = Module.class" if you need a module
   */
  @FtbScreenScope
  @dagger.Component(dependencies = MainComponent.class)
  public interface Component {
    void inject(NavigationView t);
  }

  @FtbScreenScope
  static public class Presenter extends ViewPresenter<NavigationView> {

    private final UserRepo userRepo;
    // in case needed later to show expandable issue list
    DrawerLayout drawerLayout;

    @Bind(R.id.drawer_listview)
    ListView drawerListView;

    @Bind(R.id.drawer_header_avatar)
    ImageView avatar;

    @Bind(R.id.drawer_header_name)
    TextView name;

    @Bind(R.id.drawer_header_email)
    TextView email;

    /**
     * When the view is inflated, this presented is automatically injected to the View
     * Constructor parameters are injected here automatically
     */
    @Inject
    Presenter(UserRepo userRepo) {
      this.userRepo = userRepo;
    }

    /**
     * called when the presenter and view are ready.
     * getView() will not be null
     */
    @Override
    protected void onLoad(Bundle savedInstanceState) {
      Timber.v("onLoad");

      drawerLayout = (DrawerLayout) getView().getParent();
      ButterKnife.bind(this, getView());
      createNavigationDrawer();
      FTBApplication.getEventBus().register(this);

      if (userRepo.getCurrentUser() != null) {
        showUserInfo(userRepo.getCurrentUser());
      }
    }

    @Subscribe
    public void onLoginEvent(LoginEvent event) {
      switch (event.getEventType()) {
        case LoginEvent.LOGIN:
          showUserInfo(event.getUser());
          break;
        case LoginEvent.LOGOUT:
          clearUserInfo();
          break;
        default:
          //uh
          Timber.e("onLoginEvent unknown type");
      }
    }

    private void showUserInfo(User user) {
      Picasso.with(getView().getContext())
          .load(user.getData().attributes().getPhotoThumbUrl())
          .into(avatar);
      name.setText(user.getData().attributes().getFirstName() + " " + user.getData()
          .attributes()
          .getLastName());
      email.setText("");
    }

    private void clearUserInfo() {
      avatar.setImageResource(R.drawable.ic_face_white_48dp);
      name.setText("");
      email.setText("");
    }

    private void createNavigationDrawer() {

      drawerListView.setAdapter(new NavigationAdapter(
          //TODO externalize
          new String[] { "Canvassing", "Issues", "Learn", "Logout", "About" }, new int[] {
          R.drawable.ic_pin_drop_white_24dp, R.drawable.ic_issues,
          R.drawable.ic_live_help_white_24dp, R.drawable.ic_exit_to_app_white_24dp,
          R.drawable.ic_info_outline_white_24dp
      }));

      drawerListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
          final Flow flow = Flow.get(view);
          switch (position) {
            case 0:
              if (!(flow.getHistory().top() instanceof MapScreen)) {
                showMapScreen();
              }
              break;
            case 1:
              if (!(flow.getHistory().top() instanceof Main)) {
                view.post(new Runnable() {
                  @Override
                  public void run() {
                    flow.set(new Main());
                  }
                });
              }
              break;
            case 2:
              if (!(flow.getHistory().top() instanceof LearnScreen)) {
                view.post(new Runnable() {
                  @Override
                  public void run() {
                    flow.set(new LearnScreen());
                  }
                });
              }
              break;
            case 3:
              userRepo.logout();
              view.post(new Runnable() {
                @Override
                public void run() {
                  flow.setHistory(History.single(new ChooseSignupScreen()), Flow.Direction.REPLACE);
                }
              });
              break;
            case 4:
              if (!(flow.getHistory().top() instanceof AboutScreen)) {
                view.post(new Runnable() {
                  @Override
                  public void run() {
                    flow.set(new AboutScreen());
                  }
                });
              }
              break;
          }
          drawerLayout.closeDrawers();
        }
      });
    }

    private void showMapScreen() {
      if (PermissionService.get(getView()).isGranted()) {
        getView().post(new Runnable() {
          @Override
          public void run() {
            Flow.get(getView().getContext()).set(new MapScreen());
          }
        });
      } else {
        // Display a SnackBar with an explanation and a button to trigger the request.
        Snackbar.make(getView(), R.string.permission_contacts_rationale, Snackbar.LENGTH_INDEFINITE)
            .setAction(android.R.string.ok, new View.OnClickListener() {
              @Override
              public void onClick(View view) {
                PermissionService.get(getView()).requestPermission();
              }
            })
            .show();
      }
    }

    /**
     * called on rotation only -- may not be applicable here, because no-flow
     */
    @Override
    protected void onSave(Bundle outState) {
    }

    /**
     * last chance at the view before it is detached
     */
    @Override
    public void dropView(NavigationView view) {
      super.dropView(view);
      drawerLayout = null;
      drawerListView.setOnItemClickListener(null);
      drawerListView = null;
      FTBApplication.getEventBus().unregister(this);
    }

    @OnClick(R.id.drawer_profile)
    void onProfileClicked(final View v) {
      if (!(Flow.get(v).getHistory().top() instanceof ProfileScreen)) {
        Flow.get(v).set(new ProfileScreen());
      }

      drawerLayout.closeDrawers();
    }
  }
}
