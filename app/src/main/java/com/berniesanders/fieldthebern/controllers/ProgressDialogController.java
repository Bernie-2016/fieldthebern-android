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

package com.berniesanders.fieldthebern.controllers;

/**
 *
 */

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.internal.view.ContextThemeWrapper;
import com.berniesanders.fieldthebern.R;
import dagger.Module;
import dagger.Provides;
import javax.inject.Singleton;
import mortar.Presenter;
import mortar.bundler.BundleService;
import timber.log.Timber;

import static mortar.bundler.BundleService.getBundleService;

/**
 * This is mostly just a tool for our toolbox.
 *
 * Ideally, when showing a "progress spinning wheel" it should be shown in-page or in-context.
 * For example, when the user clicks a submit button, we should replace that with the loading
 * progress spinning wheel instead of using a dialog.
 */
public class ProgressDialogController extends Presenter<ProgressDialogController.Activity> {

  public interface Activity {
    AppCompatActivity getActivity();
  }

  ProgressDialog dialog;

  /**
   * This is mostly just a tool for our toolbox.
   *
   * Ideally, when showing a "progress spinning wheel" it should be shown in-page or in-context.
   * For example, when the user clicks a submit button, we should replace that with the loading
   * progress spinning wheel instead of using a dialog.
   */
  ProgressDialogController() {
  }

  /////////////////////// Mortar //////////////////////////////////////
  @Override public void onLoad(Bundle savedInstanceState) {
  }

  @Override public void dropView(Activity view) {
    super.dropView(view);
    dismiss();
  }

  @Override protected BundleService extractBundleService(Activity activity) {
    return getBundleService(activity.getActivity());
  }

  @Module public static class ProgressDialogModule {

    @Provides @Singleton ProgressDialogController provideTemplateController() {
      return new ProgressDialogController();
    }
  }

  /////////////////////// Dialog //////////////////////////////////////

  /**
   * Shows a dialog.  Caller is responsible for calling show again after rotation if the
   * dialog needs to remain visible
   *
   * This is mostly just a tool for our toolbox.
   *
   * Ideally, when showing a "progress spinning wheel" it should be shown in-page or in-context.
   * For example, when the user clicks a submit button, we should replace that with the loading
   * progress spinning wheel instead of using a dialog.
   */
  public void show(int titleResID, int messageResID) {
    if (getView() == null) {
      Timber.w("show() called but no view was attached");
      return;
    }

    show(getView().getActivity().getString(titleResID),
        getView().getActivity().getString(messageResID));
  }

  /**
   * Shows a dialog.  Caller is responsible for calling show again after rotation if the
   * dialog needs to remain visible
   *
   * This is mostly just a tool for our toolbox.
   *
   * Ideally, when showing a "progress spinning wheel" it should be shown in-page or in-context.
   * For example, when the user clicks a submit button, we should replace that with the loading
   * progress spinning wheel instead of using a dialog.
   */
  public void show(int messageResID) {
    if (getView() == null) {
      Timber.w("show() called but no view was attached");
      return;
    }

    show(null, getView().getActivity().getString(messageResID));
  }

  /**
   * Shows a dialog.  Caller is responsible for calling show again after rotation if the
   * dialog needs to remain visible
   *
   * This is mostly just a tool for our toolbox.
   *
   * Ideally, when showing a "progress spinning wheel" it should be shown in-page or in-context.
   * For example, when the user clicks a submit button, we should replace that with the loading
   * progress spinning wheel instead of using a dialog.
   */
  public void show(String title, String message) {
    if (getView() == null) {
      Timber.w("show() called but no view was attached");
      return;
    }

    //dialog = ProgressDialog.show(getView().getActivity(), title, message);
    dialog =
        new ProgressDialog(new ContextThemeWrapper(getView().getActivity(), R.style.DarkDialog));
    dialog.setMessage(message);
    if (title != null) {
      dialog.setTitle(title);
    }
    dialog.show();
  }

  public void dismiss() {
    if (dialog != null && dialog.isShowing()) {
      dialog.dismiss();
      dialog = null;
    }
  }
}
