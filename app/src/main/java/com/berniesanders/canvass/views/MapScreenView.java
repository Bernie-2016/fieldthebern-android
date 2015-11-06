package com.berniesanders.canvass.views;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.util.AttributeSet;
import android.widget.FrameLayout;

import com.berniesanders.canvass.R;
import com.berniesanders.canvass.mortar.DaggerService;
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

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import flow.path.Path;
import flow.path.PathContext;
import timber.log.Timber;

/**
 *
 */
public class MapScreenView extends FrameLayout {

    MapFragment mapFragment;
    GoogleMap googleMap;
    WeakReference<Activity> activityWeakReference;

    @Bind(R.id.address_btn)
    FloatingActionButton fab;

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
                    gmap.animateCamera(CameraUpdateFactory.newCameraPosition(
                                    getCurrentLocationCam()),
                            300,
                            new GoogleMap.CancelableCallback() {
                                @Override
                                public void onFinish() {
                                    Timber.v("animateCamera CancelableCallback onFinish");
                                }

                                @Override
                                public void onCancel() {
                                    Timber.w("animateCamera CancelableCallback onCancel");
                                }
                            });

                }
            });
        }

        ButterKnife.bind(this, this);
    }

    @OnClick(R.id.address_btn)
    public void addNewAddress() {
        Timber.v("addNewAddress click");
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
                        .getFragmentManager().beginTransaction().remove(f).commit();
            }

            activityWeakReference.clear();
        }
    }


    //TODO implement actual location code
    CameraPosition getCurrentLocationCam() {

        LocationManager locationManager = (LocationManager) getContext().getSystemService(Context.LOCATION_SERVICE);
        if (ActivityCompat.checkSelfPermission(activityWeakReference.get(),
                Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(activityWeakReference.get(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            //return null //TODO;
            Timber.e("location permission error");
            return null;
        }


        Location location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
        return new CameraPosition
                .Builder()
                .target(new LatLng(location.getLatitude(), location.getLongitude()))
                .zoom(18f)
                .build();

    }

}
