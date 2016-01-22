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

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import com.berniesanders.fieldthebern.R;
import com.crashlytics.android.Crashlytics;
import dagger.Module;
import dagger.Provides;
import javax.inject.Singleton;
import mortar.Presenter;
import mortar.bundler.BundleService;
import rx.functions.Action1;
import timber.log.Timber;

import static mortar.bundler.BundleService.getBundleService;

/**
 * Provides a way to coordinate with the MainActivity to request permissions
 */
public class PhotoController extends Presenter<PhotoController.Activity> {

  static final int REQUEST_IMAGE_CAPTURE = 1;
  static final int PICK_PHOTO = 2;
  /**
   * passes the thumbnail of the photo taken/picked or null if cancelled
   *
   * @param bitmap
   */
  private Action1<Bitmap> onComplete;

  public interface Activity {
    AppCompatActivity getActivity();
  }

  /**
   */
  PhotoController() {
  }

  @Override
  public void onLoad(Bundle savedInstanceState) {
  }

  @Override
  public void dropView(Activity view) {
    //after this it is no longer safe to call getView()
    super.dropView(view);
  }

  public void takePhoto(Action1<Bitmap> onComplete) {
    this.onComplete = onComplete;
    Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
    if (takePictureIntent.resolveActivity(getView().getActivity().getPackageManager()) != null) {
      getView().getActivity().startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
    }
  }

  public void pickImage(Action1<Bitmap> onComplete) {
    this.onComplete = onComplete;
    Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
    intent.setType("image/*");
    getView().getActivity().startActivityForResult(intent, PICK_PHOTO);
  }

  /**
   *
   */
  public void onResult(int requestCode, int resultCode, Intent data) {
    //onComplete.call();
    Timber.v("activity result...");

    //TODO this is really hacky code so let's fail gracefully for now
    try {
      if (requestCode == REQUEST_IMAGE_CAPTURE) {
        Bitmap imageBitmap = null;

        if (resultCode == android.app.Activity.RESULT_OK) {
          Bundle extras = data.getExtras();
          imageBitmap = (Bitmap) extras.get("data");
        }
        if (onComplete != null) {
          onComplete.call(imageBitmap);
        }
      } else if (requestCode == PICK_PHOTO) {
        Bitmap imageBitmap = null;

        if (resultCode == android.app.Activity.RESULT_OK) {
          Uri imageUri = data.getData();

          //TODO this code sucks
          // How is it the api for interacting with the media store
          // is still manually iterating with a cursor...?!
          long id = Long.parseLong(imageUri.getLastPathSegment()
              .substring(imageUri.getLastPathSegment().lastIndexOf(":") + 1));

          imageBitmap = MediaStore.Images.Thumbnails.getThumbnail(
              getView().getActivity().getContentResolver(), id,
              MediaStore.Images.Thumbnails.MINI_KIND, null);
        }
        if (onComplete != null) {
          onComplete.call(imageBitmap);
        }
      }
    } catch (Exception e) {
      Timber.e(e, "Error loading image");
      Crashlytics.logException(e);

      if (getView() != null) {
        String errmsg = getView().getActivity().getString(R.string.err_cant_load_photo);
        ToastService.get(getView().getActivity()).bern(errmsg);
      }
    }
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
  public static class PhotoModule {

    @Provides
    @Singleton
    PhotoController providePhotoontroller() {
      return new PhotoController();
    }
  }
}
