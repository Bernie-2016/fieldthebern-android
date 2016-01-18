package com.berniesanders.fieldthebern.screens;

import android.os.Bundle;
import android.os.Parcelable;

import com.berniesanders.fieldthebern.FTBApplication;
import com.berniesanders.fieldthebern.R;
import com.berniesanders.fieldthebern.annotations.Layout;
import com.berniesanders.fieldthebern.controllers.ActionBarService;
import com.berniesanders.fieldthebern.controllers.DialogController;
import com.berniesanders.fieldthebern.controllers.DialogService;
import com.berniesanders.fieldthebern.controllers.ToastService;
import com.berniesanders.fieldthebern.dagger.FtbScreenScope;
import com.berniesanders.fieldthebern.dagger.MainComponent;
import com.berniesanders.fieldthebern.exceptions.AuthFailRedirect;
import com.berniesanders.fieldthebern.exceptions.NetworkUnavailableException;
import com.berniesanders.fieldthebern.models.ApiAddress;
import com.berniesanders.fieldthebern.models.MultiAddressResponse;
import com.berniesanders.fieldthebern.models.RequestMultipleAddresses;
import com.berniesanders.fieldthebern.models.RequestSingleAddress;
import com.berniesanders.fieldthebern.models.SingleAddressResponse;
import com.berniesanders.fieldthebern.mortar.FlowPathBase;
import com.berniesanders.fieldthebern.parsing.ErrorResponseParser;
import com.berniesanders.fieldthebern.repositories.AddressRepo;
import com.berniesanders.fieldthebern.repositories.VisitRepo;
import com.berniesanders.fieldthebern.repositories.specs.AddressSpec;
import com.berniesanders.fieldthebern.views.MapScreenView;
import com.google.android.gms.maps.model.CameraPosition;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindString;
import butterknife.ButterKnife;
import butterknife.OnClick;
import flow.Flow;
import flow.History;
import flow.path.Path;
import mortar.MortarScope;
import mortar.ViewPresenter;
import rx.Observer;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action0;
import rx.schedulers.Schedulers;
import timber.log.Timber;

/**
 *
 */
@Layout(R.layout.screen_map)
public class MapScreen extends FlowPathBase {

    public CameraPosition cameraPosition;
    public ApiAddress address;
    public List<ApiAddress> nearby;

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
        ErrorResponseParser errorResponseParser();
    }

    @FtbScreenScope
    static public class Presenter extends ViewPresenter<MapScreenView> {

        public static final String CAMERA_POSITION = "camera_position";
        public static final String ADDRESS = "address";
        public static final String NEARBY = "nearby";
        private final AddressRepo addressRepo;
        private final VisitRepo visitRepo;
        private final ErrorResponseParser errorResponseParser;
        Subscription singleAddressSubscription;
        Subscription multiAddressSubscription;

        List<ApiAddress> nearbyAddresses = new ArrayList<>();
        private CameraPosition cameraPosition;
        private ApiAddress address;
        boolean showInProgressDialog = false;

        @BindString(R.string.visit_in_progress_message) String inProgressMessage;
        @BindString(R.string.visit_in_progress_title) String inProgressTitle;

        @Inject
        Presenter(AddressRepo addressRepo, ErrorResponseParser errorResponseParser, VisitRepo visitRepo) {
            this.addressRepo = addressRepo;
            this.errorResponseParser = errorResponseParser;
            this.visitRepo = visitRepo;
        }

        @Override
        protected void onLoad(Bundle savedInstanceState) {
            Timber.v("onLoad");
            ButterKnife.bind(this, getView());
            setActionBar();

            if (visitRepo.inProgress() || showInProgressDialog) {
                promptToContinue();
            }

            if (savedInstanceState != null) {
                //restores state after rotation
                cameraPosition = savedInstanceState.getParcelable(CAMERA_POSITION);
                address = savedInstanceState.getParcelable(ADDRESS);
                nearbyAddresses = savedInstanceState.getParcelableArrayList(NEARBY);
            }

            if (cameraPosition == null) {
                //unfortunate hack for restoring state in flow after navigation
                cameraPosition = ((MapScreen) Path.get(getView().getContext())).cameraPosition;
            }

            if (address == null) {
                //unfortunate hack for restoring state in flow after navigation
                address = ((MapScreen) Path.get(getView().getContext())).address;
            }
            if (nearbyAddresses != null && !nearbyAddresses.isEmpty()) {
                //unfortunate hack for restoring state in flow after navigation
                nearbyAddresses = ((MapScreen) Path.get(getView().getContext())).nearby;
            }

            if (address != null) {
                getView().setAddress(address);
            }

            if (cameraPosition != null) {
                getView().setCameraPosition(cameraPosition);
            }

            getView().setOnAddressChangeListener(onAddressChange);
            getView().setOnCameraChangeListener(onCameraChange);
            getView().setOnMarkerClick(onMarkerClick);
            getView().setNearbyAddresses(nearbyAddresses);
        }

        private void promptToContinue() {

            showInProgressDialog = true;

            final ApiAddress inProgressAddress = (ApiAddress) visitRepo.get().included().get(0);

            final String formattedMessage = String.format(
                    inProgressMessage, inProgressAddress.attributes().street1());

            DialogController.DialogAction confirmAction = new DialogController.DialogAction()
                    .label(R.string.yes)
                    .action(new Action0() {
                        @Override
                        public void call() {
                            Timber.d("yes button click");
                            showInProgressDialog = false;
                            Flow.get(getView()).set(
                                    new NewVisitScreen(inProgressAddress));
                        }
                    });

            DialogController.DialogAction cancelAction = new DialogController.DialogAction()
                    .label(R.string.no)
                    .action(new Action0() {
                        @Override
                        public void call() {
                            showInProgressDialog = false;
                            visitRepo.clear();
                            Iterator<Object> iterator = Flow.get(getView()).getHistory().iterator();
                            Flow.get(getView()).setHistory(History.single(iterator.next()), Flow.Direction.REPLACE);
                        }
                    });

            DialogService
                    .get(getView())
                    .setDialogConfig(
                            new DialogController.DialogConfig()
                                    .title(inProgressTitle)
                                    .message(formattedMessage)
                                    .withActions(confirmAction, cancelAction)
                    );
        }

        MapScreenView.OnCameraChange onCameraChange = new MapScreenView.OnCameraChange() {
            @Override
            public void onCameraChange(CameraPosition cameraPosition, boolean shouldRefreshAddresses, int radius ) {
                Presenter.this.cameraPosition = cameraPosition;

                if (!shouldRefreshAddresses) { return; }

                if (multiAddressSubscription != null && !multiAddressSubscription.isUnsubscribed()) {
                    multiAddressSubscription.unsubscribe();
                }

                multiAddressSubscription = addressRepo
                        .getMultiple(
                                new AddressSpec().multipleAddresses(new RequestMultipleAddresses()
                                        .latitude(cameraPosition.target.latitude)
                                        .longitude(cameraPosition.target.longitude)
                                        .radius(radius)))
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(multiAddressObserver);
            }
        };

        MapScreenView.OnAddressChange onAddressChange = new MapScreenView.OnAddressChange() {
            @Override
            public void onAddressChange(ApiAddress address) {
                Presenter.this.address = address;
                
                if (singleAddressSubscription != null && !singleAddressSubscription.isUnsubscribed()) {
                    singleAddressSubscription.unsubscribe();
                }

                singleAddressSubscription = addressRepo
                        .getSingle(new AddressSpec().singleAddress(new RequestSingleAddress(address)))
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(singleAddressObserver);
            }
        };

        MapScreenView.OnMarkerClick onMarkerClick = new MapScreenView.OnMarkerClick() {
            @Override
            public void onMarkerClick() {
                if (singleAddressSubscription != null && !singleAddressSubscription.isUnsubscribed()) {
                    singleAddressSubscription.unsubscribe();
                }
                if (multiAddressSubscription != null && !multiAddressSubscription.isUnsubscribed()) {
                    multiAddressSubscription.unsubscribe();
                }
            }
        };

        Observer<MultiAddressResponse> multiAddressObserver = new Observer<MultiAddressResponse>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                Timber.e(e, "multiAddressObserver onError");
                if (AuthFailRedirect.redirectOnFailure(e, getView())) {
                    return;
                }

                if (e instanceof NetworkUnavailableException) {
                    ToastService
                            .get(getView())
                            .bern(getView().getResources().getString(R.string.err_internet_not_available));
                }
            }

            @Override
            public void onNext(MultiAddressResponse multiAddressResponse) {
                Timber.v("multiAddressObserver onNext \n%s", multiAddressResponse );

                nearbyAddresses = multiAddressResponse.addresses();

                getView().setNearbyAddresses(nearbyAddresses);
            }
        };


        Observer<SingleAddressResponse> singleAddressObserver = new Observer<SingleAddressResponse>() {
            @Override
            public void onCompleted() {
            }

            @Override
            public void onError(Throwable e) {
                if (getView() == null) {
                    Timber.e(e, "singleAddressObserver onError");
                    return;
                }
                if (AuthFailRedirect.redirectOnFailure(e, getView())) {
                    return;
                }
                if (e instanceof NetworkUnavailableException) {
                    ToastService
                            .get(getView())
                            .bern(getView().getResources().getString(R.string.err_internet_not_available));
                }
            }

            @Override
            public void onNext(SingleAddressResponse response) {
                Timber.v("singleAddressObserver onNext  response.addresses().get(0) =\n%s", response.addresses().get(0) );
                address = response.addresses().get(0);
            }
        };

        void setActionBar() {

            ActionBarService
                    .get(getView())
                    .hideToolbar()
                    .closeAppbar()
                    .setMainImage(null);
        }

        @Override
        protected void onSave(Bundle outState) {

            if (cameraPosition != null) {
                outState.putParcelable(CAMERA_POSITION, cameraPosition);
            }

            if (address != null) {
                outState.putParcelable(ADDRESS, address);
            }

            if (nearbyAddresses != null && !nearbyAddresses.isEmpty()) {
                outState.putParcelableArrayList(NEARBY, (ArrayList<? extends Parcelable>) nearbyAddresses);
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
            mapScreenView.setOnMarkerClick(null);
        }

        @OnClick(R.id.address_btn)
        void onAddAddressClick() {
            address = getView().getCurrentAddress();


            if (address==null) {
                ToastService
                        .get(getView())
                        .bern(getView().getResources().getString(R.string.err_address_not_loaded));
                return;
            }

            //unfortunate hack for saving state in flow after navigation
            ((MapScreen) Path.get(getView().getContext())).cameraPosition = cameraPosition;
            ((MapScreen) Path.get(getView().getContext())).address = address;
            ((MapScreen) Path.get(getView().getContext())).nearby = nearbyAddresses;
            Flow.get(getView()).set(new AddAddressScreen(address));
            dropListeners(getView());
        }
    }
}
