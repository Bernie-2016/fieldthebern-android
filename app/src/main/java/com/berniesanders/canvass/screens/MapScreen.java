package com.berniesanders.canvass.screens;

import android.os.Bundle;

import com.berniesanders.canvass.FTBApplication;
import com.berniesanders.canvass.R;
import com.berniesanders.canvass.annotations.Layout;
import com.berniesanders.canvass.dagger.FtbScreenScope;
import com.berniesanders.canvass.dagger.MainComponent;
import com.berniesanders.canvass.mortar.ActionBarService;
import com.berniesanders.canvass.mortar.FlowPathBase;
import com.berniesanders.canvass.views.MapScreenView;

import javax.inject.Inject;

import butterknife.ButterKnife;
import mortar.MortarScope;
import mortar.ViewPresenter;
import timber.log.Timber;

/**
 *
 */
@Layout(R.layout.screen_map)
public class MapScreen extends FlowPathBase {


    public MapScreen() {
    }

    @Override
    public Object createComponent() {
        return DaggerMapScreen_Component
                .builder()
                .mainComponent(FTBApplication.getComponent())
                .build();
    }

    @Override
    public String getScopeName() {
        return MapScreen.class.getName();// TODO temp scope name?
    }


//    @Module
//    class Module {
//    }

    @FtbScreenScope
    @dagger.Component(dependencies = MainComponent.class)
    public interface Component {
        void inject(MapScreenView t);
    }

    @FtbScreenScope
    static public class Presenter extends ViewPresenter<MapScreenView> {


        @Inject
        Presenter() {
        }

        @Override
        protected void onLoad(Bundle savedInstanceState) {
            Timber.v("onLoad");
            ButterKnife.bind(this, getView());
            setActionBar();
        }

        void setActionBar() {

            ActionBarService
                    .getActionbarController(getView())
                    .hideToolbar()
                    .closeAppbar()
                    .setMainImage(null);
        }

        @Override
        protected void onSave(Bundle outState) {
//            outState.putParcelable(Img.IMG_PARCEL_KEY, img);
        }

        @Override
        public void dropView(MapScreenView view) {
            super.dropView(view);
        }

        @Override
        protected void onEnterScope(MortarScope scope) {
            super.onEnterScope(scope);
            Timber.v("onEnterScope: %s", scope);
        }
    }
}
