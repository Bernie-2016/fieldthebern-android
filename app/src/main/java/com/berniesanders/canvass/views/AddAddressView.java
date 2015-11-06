package com.berniesanders.canvass.views;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.berniesanders.canvass.FTBApplication;
import com.berniesanders.canvass.R;
import com.berniesanders.canvass.events.ShowToolbarEvent;
import com.berniesanders.canvass.mortar.DaggerService;
import com.berniesanders.canvass.mortar.HandlesBack;
import com.berniesanders.canvass.screens.AddAddressScreen;
import com.berniesanders.canvass.screens.PhotoScreen;

import javax.inject.Inject;

import timber.log.Timber;
import uk.co.senab.photoview.PhotoViewAttacher;

/**
 *
 */
public class AddAddressView extends RelativeLayout {

    @Inject
    AddAddressScreen.Presenter presenter;

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
        DaggerService.<AddAddressScreen.Component>
                getDaggerComponent(context, DaggerService.DAGGER_SERVICE)
                .inject(this);
    }


    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        Timber.v("onFinishInflate");
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

}
