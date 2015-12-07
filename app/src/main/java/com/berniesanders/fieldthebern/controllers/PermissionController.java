package com.berniesanders.fieldthebern.controllers;

/**
 *
 */

import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;

import com.berniesanders.fieldthebern.exceptions.LocationUnavailableException;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import mortar.Presenter;
import mortar.bundler.BundleService;
import rx.functions.Action0;
import rx.functions.Action1;
import timber.log.Timber;

import static android.Manifest.permission.ACCESS_COARSE_LOCATION;
import static android.Manifest.permission.ACCESS_FINE_LOCATION;
import static android.content.pm.PackageManager.PERMISSION_GRANTED;
import static mortar.bundler.BundleService.getBundleService;

/**
 * Provides a way to coordinate with the MainActivity to request permissions
 */
public class PermissionController extends Presenter<PermissionController.Activity> {

    public static final int REQ_CODE_PERMISSIONS = 33;
    private Action0 onComplete;

    public interface Activity {
        AppCompatActivity getActivity();
    }

    /**
     */
    PermissionController() {
    }

    @Override
    public void onLoad(Bundle savedInstanceState) {
    }

    @Override
    public void dropView(Activity view) {
        //after this it is no longer safe to call getView()
        super.dropView(view);
    }



    public void requestPermission() {
        //Action0 onComplete
        //this.onComplete = onComplete;
        if ( Build.VERSION.SDK_INT >= 23 &&
                ContextCompat.checkSelfPermission(getView().getActivity(), ACCESS_FINE_LOCATION) != PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(getView().getActivity(), ACCESS_COARSE_LOCATION) != PERMISSION_GRANTED) {

            String[] permissions = {ACCESS_FINE_LOCATION, ACCESS_COARSE_LOCATION};
            ActivityCompat.requestPermissions(getView().getActivity(), permissions, REQ_CODE_PERMISSIONS);
        }
    }

    /**
     *
     * @param grantResults either android.content.pm.PackageManager.PERMISSION_GRANTED or
     *                     android.content.pm.PackageManager.PERMISSION_DENIED.
     */
    public void onResult(int reqCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        //onComplete.call();
        Timber.v("permission result...");
    }

    /**
     * required by mortar
     */
    @Override
    protected BundleService extractBundleService(Activity activity) {
        return getBundleService(activity.getActivity());
    }

    /**
     */
    @Module
    public static class PermissionModule {

        @Provides
        @Singleton
        PermissionController providePermissionController() {
            return new PermissionController();
        }
    }
}
