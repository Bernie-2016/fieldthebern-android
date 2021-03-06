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
import android.support.design.widget.Snackbar;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import butterknife.Bind;
import butterknife.ButterKnife;
import com.berniesanders.fieldthebern.R;
import com.berniesanders.fieldthebern.controllers.ActionBarService;
import com.berniesanders.fieldthebern.controllers.LocationService;
import com.berniesanders.fieldthebern.controllers.PermissionService;
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
import flow.Flow;
import flow.path.Path;
import flow.path.PathContext;
import java.lang.ref.WeakReference;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import javax.inject.Inject;
import org.apache.commons.lang3.reflect.FieldUtils;
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
  private OnMarkerClick onMarkerClick;
  private CameraPosition cameraPosition;
  private Map<String, ApiAddress> markerAddressMap = new HashMap<>();
  private List<ApiAddress> nearbyAddresses;
  private boolean isAttached = false;

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
        activityWeakReference = new WeakReference<>((Activity) ctx);
      }
    }

    DaggerService.<MapScreen.Component>getDaggerComponent(context,
        DaggerService.DAGGER_SERVICE).inject(this);
  }

  @Override
  protected void onFinishInflate() {
    super.onFinishInflate();
    Timber.v("onFinishInflate");

    if (activityWeakReference != null) {
      mapFragment = (MapFragment) activityWeakReference.get()
          .getFragmentManager()
          .findFragmentById(R.id.map_frag);

      mapFragment.getMapAsync(new OnMapReadyCallback() {

        @Override
        public void onMapReady(GoogleMap gmap) {
          Timber.v("OnMapReadyCallback");
          if (PermissionService.get(MapScreenView.this).isGranted()) {
            MapScreenView.this.googleMap = gmap;
            gmap.setMyLocationEnabled(true);
            gmap.getUiSettings().setMapToolbarEnabled(false);
            initCameraPosition(gmap);
          } else {
            // Display a SnackBar with an explanation and a button to trigger the request.
            Snackbar.make(MapScreenView.this,
                R.string.permission_contacts_rationale,
                Snackbar.LENGTH_INDEFINITE)
                .setAction(android.R.string.ok, new View.OnClickListener() {
                  @Override
                  public void onClick(View view) {
                    PermissionService.get(MapScreenView.this).requestPermission();
                  }
                })
                .show();
          }
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
    isAttached = true;
  }

  @Override
  protected void onDetachedFromWindow() {
    isAttached = false;
    presenter.dropView(this);
    super.onDetachedFromWindow();
    if (activityWeakReference != null) {

      MapFragment f = (MapFragment) activityWeakReference.get()
          .getFragmentManager()
          .findFragmentById(R.id.map_frag);

      if (f != null) {
        activityWeakReference.get().getFragmentManager().beginTransaction().remove(f).commit();

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
    handler.removeCallbacksAndMessages(null);

    if (cameraSubscription != null) {
      cameraSubscription.unsubscribe();
    }
    if (locationSubscription != null) {
      locationSubscription.unsubscribe();
    }
    if (geocodeSubscription != null) {
      geocodeSubscription.unsubscribe();
    }

    if (googleMap != null) {//thanks fragments
      googleMap.setOnCameraChangeListener(null);
    }
  }

  private void initCameraPosition(final GoogleMap map) {

    Timber.v("initCameraPosition");
    if (nearbyAddresses != null) {
      setNearbyAddresses(nearbyAddresses);
    }

    if (cameraPosition != null) {
      //if we're already there, bail early
      if (map.getCameraPosition().equals(cameraPosition)) {
        return;
      }

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
        map.moveCamera(CameraUpdateFactory.newCameraPosition(getCameraPosition(location)));
        connectCameraObservable(map);

        geocodeSubscription = LocationService.get(MapScreenView.this)
            .reverseGeocode(new LatLng(location.getLatitude(), location.getLongitude()))
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .single()
            .subscribe(geocodeObserver);
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
      cameraSubscription = watchCamera(map).subscribeOn(AndroidSchedulers.mainThread())
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
    public void onNext(final CameraPosition cameraPosition) {

      post(new Runnable() {
        @Override
        public void run() {

          if (isAttached) {
            LatLng latLng = cameraPosition.target;

            if (onCameraChangeListener != null) {
              onCameraChangeListener.onCameraChange(cameraPosition, true, getRadius());
            }

            geocodeSubscription = LocationService.get(MapScreenView.this)
                .reverseGeocode(latLng)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(geocodeObserver);
          }
        }
      });
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
      if (onAddressChangeListener != null) {
        onAddressChangeListener.onAddressChange(MapScreenView.this.address);
      }
    }
  };

  private CameraPosition getCameraPosition(Location location) {
    return new CameraPosition.Builder().target(
        new LatLng(location.getLatitude(), location.getLongitude())).zoom(18f).build();
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

    ActionBarService.get(this).showToolbar();
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

  public void setOnMarkerClick(OnMarkerClick onMarkerClick) {
    this.onMarkerClick = onMarkerClick;
  }

  public void setNearbyAddresses(List<ApiAddress> nearbyAddresses) {
    if (nearbyAddresses == null) {
      return;
    }
    this.nearbyAddresses = nearbyAddresses;
    if (googleMap == null) {
      return;
    }
    googleMap.clear();
    markerAddressMap.clear();

    Bitmap bmp = BitmapFactory.decodeResource(getResources(), R.drawable.ic_place_white_24dp);

    for (ApiAddress apiAddress : nearbyAddresses) {

      Double lat = apiAddress.attributes().latitude();
      Double lng = apiAddress.attributes().longitude();

      if (lat != null && lng != null) {
        String lastResponse = apiAddress.attributes().bestCanvassResponse();
        Bitmap icon = colorBitmap(bmp, ResponseColor.getColor(lastResponse, getContext()));

        Marker marker = googleMap.addMarker(
            new MarkerOptions().icon(BitmapDescriptorFactory.fromBitmap(icon))
                .position(new LatLng(lat, lng))
                .title(CanvassResponseEvaluator.getText(lastResponse,
                    getResources().getStringArray(R.array.interest))));

        markerAddressMap.put(marker.getId(), apiAddress);
      }
    }

    googleMap.setOnMarkerClickListener(onMarkerClickListener);
  }

  public int getRadius() {

    double startLat = googleMap.getProjection().getVisibleRegion().latLngBounds.northeast.latitude;
    double startLng = googleMap.getProjection().getVisibleRegion().latLngBounds.northeast.longitude;
    double endLat = googleMap.getProjection().getVisibleRegion().latLngBounds.southwest.latitude;
    double endLng = googleMap.getProjection().getVisibleRegion().latLngBounds.southwest.longitude;

    float[] results = new float[10];
    Location.distanceBetween(startLat, startLng, endLat, endLng, results);
    Timber.v("getRadius: %f", Math.ceil(results[0]));
    return (int) Math.ceil(results[0]);
  }

  GoogleMap.OnMarkerClickListener onMarkerClickListener = new GoogleMap.OnMarkerClickListener() {
    @Override
    public boolean onMarkerClick(Marker marker) {

      //stop watching the camera change while the map moves to the maker
      unsubscribe();
      if (onMarkerClick != null) {
        onMarkerClick.onMarkerClick();
      }

      //set the address manually
      ApiAddress apiAddress = markerAddressMap.get(marker.getId());
      setAddress(apiAddress);
      onAddressChangeListener.onAddressChange(apiAddress);
      leaningTextView.setText(
          CanvassResponseEvaluator.getText(apiAddress.attributes().bestCanvassResponse(),
              getResources().getStringArray(R.array.interest)));

      //re-enable the camera watch
      handler.postDelayed(new Runnable() {
        @Override
        public void run() {
          if (onCameraChangeListener != null) {
            //notify the listener that the camera moved
            onCameraChangeListener.onCameraChange(googleMap.getCameraPosition(), false, 100);
            connectCameraObservable(googleMap);
          }
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

  public ApiAddress getCurrentAddress() {
    return address;
  }

  public interface OnCameraChange {
    void onCameraChange(CameraPosition cameraPosition, boolean shouldRefreshAddresses, int radius);
  }

  public interface OnAddressChange {
    void onAddressChange(ApiAddress apiAddress);
  }

  public interface OnMarkerClick {
    void onMarkerClick();
  }
}
