package com.berniesanders.fieldthebern.screens;

import android.location.Address;
import android.os.Bundle;

import com.berniesanders.fieldthebern.FTBApplication;
import com.berniesanders.fieldthebern.R;
import com.berniesanders.fieldthebern.annotations.Layout;
import com.berniesanders.fieldthebern.controllers.ActionBarService;
import com.berniesanders.fieldthebern.controllers.ToastService;
import com.berniesanders.fieldthebern.dagger.FtbScreenScope;
import com.berniesanders.fieldthebern.dagger.MainComponent;
import com.berniesanders.fieldthebern.location.StateConverter;
import com.berniesanders.fieldthebern.models.ApiAddress;
import com.berniesanders.fieldthebern.models.MultiAddressResponse;
import com.berniesanders.fieldthebern.models.RequestMultipleAddresses;
import com.berniesanders.fieldthebern.models.RequestSingleAddress;
import com.berniesanders.fieldthebern.mortar.FlowPathBase;
import com.berniesanders.fieldthebern.repositories.AddressRepo;
import com.berniesanders.fieldthebern.repositories.specs.AddressSpec;
import com.berniesanders.fieldthebern.views.MapScreenView;
import com.google.android.gms.maps.model.CameraPosition;

import java.util.List;

import javax.inject.Inject;

import butterknife.ButterKnife;
import butterknife.OnClick;
import flow.Flow;
import flow.path.Path;
import mortar.MortarScope;
import mortar.ViewPresenter;
import rx.Observer;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
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
        AddressRepo addressRepo();
    }

    @FtbScreenScope
    static public class Presenter extends ViewPresenter<MapScreenView> {

        public static final String CAMERA_POSITION = "camera_position";
        public static final String ADDRESS = "address";
        private final AddressRepo addressRepo;

        private CameraPosition cameraPosition;
        private Address address;


        @Inject
        Presenter(AddressRepo addressRepo) {
            this.addressRepo = addressRepo;
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

                Subscription multiAddressSubscription = addressRepo.getMultiple(
                        new AddressSpec()
                            .multipleAddresses(
                                    new RequestMultipleAddresses()
                                            .latitude(cameraPosition.target.latitude)
                                            .longitude(cameraPosition.target.longitude)
                                            .radius(1000)))
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(multiAddressObserver);
            }
        };

        MapScreenView.OnAddressChange onAddressChange = new MapScreenView.OnAddressChange() {
            @Override
            public void onAddressChange(Address address) {
                Presenter.this.address = address;

                Subscription singleAddressSubscription = addressRepo.getSingle(
                        new AddressSpec()
                                .singleAddress(
                                        new RequestSingleAddress()
                                                //.latitude(cameraPosition.target.latitude)
                                                //.longitude(cameraPosition.target.longitude)
                                                .street1(address.getAddressLine(0))
                                                //.street2("Apt #1")
                                                .city(address.getLocality())
                                                .state(StateConverter.getStateCode(address.getAdminArea()))
                                                .zip(address.getPostalCode())))
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(singleAddressObserver);
            }
        };

        Observer<MultiAddressResponse> multiAddressObserver = new Observer<MultiAddressResponse>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                Timber.e(e, "multiAddressObserver onError");
            }

            @Override
            public void onNext(MultiAddressResponse multiAddressResponse) {
                Timber.v("multiAddressObserver onNext \n%s", multiAddressResponse );

                List<ApiAddress> nearbyAddresses = multiAddressResponse.addresses();

                getView().setNearbyAddresses(nearbyAddresses);
            }
        };


        Observer<ApiAddress> singleAddressObserver = new Observer<ApiAddress>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                Timber.w("singleAddressObserver onError: %s", e.getMessage());
            }

            @Override
            public void onNext(ApiAddress apiAddresses) {
                Timber.v("singleAddressObserver onNext \n%s", apiAddresses );
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
            ButterKnife.unbind(this);
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
            if (address==null) {
                ToastService
                        .get(getView())
                        .toast(getView().getResources().getString(R.string.err_address_not_loaded));
                return;
            }

            //unfortunate hack for saving state in flow after navigation
            ((MapScreen) Path.get(getView().getContext())).cameraPosition = cameraPosition;
            ((MapScreen) Path.get(getView().getContext())).address = address;
            Flow.get(getView()).set(new AddAddressScreen(address));
            dropListeners(getView());
        }
    }
}
