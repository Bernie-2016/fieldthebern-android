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

package com.berniesanders.fieldthebern.views.holders;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.berniesanders.fieldthebern.R;
import com.berniesanders.fieldthebern.adapters.BaseViewHolder;

import timber.log.Timber;

/**
 *
 */
public class ViewHolderFactory {


    /**
     * @return a subclass of BaseViewHolder.
     */
    public static BaseViewHolder create(int resId, ViewGroup parent) {

        switch (resId) {
            case R.layout.row_h1:
                return new H1Holder(inflate(resId, parent));

            case R.layout.row_h2:
                return new H2Holder(inflate(resId, parent));

            case R.layout.row_h3:
                return new H3Holder(inflate(resId, parent));

            case R.layout.row_img:
                return new ImgHolder(inflate(resId, parent));

            case R.layout.row_nav:
                return new NavHolder(inflate(resId, parent));

            case R.layout.row_quote:
                return new QuoteHolder(inflate(resId, parent));

            case R.layout.row_p:
                return new PHolder(inflate(resId, parent));

            case R.layout.row_video:
                return new VideoHolder(inflate(resId, parent));

            case R.layout.row_list:
                return new ListHolder(inflate(resId, parent));

            case R.layout.item_collection:
                return new CollectionHolder(inflate(resId, parent));

            case R.layout.item_page:
                return new PageHolder(inflate(resId, parent));

            default:
                Timber.e(new Exception(),
                        "Unmapped view holder... res id: %d, returning default",
                        resId);

                return new ErrorViewHolder(inflate(R.layout.row_error, parent));
        }

    }

    private static View inflate(int resId, ViewGroup parent) {
        return LayoutInflater
                .from(parent.getContext())
                .inflate(resId, parent, false);
    }
}
