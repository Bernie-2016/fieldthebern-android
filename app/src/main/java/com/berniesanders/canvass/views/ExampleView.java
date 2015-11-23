package com.berniesanders.canvass.views;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.FrameLayout;

import com.berniesanders.canvass.mortar.DaggerService;
import com.berniesanders.canvass.screens.ExampleScreen;

import javax.inject.Inject;

import timber.log.Timber;

/**
 * Example mortar screen.
 * Change what it extends as needed. Any View/Layout type is fine to extend
 */
public class ExampleView extends FrameLayout {

    /**
     * Make sure you are pointing at the correct presenter type
     * YourScreen.Presenter
     */
    @Inject
    ExampleScreen.Presenter presenter;

    public ExampleView(Context context) {
        super(context);
        injectSelf(context);
    }

    public ExampleView(Context context, AttributeSet attrs) {
        super(context, attrs);
        injectSelf(context);
    }

    public ExampleView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        injectSelf(context);
    }


    /**
     * This is how the presenter is injected on to this view.
     * Important to note component type is how the DaggerService finds the right component
     */
    private void injectSelf(Context context) {
        if (isInEditMode()) { return; }
        DaggerService.<ExampleScreen.Component>
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
        presenter.takeView(this);
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        presenter.dropView(this);
    }

}
