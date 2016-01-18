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

package com.berniesanders.fieldthebern.controllers;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.berniesanders.fieldthebern.R;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import mortar.Presenter;
import mortar.bundler.BundleService;

import static mortar.bundler.BundleService.getBundleService;

/**
 *
 */
public class ToastController extends Presenter<ToastController.Activity> {

    public interface Activity {
        AppCompatActivity getActivity();
    }

    ToastController() {

    }

    public void bern(String text) {
        bern(text, Toast.LENGTH_LONG);
    }

    public void bern(String text, int toastDuration) {
        //Toast.makeText(getView().getActivity(), text, Toast.LENGTH_LONG).show();

        View layout = LayoutInflater
                .from(getView().getActivity())
                .inflate(R.layout.toast,
                        (ViewGroup) getView().getActivity().findViewById(R.id.toast_layout_root),
                        false
                );

        ImageView image = (ImageView) layout.findViewById(R.id.image);
        image.setImageResource(R.drawable.ic_info_outline_white_24dp);
        TextView textView = (TextView) layout.findViewById(R.id.text);
        textView.setText(text);

        Toast toast = new Toast(getView().getActivity());
        toast.setGravity(Gravity.CENTER_VERTICAL, 0, 0);
        toast.setDuration(toastDuration);
        toast.setView(layout);
        toast.show();
    }

    @Override
    protected void onLoad(Bundle savedInstanceState) {
        super.onLoad(savedInstanceState);
    }

    @Override
    public void dropView(Activity view) {
        super.dropView(view);
    }

    @Override
    protected BundleService extractBundleService(Activity activity) {
        return getBundleService(activity.getActivity());
    }


    @Module
    public static class ToastModule {

        @Provides
        @Singleton
        ToastController provideToastController() {
            return new ToastController();
        }
    }
}
