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
import butterknife.ButterKnife;
import com.berniesanders.fieldthebern.FTBApplication;
import com.berniesanders.fieldthebern.R;
import com.berniesanders.fieldthebern.annotations.Layout;
import com.berniesanders.fieldthebern.dagger.FtbScreenScope;
import com.berniesanders.fieldthebern.dagger.MainComponent;
import com.berniesanders.fieldthebern.mortar.ParcelableScreen;
import com.berniesanders.fieldthebern.views.ExampleView;
import dagger.Provides;
import javax.inject.Inject;
import mortar.ViewPresenter;
import timber.log.Timber;

/**
 * Example for creating new Mortar Screen that helps explain how it all works.
 * Set the @Layout annotation to the resource id of the layout for the screen
 */
@Layout(R.layout.screen_example)
public class ExampleScreen extends ParcelableScreen {

  private final String someData;

  /**
   * Constructor called by Flow throughout the app
   *
   * Example:
   * Flow.get(context).set(new ExampleScreen("Some Data To Pass");
   *
   * Note:
   * Generally common types like "String" are not injected because injection works based on type
   */
  public ExampleScreen(String someData) {
    this.someData = someData;
  }

  /**
   * Create the component defined as an inner class below.
   * This component will inject the presenter on the view, and dependencies/module on the
   * presenter.
   * You can pass data (someData) from the Screen to its Presenter through this component.
   * Remember you must run the gradle 'build' class for Dagger to generate to component code
   *
   * Note:
   * Generally common types like "String" are not injected because injection works based on type
   */
  @Override
  public Object createComponent() {
    return DaggerExampleScreen_Component.builder()
        .mainComponent(
            FTBApplication.getComponent()) //must set if module has (dependencies = MainComponent.class)
        .exampleModule(new ExampleModule(someData)) //pass data to the presenter here
        .build();
  }

  @Override
  public String getScopeName() {
    return ExampleScreen.class.getName();
  }

  @Override protected void doWriteToParcel(Parcel parcel, int flags) {
    parcel.writeString(someData);
  }

  public static final Creator<ExampleScreen> CREATOR = new ScreenCreator<ExampleScreen>() {
    @Override protected ExampleScreen doCreateFromParcel(Parcel source) {
      String someData = source.readString();
      return new ExampleScreen(someData);
    }

    @Override public ExampleScreen[] newArray(int size) {
      return new ExampleScreen[size];
    }
  };

  @dagger.Module
  class ExampleModule {

    private final String someDataToInject;

    /**
     * pass variables to the component that will then be injected to the presenter
     */
    public ExampleModule(String someDataToInject) {
      this.someDataToInject = someDataToInject;
    }

    @Provides
    String provideSomeData() {
      return someDataToInject;
    }
  }

  /**
   * This component is used to inject the view with the presenter once the view is inflated.
   * The view will injected itself using this component on inflate.
   * Expose anything you want injected to the presenter here
   * Only use "dependencies = MainComponent.class" if you need something from the main component
   * Only use "modules = ExampleModule.class" if you need a module
   */
  @FtbScreenScope
  @dagger.Component(modules = ExampleModule.class, dependencies = MainComponent.class)
  public interface Component {
    /**
     * injection target = the view (ExampleView) to have the presented injected on it
     */
    void inject(ExampleView t);

    // Expose anything you want injected to the presenter here
    // such as Gson from the MainComponent

    // Gson exposeGson();
  }

  @FtbScreenScope
  static public class Presenter extends ViewPresenter<ExampleView> {

    /**
     * Since the presenter is static it should survive rotation
     */
    private final String someInjectedData;

    /**
     * When the view is inflated, this presented is automatically injected to the ExampleView
     * Constructor parameters here are injected automatically
     */
    @Inject
    Presenter(String someInjectedData) {
      this.someInjectedData = someInjectedData;
    }

    /**
     * called when the presenter and view are ready.
     * getView() will not be null
     *
     * @param savedInstanceState This bundle is only passed on rotation not passed on navigating
     * back
     */
    @Override
    protected void onLoad(Bundle savedInstanceState) {
      Timber.v("onLoad");
      ButterKnife.bind(this, getView());
    }

    //        void setActionBar() {
    //            ActionBarController.MenuAction menu =
    //                    new ActionBarController
    //                            .MenuAction()
    //                            .setIsSearch();
    //            ActionBarService
    //                    .get(getView())
    //                    .setMainImage(null)
    //                    .setConfig(new ActionBarController.Config("actionbar title", menu));
    //        }

    /**
     * Called on rotation only
     */
    @Override
    protected void onSave(Bundle outState) {
    }

    /**
     * Last chance at the view before it is detached.
     * You can save state with hack, (restore it the same way by reading the field).
     * objects saved with be "parceled" by gson. Example:
     *
     * ((ExampleScreen)Path.get(view.getContext())).somePublicField = "Something you want to save"
     */
    @Override
    public void dropView(ExampleView view) {
      super.dropView(view);
      ButterKnife.unbind(this);
    }
  }
}
