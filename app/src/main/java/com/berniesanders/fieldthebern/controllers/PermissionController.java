package com.berniesanders.fieldthebern.controllers;

/**
 *
 */

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import mortar.Presenter;
import mortar.bundler.BundleService;

import static mortar.bundler.BundleService.getBundleService;

/**
 * Provides a way to coordinate with the MainActivity to request permissions
 */
public class PermissionController extends Presenter<PermissionController.Activity> {

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
