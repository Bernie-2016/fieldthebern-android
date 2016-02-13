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
import android.view.View;
import com.berniesanders.fieldthebern.R;
import com.berniesanders.fieldthebern.annotations.Layout;
import com.berniesanders.fieldthebern.controllers.ActionBarService;
import com.berniesanders.fieldthebern.dagger.FtbScreenScope;
import com.berniesanders.fieldthebern.models.Img;
import com.berniesanders.fieldthebern.mortar.ParcelableScreen;
import com.berniesanders.fieldthebern.views.PhotoScreenView;
import com.squareup.picasso.Picasso;
import dagger.Module;
import dagger.Provides;
import javax.inject.Inject;
import mortar.MortarScope;
import mortar.ViewPresenter;
import timber.log.Timber;

/**
 *
 */
@Layout(R.layout.screen_photo_view)
public class PhotoScreen extends ParcelableScreen {

  private final Img img;

  public PhotoScreen(Img img) {
    this.img = img;
  }

  @Override
  public Object createComponent() {
    return DaggerPhotoScreen_Component.builder().imgModule(new ImgModule(img)).build();
  }

  @Override
  public String getScopeName() {
    return PhotoScreen.class.getName();// TODO temp scope name?
  }

  @Override protected void doWriteToParcel(Parcel parcel, int flags) {
    parcel.writeParcelable(img, 0);
  }

  public static final Parcelable.Creator<PhotoScreen>
      CREATOR = new ParcelableScreen.ScreenCreator<PhotoScreen>() {
    @Override protected PhotoScreen doCreateFromParcel(Parcel source) {
      Img img = source.readParcelable(Img.class.getClassLoader());
      return new PhotoScreen(img);
    }

    @Override public PhotoScreen[] newArray(int size) {
      return new PhotoScreen[size];
    }
  };

  @Module
  class ImgModule {
    private final Img imgage;

    public ImgModule(Img imgage) {
      this.imgage = imgage;
    }

    @Provides
    public Img provideImg() {
      return imgage;
    }
  }

  @FtbScreenScope
  @dagger.Component(modules = ImgModule.class)
  public interface Component {
    void inject(PhotoScreenView t);

    Img getImg();
  }

  @FtbScreenScope
  static public class Presenter extends ViewPresenter<PhotoScreenView> {

    private final Img img;

    @Inject
    Presenter(Img img) {
      this.img = img;
    }

    @Override
    protected void onLoad(Bundle savedInstanceState) {
      Timber.v("onLoad");

      Picasso.with(getView().getContext()).load(img.getText()).into(getView().getImageView());

      //getView().getSourceTextView().setText(img.getCaption() +"\n"+img.getSource());
      getView().getSourceTextView().setVisibility(View.GONE);

      setActionBar();
    }

    void setActionBar() {

      ActionBarService.get(getView()).hideToolbar().closeAppbar();
    }

    @Override
    protected void onSave(Bundle outState) {
      outState.putParcelable(Img.IMG_PARCEL_KEY, img);
    }

    @Override
    public void dropView(PhotoScreenView view) {
      super.dropView(view);
    }

    @Override
    protected void onEnterScope(MortarScope scope) {
      super.onEnterScope(scope);
      Timber.v("onEnterScope: %s", scope);
    }
  }
}
