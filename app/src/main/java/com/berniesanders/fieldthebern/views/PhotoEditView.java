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

package com.berniesanders.fieldthebern.views;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import com.berniesanders.fieldthebern.R;
import com.berniesanders.fieldthebern.controllers.PermissionService;
import com.berniesanders.fieldthebern.controllers.PhotoService;
import com.berniesanders.fieldthebern.media.SaveImageTarget;
import com.berniesanders.fieldthebern.models.UserAttributes;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;
import rx.functions.Action0;
import rx.functions.Action1;
import timber.log.Timber;

/**
 * Displays current profile photo and options to replace from the camera or gallery picker.
 */
public class PhotoEditView extends FrameLayout {
  private PhotoChangeListener photoChangeListener;
  private boolean avatarButtonSliderOpen;

  @Bind(R.id.user_photo)
  ImageView userImageView;

  @Bind(R.id.mask)
  ImageView mask;

  @Bind(R.id.avatar_buttons)
  View avatarButtons;

  @Bind(R.id.avatar_container)
  View avatarContainer;

  public interface PhotoChangeListener {
    void onPhotoChanged(Bitmap bitmap, String base64PhotoData);
  }

  public PhotoEditView(Context context) {
    super(context);
  }

  public PhotoEditView(Context context, AttributeSet attrs) {
    super(context, attrs);
  }

  public PhotoEditView(Context context, AttributeSet attrs, int defStyleAttr) {
    super(context, attrs, defStyleAttr);
  }

  public void setPhotoChangeListener(PhotoChangeListener photoChangeListener) {
    this.photoChangeListener = photoChangeListener;
  }

  @Override
  protected void onFinishInflate() {
    super.onFinishInflate();
    if (isInEditMode()) {
      return;
    }
    ButterKnife.bind(this);
  }

  public void load(UserAttributes userAttributes, @Nullable Bitmap userPhoto) {
    if (userPhoto != null) {
      showPhoto(userPhoto);
      return;
    }

    Picasso.with(getContext()).load(userAttributes.getPhotoLargeUrl()).into(new Target() {
      @Override
      public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
        showPhoto(bitmap);
      }

      @Override
      public void onBitmapFailed(Drawable errorDrawable) {
        Timber.w("Failed to load user photo bitmap");
      }

      @Override
      public void onPrepareLoad(Drawable placeHolderDrawable) {
      }
    });
  }

  private void notifyPhotoChange(Bitmap bitmap, String b64) {
    if (photoChangeListener != null) {
      photoChangeListener.onPhotoChanged(bitmap, b64);
    }
  }

  @OnClick(R.id.user_photo)
  void showAvatarButtons() {
    if (avatarButtonSliderOpen) {
      toggleAvatarWidget(false); //close the widget
    } else {
      toggleAvatarWidget(true); //open the widget
    }
  }

  private void showPhoto(@NonNull Bitmap bitmap) {
    userImageView.setImageBitmap(bitmap);
    mask.setVisibility(View.VISIBLE);
    post(new Runnable() {
      @Override
      public void run() {
        toggleAvatarWidget(false);
      }
    });
  }

  public void toggleAvatarWidget(boolean open) {
    float center = avatarContainer.getResources().getDisplayMetrics().widthPixels / 2;

    if (open) {
      avatarContainer.animate().x(20).setDuration(200).start();
      avatarButtons.setAlpha(0);
      avatarButtons.setVisibility(View.VISIBLE);
      avatarButtons.setX(0);
      avatarButtons.animate()
          .x(avatarContainer.getWidth())
          .alpha(1)
          .setStartDelay(150)
          .setDuration(200)
          .start();
      avatarButtonSliderOpen = true;
    } else {
      //close the widget
      avatarContainer.animate().x(center - avatarContainer.getWidth() / 2).setDuration(200).start();
      avatarButtons.animate().alpha(0).setDuration(75).start();
      avatarButtonSliderOpen = false;
    }
  }

  private Action1<Bitmap> receiveNewBitmap = new Action1<Bitmap>() {
    @Override
    public void call(Bitmap bitmap) {
      if (bitmap != null) {
        showPhoto(bitmap);
        String base64 = SaveImageTarget.base64EncodeBitmap(bitmap, getContext());
        notifyPhotoChange(bitmap, base64);
      }
    }
  };

  @OnClick(R.id.takePhoto)
  void takePicture() {
    final View view = this;
    if (PermissionService.get(view).isPhotoGranted()) {
      PhotoService.get(view).takePhoto(receiveNewBitmap);
    } else {
      requestTakePhotoPermission();
    }
  }

  @OnClick(R.id.pickGallery)
  void choosePhoto() {
    final View view = this;
    if (PermissionService.get(view).isPhotoGranted()) {
      PhotoService.get(view).pickImage(receiveNewBitmap);
    } else {
      requestGalleryPermission();
    }
  }

  private void requestTakePhotoPermission() {
    PermissionService.get(this).requestGalleryPermission(new Action0() {
      @Override
      public void call() {
        takePicture();
      }
    }, new Action0() {
      @Override
      public void call() {
        showTakePhotoSnackbar();
      }
    });
  }

  private void showTakePhotoSnackbar() {
    // Display a SnackBar with an explanation and a button
    // to trigger the request.
    Snackbar.make(this, R.string.permission_photo_rationale, Snackbar.LENGTH_INDEFINITE)
        .setAction(android.R.string.ok, new View.OnClickListener() {
          @Override
          public void onClick(View view) {
            PermissionService.get(PhotoEditView.this).requestGalleryPermission(new Action0() {
              @Override
              public void call() {
                takePicture();
              }
            }, null);
          }
        })
        .show();
  }

  private void requestGalleryPermission() {
    PermissionService.get(this).requestGalleryPermission(new Action0() {
      @Override
      public void call() {
        choosePhoto();
      }
    }, new Action0() {
      @Override
      public void call() {
        showGalleryPhotoSnackbar();
      }
    });
  }

  private void showGalleryPhotoSnackbar() {
    // Display a SnackBar with an explanation and a button
    // to trigger the request.
    Snackbar.make(this, R.string.permission_photo_rationale, Snackbar.LENGTH_INDEFINITE)
        .setAction(android.R.string.ok, new View.OnClickListener() {
          @Override
          public void onClick(View view) {
            PermissionService.get(PhotoEditView.this).requestGalleryPermission(new Action0() {
              @Override
              public void call() {
                choosePhoto();
              }
            }, null);
          }
        })
        .show();
  }
}
