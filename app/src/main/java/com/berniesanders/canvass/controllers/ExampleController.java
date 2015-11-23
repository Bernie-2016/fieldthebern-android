package com.berniesanders.canvass.controllers;

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
 * Provides a way to coordinate with the MainActivity
 */
public class ExampleController extends Presenter<ExampleController.Activity> {

    public interface Activity {
        AppCompatActivity getActivity();
    }

    /**
     * package private
     * only instantiated via the ExampleControllerModule dagger module inner class below
     */
    ExampleController() {
    }

    @Override
    public void onLoad(Bundle savedInstanceState) {
        //you can safely call getView() to get the activity here
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
     * Used to inject this singleton presenter/controller onto the Activity.
     * Add the module to the MainComponent, then create a field on the MainActivity
     * Then you can add the "service" to access this presenter/controller
     * in MainActivity.initActivityScope()
     */
    @Module
    public static class ExampleControllerModule {

        @Provides
        @Singleton
        ExampleController provideTemplateController() {
            return new ExampleController();
        }
    }
}
