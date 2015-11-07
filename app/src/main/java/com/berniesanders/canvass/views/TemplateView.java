package com.berniesanders.canvass.views;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.FrameLayout;

import com.berniesanders.canvass.mortar.DaggerService;
import com.berniesanders.canvass.screens.TemplateScreen;

import javax.inject.Inject;

import timber.log.Timber;

/**
 * Example mortar screen.
 * Change what it extends as needed. Any View/Layout type is fine to extend
 */
public class TemplateView extends FrameLayout {

    /**
     * Make sure you are pointing at the correct presenter type
     * YourScreen.Presenter
     */
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


    /**
     * This is how the presenter is injected on to this view
     *
     * Important to note component type is how the DaggerService finds the right component
     */
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
