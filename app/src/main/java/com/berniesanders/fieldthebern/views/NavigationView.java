package com.berniesanders.fieldthebern.views;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.LinearLayout;

import com.berniesanders.fieldthebern.FTBApplication;
import com.berniesanders.fieldthebern.screens.DaggerNavigationScreen_Component;
import com.berniesanders.fieldthebern.screens.NavigationScreen;

import javax.inject.Inject;

import timber.log.Timber;

/**
 * Example mortar screen.
 * Change what it extends as needed. Any View/Layout type is fine to extend
 */
public class NavigationView extends LinearLayout {

    /**
     * Make sure you are pointing at the correct presenter type
     * YourScreen.Presenter
     */
    @Inject
    NavigationScreen.Presenter presenter;

    NavigationScreen.Component daggerComponent;

    public NavigationView(Context context) {
        super(context);
        injectSelf(context);
    }

    public NavigationView(Context context, AttributeSet attrs) {
        super(context, attrs);
        injectSelf(context);
    }

    public NavigationView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        injectSelf(context);
    }


    /**
     * This is a bit unusual method of injection because
     * the nav sits outside the normal flow container/process
     */
    private void injectSelf(Context context) {
        if (isInEditMode()) { return; }
        createComponent().inject(this);
    }

    private NavigationScreen.Component createComponent() {
         return DaggerNavigationScreen_Component
                .builder()
                .mainComponent(FTBApplication.getComponent())
                .build();
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
