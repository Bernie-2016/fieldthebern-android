package com.berniesanders.canvass.screens;

import android.location.Address;
import android.os.Bundle;

import com.berniesanders.canvass.FTBApplication;
import com.berniesanders.canvass.R;
import com.berniesanders.canvass.annotations.Layout;
import com.berniesanders.canvass.controllers.ActionBarService;
import com.berniesanders.canvass.dagger.FtbScreenScope;
import com.berniesanders.canvass.dagger.MainComponent;
import com.berniesanders.canvass.mortar.FlowPathBase;
import com.berniesanders.canvass.views.MapScreenView;
import com.google.android.gms.maps.model.CameraPosition;

import javax.inject.Inject;

import butterknife.ButterKnife;
import butterknife.OnClick;
import flow.Flow;
import flow.path.Path;
import mortar.MortarScope;
import mortar.ViewPresenter;
import timber.log.Timber;

/**
 *
 */
@Layout(R.layout.screen_map)
public class MapScreen extends FlowPathBase {

    public CameraPosition cameraPosition;
    public Address address;

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

        public static final String CAMERA_POSITION = "camera_position";
        public static final String ADDRESS = "address";

        private CameraPosition cameraPosition;
        private Address address;


        @Inject
        Presenter() {
        }

        @Override
        protected void onLoad(Bundle savedInstanceState) {
            Timber.v("onLoad");
            ButterKnife.bind(this, getView());
            setActionBar();

            if (savedInstanceState != null) {
                //restores state after rotation
                cameraPosition = savedInstanceState.getParcelable(CAMERA_POSITION);
                address = savedInstanceState.getParcelable(ADDRESS);
            }

            if (cameraPosition == null) {
                //unfortunate hack for restoring state in flow after navigation
                cameraPosition = ((MapScreen) Path.get(getView().getContext())).cameraPosition;
            }

            if (address == null) {
                //unfortunate hack for restoring state in flow after navigation
                address = ((MapScreen) Path.get(getView().getContext())).address;
            }

            if (address != null) {
                getView().setAddress(address);
            }

            if (cameraPosition != null) {
                getView().setCameraPosition(cameraPosition);
            }

            getView().setOnAddressChangeListener(onAddressChange);
            getView().setOnCameraChangeListener(onCameraChange);
        }

        MapScreenView.OnCameraChange onCameraChange = new MapScreenView.OnCameraChange() {
            @Override
            public void onCameraChange(CameraPosition cameraPosition) {
                Presenter.this.cameraPosition = cameraPosition;
            }
        };

        MapScreenView.OnAddressChange onAddressChange = new MapScreenView.OnAddressChange() {
            @Override
            public void onAddressChange(Address address) {
                Presenter.this.address = address;
            }
        };

        void setActionBar() {

            ActionBarService
                    .getActionbarController(getView())
                    .hideToolbar()
                    .closeAppbar()
                    .setMainImage(null);
        }

        @Override
        protected void onSave(Bundle outState) {

            if (cameraPosition!=null) {
                outState.putParcelable(CAMERA_POSITION, cameraPosition);
            }

            if (address!=null) {
                outState.putParcelable(ADDRESS, address);
            }
        }

        @Override
        public void dropView(MapScreenView view) {
            super.dropView(view);
            dropListeners(view);
        }

        @Override
        protected void onEnterScope(MortarScope scope) {
            super.onEnterScope(scope);
            Timber.v("onEnterScope: %s", scope);
        }

        private void dropListeners(MapScreenView mapScreenView) {
            mapScreenView.setOnAddressChangeListener(null);
            mapScreenView.setOnCameraChangeListener(null);
        }

        @OnClick(R.id.address_btn)
        void onAddAddressClick() {
            //unfortunate hack for saving state in flow after navigation
            ((MapScreen) Path.get(getView().getContext())).cameraPosition = cameraPosition;
            ((MapScreen) Path.get(getView().getContext())).address = address;
            Flow.get(getView()).set(new AddAddressScreen(address));
            dropListeners(getView());
        }
    }
}
