package com.berniesanders.canvass.views;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.FrameLayout;

import com.berniesanders.canvass.mortar.DaggerService;
import com.berniesanders.canvass.screens.AddAddressScreen;
import com.berniesanders.canvass.screens.TemplateScreen;

import javax.inject.Inject;

import timber.log.Timber;

/**
 *
 */
public class TemplateView extends FrameLayout {

    @Inject
    TemplateScreen.Presenter presenter;

    public TemplateView(Context context) {
        super(context);
        injectSelf(context);

    }

    public TemplateView(Context context, AttributeSet attrs) {
        super(context, attrs);
        injectSelf(context);
    }

    public TemplateView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        injectSelf(context);
    }



    private void injectSelf(Context context) {
        DaggerService.<TemplateScreen.Component>
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
