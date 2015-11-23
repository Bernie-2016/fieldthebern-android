package com.berniesanders.canvass.views;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.RelativeLayout;

import com.berniesanders.canvass.mortar.DaggerService;
import com.berniesanders.canvass.screens.LoginScreen;

import javax.inject.Inject;

import timber.log.Timber;

/**
 */
public class LoginView extends RelativeLayout {

    /**
     */
    @Inject
    LoginScreen.Presenter presenter;


    public LoginView(Context context) {
        super(context);
        injectSelf(context);
    }

    public LoginView(Context context, AttributeSet attrs) {
        super(context, attrs);
        injectSelf(context);
    }

    public LoginView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        injectSelf(context);
    }


    /**
     */
    private void injectSelf(Context context) {
        if (isInEditMode()) { return; }
        DaggerService.<LoginScreen.Component>
                getDaggerComponent(context, DaggerService.DAGGER_SERVICE)
                .inject(this);
    }


    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        if (isInEditMode()) { return; }
        Timber.v("onFinishInflate");
        //ButterKnife.bind(this, this);
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        if (isInEditMode()) { return; }
        presenter.takeView(this);
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        presenter.dropView(this);
    }

}
