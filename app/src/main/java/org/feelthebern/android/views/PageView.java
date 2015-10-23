package org.feelthebern.android.views;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.LinearLayout;

import org.feelthebern.android.R;
import org.feelthebern.android.mortar.DaggerService;
import org.feelthebern.android.screens.PageScreen;

import javax.inject.Inject;

/**
 *
 */

public class PageView extends LinearLayout {

    @Inject
    PageScreen.Presenter presenter;

    public PageView(Context context) {
        super(context);
        injectSelf(context);
    }

    public PageView(Context context, AttributeSet attrs) {
        super(context, attrs);
        injectSelf(context);
    }

    public PageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        injectSelf(context);
    }



    private void injectSelf(Context context) {
        DaggerService.<PageScreen.Component>
                getDaggerComponent(context, DaggerService.DAGGER_SERVICE)
//                getDaggerComponent(context, PageScreen.class.getName())
                .inject(this);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        presenter.takeView(this);
    }

    @Override
    protected void onDetachedFromWindow() {
        presenter.dropView(this);
        super.onDetachedFromWindow();
    }

}
