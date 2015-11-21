package com.berniesanders.canvass.location;

import android.annotation.TargetApi;
import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.Build;
import android.support.v4.content.ContextCompat;

import com.berniesanders.canvass.exceptions.AddressUnavailableException;
import com.berniesanders.canvass.exceptions.LocationUnavailableException;
import com.google.android.gms.common.api.GoogleApiClient;

import org.apache.commons.lang3.StringUtils;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import javax.inject.Inject;

import rx.Observable;
import rx.Subscriber;

import static android.Manifest.permission.ACCESS_COARSE_LOCATION;
import static android.Manifest.permission.ACCESS_FINE_LOCATION;
import static android.content.pm.PackageManager.PERMISSION_GRANTED;
/**
 *
 */
public class LocationAdapter
//        GoogleApiClient.ConnectionCallbacks,
//        GoogleApiClient.OnConnectionFailedListener,
//        LocationSource,
//        LocationListener
{

    GoogleApiClient googleApiClient;
    Context context;
    Location location;
    LocationManager locationManager;

    @Inject
    public LocationAdapter(Context context, LocationManager locationManager) {

        this.context = context;
        this.locationManager = locationManager;
    }

    public Observable<Location> get() {
        return Observable.create(new Observable.OnSubscribe<Location>() {
            @Override
            public void call(Subscriber<? super Location> subscriber) {
                subscriber.onError(new LocationUnavailableException("test"));
//                try {
//                    subscriber.onNext(getLocation());
//                    subscriber.onCompleted();
//                } catch (LocationUnavailableException e) {
//                    subscriber.onError(e);
//                }

           }
        });
    }

    @TargetApi(23)
    private Location getLocation() throws LocationUnavailableException {
        if ( Build.VERSION.SDK_INT >= 23 &&
                ContextCompat.checkSelfPermission(context, ACCESS_FINE_LOCATION) != PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(context, ACCESS_COARSE_LOCATION) != PERMISSION_GRANTED) {
            throw new LocationUnavailableException("location permission error");
        }

        LocationManager locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);


        location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);

        if (location==null) {
            throw new LocationUnavailableException("location was null");
        }

        return location;
    }


    public Observable<String> getAddress(){
        return Observable.create(new Observable.OnSubscribe<String>() {
            @Override
            public void call(Subscriber<? super String> subscriber) {
                subscriber.onError(new AddressUnavailableException("test"));
//                try {
//                    String stateCode = getStateForLocation(getLocation());
//                    subscriber.onNext(stateCode);
//                    subscriber.onCompleted();
//                } catch (LocationUnavailableException e) {
//                    subscriber.onError(e);
//                }
            }
        });
    }

    private String getStateForLocation(Location location) throws AddressUnavailableException {
        // Errors could still arise from using the Geocoder (for example, if there is no
        // connectivity, or if the Geocoder is given illegal location data). Or, the Geocoder may
        // simply not have an address for a location. In all these cases, we communicate with the
        // receiver using a resultCode indicating failure. If an address is found, we use a
        // resultCode indicating success.

        // The Geocoder used in this sample. The Geocoder's responses are localized for the given
        // Locale, which represents a specific geographical or linguistic region. Locales are used
        // to alter the presentation of information such as numbers or dates to suit the conventions
        // in the region they describe.
        Geocoder geocoder = new Geocoder(context, Locale.getDefault());

        // Address found using the Geocoder.
        List<Address> addresses = null;
        Address address = null;
        try {
            // Using getFromLocation() returns an array of Addresses for the area immediately
            // surrounding the given latitude and longitude. The results are a best guess and are
            // not guaranteed to be accurate.
            addresses = geocoder.getFromLocation(
                    location.getLatitude(),
                    location.getLongitude(),
                    // In this sample, we get just a single address.
                    1);
        } catch (IOException ioException) {
            // Catch network or other I/O problems.
            throw new AddressUnavailableException();
            //Log.e(TAG, errorMessage, ioException);
        } catch (IllegalArgumentException illegalArgumentException) {
            // Catch invalid latitude or longitude values.
            //errorMessage = getString(R.string.invalid_lat_long_used);
            //Log.e(TAG, errorMessage + ". " +
            //        "Latitude = " + location.getLatitude() +
            //        ", Longitude = " + location.getLongitude(), illegalArgumentException);
            throw new AddressUnavailableException();
        }

        // Handle case where no address was found.
        if (addresses == null || addresses.size()  == 0) {
//            if (errorMessage.isEmpty()) {
//                errorMessage = getString(R.string.no_address_found);
//                Log.e(TAG, errorMessage);
//            }
            //deliverResultToReceiver(Constants.FAILURE_RESULT, errorMessage);
            throw new AddressUnavailableException();
        } else {
            address = addresses.get(0);
            //ArrayList<String> addressFragments = new ArrayList<String>();

            // Fetch the address lines using {@code getAddressLine},
            // join them, and send them to the thread. The {@link android.location.address}
            // class provides other options for fetching address details that you may prefer
            // to use. Here are some examples:
            // getLocality() ("Mountain View", for example)
            // getAdminArea() ("CA", for example)
            // getPostalCode() ("94043", for example)
            // getCountryCode() ("US", for example)
            // getCountryName() ("United States", for example)
            //for(int i = 0; i < address.getMaxAddressLineIndex(); i++) {
            //    addressFragments.add(address.getAddressLine(i));
            //}
//            Log.i(TAG, getString(R.string.address_found));
//            deliverResultToReceiver(Constants.SUCCESS_RESULT,
//                    TextUtils.join(System.getProperty("line.separator"), addressFragments));

            if (address == null || StringUtils.isBlank(address.getAdminArea())){
                throw new AddressUnavailableException("address or state code was null/blank");
            }
        }


        return StateConverter.getStateCode(address.getAdminArea());
    }

//    protected synchronized void buildGoogleApiClient() {
//        googleApiClient = new GoogleApiClient.Builder(context)
//                .addConnectionCallbacks(this)
//                .addOnConnectionFailedListener(this)
//                .addApi(LocationServices.API)
//                .build();
//    }
//
//    @Override
//    public void onConnected(Bundle bundle) {
//
//    }
//
//    @Override
//    public void onConnectionSuspended(int i) {
//
//    }
//
//    @Override
//    public void onConnectionFailed(ConnectionResult connectionResult) {
//
//    }
//
//    @Override
//    public void onLocationChanged(Location location) {
//
//    }
//
//    @Override
//    public void activate(OnLocationChangedListener onLocationChangedListener) {
//
//    }
//
//    @Override
//    public void deactivate() {
//
//    }


}
