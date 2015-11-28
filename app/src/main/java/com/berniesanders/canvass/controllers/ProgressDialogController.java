package com.berniesanders.canvass.controllers;

/**
 *
 */

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import mortar.Presenter;
import mortar.bundler.BundleService;
import timber.log.Timber;

import static mortar.bundler.BundleService.getBundleService;

/**
 * This is mostly just a tool for our toolbox.
 *
 * Ideally, when showing a "progress spinning wheel" it should be shown in-page or in-context.
 * For example, when the user clicks a submit button, we should replace that with the loading
 * progress spinning wheel instead of using a dialog.
 */
public class ProgressDialogController extends Presenter<ProgressDialogController.Activity> {

    public interface Activity {
        AppCompatActivity getActivity();
    }

    ProgressDialog dialog;

    /**
     * This is mostly just a tool for our toolbox.
     *
     * Ideally, when showing a "progress spinning wheel" it should be shown in-page or in-context.
     * For example, when the user clicks a submit button, we should replace that with the loading
     * progress spinning wheel instead of using a dialog.
     */
    ProgressDialogController() {
    }

    /////////////////////// Mortar //////////////////////////////////////
    @Override
    public void onLoad(Bundle savedInstanceState) {
    }

    @Override
    public void dropView(Activity view) {
        super.dropView(view);
        dismiss();
    }

    @Override
    protected BundleService extractBundleService(Activity activity) {
        return getBundleService(activity.getActivity());
    }

    @Module
    public static class ProgressDialogModule {

        @Provides
        @Singleton
        ProgressDialogController provideTemplateController() {
            return new ProgressDialogController();
        }
    }


    /////////////////////// Dialog //////////////////////////////////////

    /**
     * Shows a dialog.  Caller is responsible for calling show again after rotation if the
     * dialog needs to remain visible
     *
     * This is mostly just a tool for our toolbox.
     *
     * Ideally, when showing a "progress spinning wheel" it should be shown in-page or in-context.
     * For example, when the user clicks a submit button, we should replace that with the loading
     * progress spinning wheel instead of using a dialog.
     */
    public void show(int titleResID, int messageResID) {
        if (getView()==null) {
            Timber.w("show() called but no view was attached");
            return;
        }

        show(getView().getActivity().getString(titleResID),
                getView().getActivity().getString(messageResID));
    }

    /**
     * Shows a dialog.  Caller is responsible for calling show again after rotation if the
     * dialog needs to remain visible
     *
     * This is mostly just a tool for our toolbox.
     *
     * Ideally, when showing a "progress spinning wheel" it should be shown in-page or in-context.
     * For example, when the user clicks a submit button, we should replace that with the loading
     * progress spinning wheel instead of using a dialog.
     */
    public void show(String title, String message) {
        if (getView()==null) {
            Timber.w("show() called but no view was attached");
            return;
        }

        dialog = ProgressDialog.show(getView().getActivity(), title, message);
    }


    public void dismiss() {
        if (dialog!=null && dialog.isShowing()) {
            dialog.dismiss();
            dialog=null;
        }
    }
}
