package com.berniesanders.fieldthebern;


import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.widget.FrameLayout;

import com.berniesanders.fieldthebern.adapters.CustomPagerAdapter;
import com.berniesanders.fieldthebern.mortar.DaggerService;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.ButterKnife;
import me.relex.circleindicator.CircleIndicator;

/**
 * Example mortar view.
 * Change what it extends as needed. Any View/Layout type is fine to extend
 */
public class AppIntroView extends FrameLayout {

    @Bind(R.id.viewpager)
    ViewPager viewPager;
    @Bind(R.id.circleIndicator)
    CircleIndicator circleIndicator;
    /**
     * Make sure you are pointing at the correct presenter type
     * YourScreen.Presenter
     */
    @Inject
    AppIntroScreen.Presenter presenter;

    public AppIntroView(Context context) {
        super(context);
        injectSelf(context);
    }

    public AppIntroView(Context context, AttributeSet attrs) {
        super(context, attrs);
        injectSelf(context);
    }

    public AppIntroView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        injectSelf(context);
    }


    /**
     * This is how the presenter is injected on to this view.
     * Important to note component type is how the DaggerService finds the right component
     */
    private void injectSelf(Context context) {
        if (isInEditMode()) {
            return;
        }
        DaggerService.<AppIntroScreen.Component>
                getDaggerComponent(context, DaggerService.DAGGER_SERVICE)
                .inject(this);
    }


    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        ButterKnife.bind(this, this);
        viewPager.setAdapter(new CustomPagerAdapter(getContext()));
        circleIndicator.setViewPager(viewPager);
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