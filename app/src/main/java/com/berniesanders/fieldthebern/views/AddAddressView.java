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

import android.content.Context;
import android.location.Address;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.widget.EditText;
import android.widget.RelativeLayout;

import com.berniesanders.fieldthebern.R;
import com.berniesanders.fieldthebern.models.ApiAddress;
import com.berniesanders.fieldthebern.mortar.DaggerService;
import com.berniesanders.fieldthebern.screens.AddAddressScreen;
import com.berniesanders.fieldthebern.screens.NewVisitScreen;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import flow.Flow;
import timber.log.Timber;

/**
 *
 */
public class AddAddressView extends RelativeLayout {

    @Inject
    AddAddressScreen.Presenter presenter;

    private ApiAddress address;

    @Bind(R.id.address)
    EditText addressEditText;

    @Bind(R.id.apartment)
    EditText apartmentEditText;

    public AddAddressView(Context context) {
        super(context);
        injectSelf(context);

    }

    public AddAddressView(Context context, AttributeSet attrs) {
        super(context, attrs);
        injectSelf(context);
    }

    public AddAddressView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        injectSelf(context);
    }



    private void injectSelf(Context context) {
        if (isInEditMode()) { return; }
        DaggerService.<AddAddressScreen.Component>
                getDaggerComponent(context, DaggerService.DAGGER_SERVICE)
                .inject(this);
    }


    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        if (isInEditMode()) { return; }
        Timber.v("onFinishInflate");
        ButterKnife.bind(this, this);
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        presenter.takeView(this);
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        presenter.dropView(this);
    }

    public void setAddress(ApiAddress address) {
        this.address = address;
        addressEditText.setText(address.attributes().street1());

        if (address.attributes().street2() != null) {
            apartmentEditText.setText(address.attributes().street2());
        }
    }

    public ApiAddress getAddress() {

        if (addressEditText.getText()!=null) {
            address.attributes().street1(addressEditText.getText().toString());
        }

        if (apartmentEditText.getText()!=null) {
            address.attributes().street2(apartmentEditText.getText().toString());
        }

        return address;
    }
}
