package com.berniesanders.fieldthebern.views;

import android.content.Context;
import android.location.Address;
import android.util.AttributeSet;
import android.widget.EditText;
import android.widget.RelativeLayout;

import com.berniesanders.fieldthebern.R;
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
    private Address address;

    @Bind(R.id.address)
    EditText addressEditText;

    @Bind(R.id.apartment)
    EditText aparetmentEditText;

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

    public void setAddress(Address address) {
        this.address = address;
        addressEditText.setText(address.getAddressLine(0));
    }

    public Address getAddress() {
        if (addressEditText.getText()!=null) {
            address.setAddressLine(0, addressEditText.getText().toString());
        }
        return address;
    }
}
