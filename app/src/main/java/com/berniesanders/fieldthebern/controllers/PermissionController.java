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

import android.annotation.TargetApi;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import com.berniesanders.fieldthebern.apilevels.PermissionUtil;
import dagger.Module;
import dagger.Provides;
import javax.inject.Singleton;
import mortar.Presenter;
import mortar.bundler.BundleService;
import rx.functions.Action0;
import timber.log.Timber;

import static android.Manifest.permission.ACCESS_COARSE_LOCATION;
import static android.Manifest.permission.ACCESS_FINE_LOCATION;
import static android.Manifest.permission.GET_ACCOUNTS;
import static android.Manifest.permission.READ_EXTERNAL_STORAGE;
import static android.content.pm.PackageManager.PERMISSION_GRANTED;
import static mortar.bundler.BundleService.getBundleService;

/**
 * Provides a way to coordinate with the MainActivity to request permissions
 */
public class PermissionController extends Presenter<PermissionController.Activity> {

  public static final int REQ_CODE_PERMISSIONS = 33;
  private Action0 onComplete;
  private Action0 onFail;

  public interface Activity {
    AppCompatActivity getActivity();
  }

  /**
   */
  PermissionController() {
  }

  @Override
  public void onLoad(Bundle savedInstanceState) {
  }

  @Override
  public void dropView(Activity view) {
    //after this it is no longer safe to call getView()
    super.dropView(view);
  }

  public boolean isGranted() {
    return !(Build.VERSION.SDK_INT >= 23 &&
        ContextCompat.checkSelfPermission(getView().getActivity(), ACCESS_FINE_LOCATION)
            != PERMISSION_GRANTED &&
        ContextCompat.checkSelfPermission(getView().getActivity(), ACCESS_COARSE_LOCATION)
            != PERMISSION_GRANTED &&
        ContextCompat.checkSelfPermission(getView().getActivity(), GET_ACCOUNTS)
            != PERMISSION_GRANTED);
  }

  public boolean isPhotoGranted() {
    return !(Build.VERSION.SDK_INT >= 23
        && ContextCompat.checkSelfPermission(getView().getActivity(), READ_EXTERNAL_STORAGE)
        != PERMISSION_GRANTED);
  }

  public void requestPermission() {
    //Action0 onComplete
    //this.onComplete = onComplete;
    if (!isGranted()) {
      String[] permissions = { ACCESS_FINE_LOCATION, ACCESS_COARSE_LOCATION, GET_ACCOUNTS };
      ActivityCompat.requestPermissions(getView().getActivity(), permissions, REQ_CODE_PERMISSIONS);
    }
  }

  @TargetApi(23)
  public void requestGalleryPermission(Action0 onComplete, Action0 onFail) {
    this.onComplete = onComplete;
    this.onFail = onFail;
    //Action0 onComplete
    //this.onComplete = onComplete;
    if (!isPhotoGranted()) {
      String[] permissions = { READ_EXTERNAL_STORAGE };
      ActivityCompat.requestPermissions(getView().getActivity(), permissions, REQ_CODE_PERMISSIONS);
    }
  }

  /**
   * @param grantResults either android.content.pm.PackageManager.PERMISSION_GRANTED or
   * android.content.pm.PackageManager.PERMISSION_DENIED.
   */
  public void onResult(int reqCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

    if (PermissionUtil.verifyPermissions(grantResults)) {
      if (onComplete != null) {
        onComplete.call();
        Timber.v("permissions granted...");
      }
    } else {
      if (onFail != null) {
        onFail.call();
        Timber.v("permission failed...");
      }
    }

    onFail = null;
    onComplete = null;
  }

  /**
   * required by mortar
   */
  @Override
  protected BundleService extractBundleService(Activity activity) {
    return getBundleService(activity.getActivity());
  }

  /**
   */
  @Module
  public static class PermissionModule {

    @Provides
    @Singleton
    PermissionController providePermissionController() {
      return new PermissionController();
    }
  }
}
