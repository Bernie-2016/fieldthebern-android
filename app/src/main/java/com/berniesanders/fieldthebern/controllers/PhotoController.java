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
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.ParcelFileDescriptor;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.webkit.MimeTypeMap;

import com.berniesanders.fieldthebern.R;
import com.crashlytics.android.Crashlytics;
import com.google.common.collect.Sets;

import java.io.FileDescriptor;
import java.util.HashSet;

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
  static final int THUMBNAIL_MINI_WIDTH = 512;
  static final int THUMBNAIL_MINI_HEIGHT = 384;
  static final HashSet<String> acceptedMimeTypes =
          Sets.newHashSet("image/jpeg","image/png","image/gif");

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

          String imagePath = imageUri.getPath();
          //If the uri comes from the default document picker, we don't have to worry about the
          // extension. Android will already have made sure it is an image.
          if (!"com.android.providers.media.documents".equals(imageUri.getAuthority())) {
            //The user chose to use the file browser to select the uri, it might not be an image.
            //Check the type to make sure it's accepted.
            String extension = MimeTypeMap.getFileExtensionFromUrl(imageUri.getPath());
            if (extension != null) {
              MimeTypeMap mime = MimeTypeMap.getSingleton();
              String type = mime.getMimeTypeFromExtension(extension);

              if (!acceptedMimeTypes.contains(type)) {
                showError(getView().getActivity().getString(R.string.err_cant_load_photo_bad_extension));
                return;
              }
            } else {
              showError(getView().getActivity().getString(R.string.err_cant_load_photo_bad_extension));
              return;
            }
          }

          //File is an accepted type. Get the bitmap
          ParcelFileDescriptor parcelFileDescriptor =
                  getView().getActivity().getContentResolver().openFileDescriptor(imageUri, "r");
          FileDescriptor fileDescriptor = parcelFileDescriptor.getFileDescriptor();
          Bitmap image = BitmapFactory.decodeFileDescriptor(fileDescriptor);
          parcelFileDescriptor.close();

          imageBitmap = Bitmap.createScaledBitmap(image, THUMBNAIL_MINI_WIDTH, THUMBNAIL_MINI_HEIGHT, false);

        }
        if (onComplete != null) {
          onComplete.call(imageBitmap);
        }
      }
    } catch (Exception e) {
      Timber.e(e, "Error loading image");
      Crashlytics.logException(e);


      showError(getView().getActivity().getString(R.string.err_cant_load_photo));

    }
  }

  private void showError(String errmsg) {
    if (getView() != null) {
      ToastService.get(getView().getActivity()).bern(errmsg);
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
