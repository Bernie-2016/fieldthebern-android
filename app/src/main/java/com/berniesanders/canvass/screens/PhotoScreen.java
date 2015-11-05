package com.berniesanders.canvass.screens;

import android.os.Bundle;

import com.berniesanders.canvass.FTBApplication;
import com.berniesanders.canvass.annotations.Layout;
import com.berniesanders.canvass.dagger.FtbScreenScope;
import com.berniesanders.canvass.events.ChangePageEvent;
import com.berniesanders.canvass.models.Img;
import com.berniesanders.canvass.mortar.FlowPathBase;
import com.berniesanders.canvass.views.PhotoScreenView;
import com.squareup.picasso.Picasso;

import javax.inject.Inject;

import dagger.Module;
import dagger.Provides;
import mortar.MortarScope;
import mortar.ViewPresenter;
import timber.log.Timber;

/**
 *
 */
@Layout(com.berniesanders.canvass.R.layout.screen_photo_view)
public class PhotoScreen extends FlowPathBase {

    private final Img img;

    public PhotoScreen(Img img) {
        this.img = img;
    }

    @Override
    public int getLayout() {
        return com.berniesanders.canvass.R.layout.screen_photo_view;
    }

    @Override
    public Object createComponent() {
        return DaggerPhotoScreen_Component
                .builder()
                .imgModule(new ImgModule(img))
                .build();
    }

    @Override
    public String getScopeName() {
        return PhotoScreen.class.getName();// TODO temp scope name?
    }


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
            new ChangePageEvent()
                    .with(FTBApplication.getEventBus())
                    .close(true)
                    .hideToolbar(true)
                    .dispatch();

            Picasso.with(getView().getContext())
                    .load(img.getText())
                    .into(getView().getImageView());


            // Set the Drawable displayed
            //Drawable bitmap = getResources().getDrawable(R.drawable.wallpaper);
            //mImageView.setImageDrawable(bitmap);

            // Attach a PhotoViewAttacher, which takes care of all of the zooming functionality.

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
