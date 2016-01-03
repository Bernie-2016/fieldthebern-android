package com.berniesanders.fieldthebern.views;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.location.Address;
import android.location.Location;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.berniesanders.fieldthebern.R;
import com.berniesanders.fieldthebern.controllers.ActionBarService;
import com.berniesanders.fieldthebern.controllers.LocationService;
import com.berniesanders.fieldthebern.media.ResponseColor;
import com.berniesanders.fieldthebern.models.ApiAddress;
import com.berniesanders.fieldthebern.mortar.DaggerService;
import com.berniesanders.fieldthebern.mortar.HandlesBack;
import com.berniesanders.fieldthebern.parsing.CanvassResponseEvaluator;
import com.berniesanders.fieldthebern.screens.MapScreen;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import org.apache.commons.lang3.reflect.FieldUtils;

import java.lang.ref.WeakReference;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.ButterKnife;
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
    Handler handler;
    Observer<Location> locationObserver;
    ApiAddress address;

    @Bind(R.id.address)
    TextView addressTextView;

    @Bind(R.id.leaning)
    TextView leaningTextView;

    @Bind(R.id.pin_drop)
    ImageView pinDrop;

    @Bind(R.id.progressBar)
    ProgressBar progressBar;

    @Inject
    MapScreen.Presenter presenter;
    private OnCameraChange onCameraChangeListener;
    private OnAddressChange onAddressChangeListener;
    private CameraPosition cameraPosition;
    private Map<String, ApiAddress> markerAddressMap = new HashMap<>();
    private List<ApiAddress> nearbyAddresses;

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
        //TODO: we could probably change this to a "controller"
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
                    gmap.getUiSettings().setMapToolbarEnabled(false);
                    initCameraPosition(gmap);
                }
            });
        }

        ButterKnife.bind(this, this);
        handler = new Handler();
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
        googleMap = null;
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

        handler.removeCallbacksAndMessages(null);
        googleMap.setOnCameraChangeListener(null);
    }

    private void initCameraPosition(final GoogleMap map) {

        Timber.v("initCameraPosition");
        if (nearbyAddresses!=null) {
            setNearbyAddresses(nearbyAddresses);
        }

        if (cameraPosition != null) {
            //if we're already there, bail early
            if (map.getCameraPosition().equals(cameraPosition)) { return; }

            map.moveCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));

            if (address == null) {
                connectCameraObservable(map);
            } else {
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        connectCameraObservable(map);
                    }
                }, 1000);
            }
            return;
        }

        locationObserver = new Observer<Location>() {
            @Override
            public void onCompleted() {
                Timber.v("initCameraPosition onCompleted");
            }

            @Override
            public void onError(Throwable e) {
                Timber.e(e, "initCameraPosition onError");
            }

            @Override
            public void onNext(Location location) {
                map.moveCamera(CameraUpdateFactory
                        .newCameraPosition(getCameraPosition(location)));
                connectCameraObservable(map);
            }
        };

        locationSubscription = LocationService.get(this)
                .get()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(locationObserver);
    }

    private void connectCameraObservable(final GoogleMap map) {

        if (cameraSubscription == null || cameraSubscription.isUnsubscribed()) {
            cameraSubscription = watchCamera(map)
                    .subscribeOn(AndroidSchedulers.mainThread())
                    .observeOn(AndroidSchedulers.mainThread())
                    .debounce(1, TimeUnit.SECONDS)
                    .subscribe(cameraObserver);
        }
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
            LatLng latLng = cameraPosition.target;

            if (onCameraChangeListener!=null) {
                onCameraChangeListener.onCameraChange(cameraPosition, true);
            }

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
        }

        @Override
        public void onError(Throwable e) {
            Timber.e(e, "geocodeObserver onError");
        }

        @Override
        public void onNext(Address address) {
            MapScreenView.this.address = ApiAddress.from(address, address.getPremises());

            setAddress(MapScreenView.this.address);
            if (onAddressChangeListener!=null) {
                onAddressChangeListener.onAddressChange(MapScreenView.this.address);
            }
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
                                address = null;
                                addressTextView.setText("");
                                leaningTextView.setText("");
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
                .get(this)
                .showToolbar();
        return false;
    }

    public void setAddress(ApiAddress address) {
        Timber.v("setAddress: %s", address.toString());
        leaningTextView.setText("");
        this.address = address;
        addressTextView.setText(address.attributes().street1());
        progressBar.setVisibility(View.GONE);
        pinDrop.setVisibility(View.VISIBLE);
    }

    public void setCameraPosition(CameraPosition cameraPosition) {
        Timber.v("setCameraPosition: %s", cameraPosition.toString());
        this.cameraPosition = cameraPosition;
        if (googleMap != null) {
            initCameraPosition(googleMap);
        }
    }

    public void setOnCameraChangeListener(OnCameraChange onCameraChangeListener) {
        this.onCameraChangeListener = onCameraChangeListener;
    }

    public void setOnAddressChangeListener(OnAddressChange onAddressChangeListener) {
        this.onAddressChangeListener = onAddressChangeListener;
    }

    public void setNearbyAddresses(List<ApiAddress> nearbyAddresses) {
        this.nearbyAddresses = nearbyAddresses;
        if (googleMap==null) { return; }
        googleMap.clear();
        markerAddressMap.clear();

        Bitmap bmp = BitmapFactory.decodeResource(getResources(), R.drawable.ic_place_white_24dp);

        for(ApiAddress apiAddress : nearbyAddresses) {

            Double lat = apiAddress.attributes().latitude();
            Double lng = apiAddress.attributes().longitude();

            if (lat!=null && lng !=null) {
                String lastResponse = apiAddress.attributes().bestCanvassResponse();
                Bitmap icon = colorBitmap(bmp, ResponseColor.getColor(lastResponse, getContext()));

                Marker marker = googleMap.addMarker(new MarkerOptions()
                        .icon(BitmapDescriptorFactory.fromBitmap(icon))
                        .position(new LatLng(lat, lng))
                        .title(CanvassResponseEvaluator
                                .getText(lastResponse, getResources().getStringArray(R.array.interest))));

                markerAddressMap.put(marker.getId(), apiAddress );
            }
        }

        googleMap.setOnMarkerClickListener(onMarkerClickListener);
    }

    GoogleMap.OnMarkerClickListener onMarkerClickListener = new GoogleMap.OnMarkerClickListener() {
        @Override
        public boolean onMarkerClick(Marker marker) {

            //stop watching the camera change while the map moves to the maker
            unsubscribe();

            //set the address manually
            ApiAddress apiAddress = markerAddressMap.get(marker.getId());
            setAddress(apiAddress);
            onAddressChangeListener.onAddressChange(apiAddress);
            leaningTextView.setText(CanvassResponseEvaluator.getText(
                    apiAddress.attributes().bestCanvassResponse(),
                    getResources().getStringArray(R.array.interest)));

            //re-enable the camera watch
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    //notify the listener that the camera moved
                    onCameraChangeListener.onCameraChange(googleMap.getCameraPosition(), false);
                    connectCameraObservable(googleMap);
                }
            }, 1500);
            return false;
        }
    };

    private Bitmap colorBitmap(final Bitmap bm, int color) {

        Paint paint = new Paint();
        ColorFilter filter = new PorterDuffColorFilter(color, PorterDuff.Mode.SRC_IN);
        paint.setColorFilter(filter);
        Bitmap copiedBitmap = bm.copy(Bitmap.Config.ARGB_8888, true);
        Canvas canvas = new Canvas(copiedBitmap);
        canvas.drawBitmap(bm, 0, 0, paint);
        return copiedBitmap;
    }

    public interface OnCameraChange {
        void onCameraChange(CameraPosition cameraPosition, boolean shouldRefreshAddresses);
    }

    public interface OnAddressChange {
        void onAddressChange(ApiAddress apiAddress);
    }
}
