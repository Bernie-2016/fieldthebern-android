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

package com.berniesanders.fieldthebern.adapters;

/**
 *
 */
import android.support.v7.widget.RecyclerView;
import android.view.View;

import timber.log.Timber;


/**
 * A base ViewHolder that has a Model type "M". to set the mode call setModel()
 */

public abstract class BaseViewHolder<M>
        extends RecyclerView.ViewHolder
        implements View.OnClickListener {

    protected M model;
    protected MultiAdapter.ClickListener adapterListener;


    public BaseViewHolder(View view) {
        super(view);
    }


    /**
     * Not enabled by default.
     * To enable in your view, add <br /><br />
     * {@code this.setOnClickListener(this);} <br /><br />
     * in your constructor.
     */
    @Override
    public void onClick(View v) {
        Timber.v("onClick"+v);

        if (getModel() == null) {
            Timber.v("getModel was null :"+v);
            return;
        }

        if (adapterListener != null) {
            adapterListener.onClick(model, v);
        }
    }

    public void setModel(M model) {
        this.model = model;
    }

    @SuppressWarnings("UnusedDeclaration")
    public M getModel() {
        return model;
    }

    public void setItemClickListener(MultiAdapter.ClickListener adapterListener) {
        this.adapterListener = adapterListener;
    }
}
