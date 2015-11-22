package com.berniesanders.canvass.views;

import android.app.Activity;
import android.content.Context;
import android.location.Address;
import android.location.Location;
import android.support.design.widget.FloatingActionButton;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.berniesanders.canvass.R;
import com.berniesanders.canvass.controllers.ActionBarService;
import com.berniesanders.canvass.controllers.LocationService;
import com.berniesanders.canvass.mortar.DaggerService;
import com.berniesanders.canvass.mortar.HandlesBack;
import com.berniesanders.canvass.screens.AddAddressScreen;
import com.berniesanders.canvass.screens.MapScreen;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;

import org.apache.commons.lang3.reflect.FieldUtils;

import java.lang.ref.WeakReference;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import flow.Flow;
import flow.path.Path;
import flow.path.PathContext;
import rx.Observable;
import rx.Observer;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import timber.log.Timber;

/**
 *
 */
public class MapScreenView extends FrameLayout implements HandlesBack {

    MapFragment mapFragment;
    GoogleMap googleMap;
    WeakReference<Activity> activityWeakReference;
    Subscription cameraSubscription;
    Subscription locationSubscription;
    Subscription geocodeSubscription;

    Observer<Location> locationObserver;

    Address address;

    @Bind(R.id.address_btn)
    FloatingActionButton fab;

    @Bind(R.id.address)
    TextView addressTextView;

    @Bind(R.id.pin_drop)
    ImageView pinDrop;

    @Bind(R.id.progressBar)
    ProgressBar progressBar;

    @Inject
    MapScreen.Presenter presenter;

    public MapScreenView(Context context) {
        super(context);
        injectSelf(context);
    }

    public MapScreenView(Context context, AttributeSet attrs) {
        super(context, attrs);
        injectSelf(context);
    }

    public MapScreenView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        injectSelf(context);
    }


    private void injectSelf(Context context) {
        //This is a hack to safely get a reference to the activity.
        //Flow is internally already passing around the references in a private Map<Path,Context>
        //So we use a little hacky reflection tool to steal the activity ref
        //but hold it in a weak ref
        PathContext pathContext = (PathContext) context;
        Map<Path, Context> contextMap = new HashMap<>();
        try {
            contextMap = (Map<Path, Context>) FieldUtils.readDeclaredField(context, "contexts", true);
        } catch (IllegalAccessException e) {
            Timber.e(e, "error reading path contexts...");
            e.printStackTrace();
        }

        for (Context ctx : contextMap.values()) {
            if (ctx instanceof Activity) {
                Timber.v("We found an activity!");
                activityWeakReference = new WeakReference<Activity>((Activity) ctx);
            }
        }



        DaggerService.<MapScreen.Component>
                getDaggerComponent(context, DaggerService.DAGGER_SERVICE)
                .inject(this);
    }


    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        Timber.v("onFinishInflate");

        if (activityWeakReference != null) {
            mapFragment = (MapFragment) activityWeakReference
                    .get()
                    .getFragmentManager()
                    .findFragmentById(R.id.map_frag);

            mapFragment.getMapAsync(new OnMapReadyCallback() {

                @Override
                public void onMapReady(GoogleMap gmap) {
                    Timber.v("OnMapReadyCallback");
                    MapScreenView.this.googleMap = gmap;
                    gmap.setMyLocationEnabled(true);
                    setCameraPosition(gmap);
                }
            });
        }

        ButterKnife.bind(this, this);
    }

    @OnClick(R.id.address_btn)
    public void addNewAddress() {
        Timber.v("addNewAddress click");
        Flow.get(getContext()).set(new AddAddressScreen());
    }

    @Override
    protected void onAttachedToWindow() {
        Timber.v("onAttachToWindow");
        super.onAttachedToWindow();
        presenter.takeView(this);
    }

    @Override
    protected void onDetachedFromWindow() {
        presenter.dropView(this);
        super.onDetachedFromWindow();
        if (activityWeakReference != null) {

            MapFragment f = (MapFragment) activityWeakReference
                    .get()
                    .getFragmentManager()
                    .findFragmentById(R.id.map_frag);

            if (f != null) {
                activityWeakReference
                        .get()
                        .getFragmentManager()
                        .beginTransaction()
                        .remove(f)
                        .commit();

                ///???
//                activityWeakReference
//                        .get()
//                        .getFragmentManager().executePendingTransactions();
            }

            activityWeakReference.clear();
        }

        unsubscribe();
    }

    private void unsubscribe() {

        if (cameraSubscription != null) {
            cameraSubscription.unsubscribe();
        }
        if (locationSubscription != null) {
            locationSubscription.unsubscribe();
        }
        if (geocodeSubscription != null) {
            geocodeSubscription.unsubscribe();
        }
    }

    private void setCameraPosition(final GoogleMap map) {

        locationObserver = new Observer<Location>() {
            @Override
            public void onCompleted() {
                Timber.v("setCameraPosition onCompleted");
            }

            @Override
            public void onError(Throwable e) {
                Timber.e(e, "setCameraPosition onError");
            }

            @Override
            public void onNext(Location location) {
                map.moveCamera(CameraUpdateFactory
                        .newCameraPosition(getCameraPosition(location)));

                cameraSubscription = watchCamera(map)
                        .subscribeOn(AndroidSchedulers.mainThread())
                        .observeOn(AndroidSchedulers.mainThread())
                        .debounce(1, TimeUnit.SECONDS)
                        .subscribe(cameraObserver);
            }
        };

        locationSubscription = LocationService.get(this)
                .get()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(locationObserver);
    }


    Observer<CameraPosition> cameraObserver = new Observer<CameraPosition>() {
        @Override
        public void onCompleted() {

        }

        @Override
        public void onError(Throwable e) {
            Timber.e(e, "onError");
        }

        @Override
        public void onNext(CameraPosition cameraPosition) {
            Timber.v("onNext");
            LatLng latLng = cameraPosition.target;

            geocodeSubscription = LocationService.get(MapScreenView.this)
                    .reverseGeocode(latLng)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(geocodeObserver);
        }
    };

    Observer<Address> geocodeObserver = new Observer<Address>() {

        @Override
        public void onCompleted() {
            Timber.v("geocodeObserver onCompleted");
        }

        @Override
        public void onError(Throwable e) {
            Timber.e(e, "geocodeObserver onError");
        }

        @Override
        public void onNext(Address address) {
            Timber.v("geocodeObserver onNext");
            MapScreenView.this.address = address;
            addressTextView.setText(address.getAddressLine(0));
            progressBar.setVisibility(View.GONE);
            pinDrop.setVisibility(View.VISIBLE);
        }
    };

    private CameraPosition getCameraPosition(Location location) {
        return new CameraPosition
                .Builder()
                .target(new LatLng(location.getLatitude(), location.getLongitude()))
                .zoom(18f)
                .build();
    }



    private Observable<CameraPosition> watchCamera(final GoogleMap map) {

        return Observable.create(new Observable.OnSubscribe<CameraPosition>() {

            @Override
            public void call(final Subscriber<? super CameraPosition> subscriber) {

                GoogleMap.OnCameraChangeListener camChangeListener =
                        new GoogleMap.OnCameraChangeListener() {

                            @Override
                            public void onCameraChange(CameraPosition camPosition) {
                                Timber.v("onCameraChange");
                                address = null;
                                addressTextView.setText("");
                                progressBar.setVisibility(View.VISIBLE);
                                pinDrop.setVisibility(View.GONE);
                                subscriber.onNext(camPosition);
                            }
                        };

                map.setOnCameraChangeListener(camChangeListener);
            }
        });
    }

    @Override
    public boolean onBackPressed() {

        ActionBarService
                .getActionbarController(this)
                .showToolbar();
        return false;
    }
}
