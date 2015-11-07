package com.berniesanders.canvass.screens;

import android.os.Bundle;
import android.view.View;

import com.berniesanders.canvass.R;
import com.berniesanders.canvass.annotations.Layout;
import com.berniesanders.canvass.dagger.FtbScreenScope;
import com.berniesanders.canvass.models.Img;
import com.berniesanders.canvass.mortar.ActionBarService;
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
@Layout(R.layout.screen_photo_view)
public class PhotoScreen extends FlowPathBase {

    private final Img img;

    public PhotoScreen(Img img) {
        this.img = img;
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

            Picasso.with(getView().getContext())
                    .load(img.getText())
                    .into(getView().getImageView());

            //getView().getSourceTextView().setText(img.getCaption() +"\n"+img.getSource());
            getView().getSourceTextView().setVisibility(View.GONE);

            setActionBar();
        }
        void setActionBar() {

            ActionBarService
                    .getActionbarController(getView())
                    .hideToolbar()
                    .closeAppbar();
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
