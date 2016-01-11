package com.berniesanders.fieldthebern.views;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.FrameLayout;

import com.berniesanders.fieldthebern.mortar.DaggerService;
import com.berniesanders.fieldthebern.screens.ProfileScreen;
import com.berniesanders.fieldthebern.screens.ProfileSettingsScreen;

import javax.inject.Inject;

/**
 * Example mortar view.
 * Change what it extends as needed. Any View/Layout type is fine to extend
 */
public class ProfileSettingsView extends FrameLayout {

    /**
     * Make sure you are pointing at the correct presenter type
     * YourScreen.Presenter
     */
    @Inject
    ProfileSettingsScreen.Presenter presenter;

    public ProfileSettingsView(Context context) {
        super(context);
        injectSelf(context);
    }

    public ProfileSettingsView(Context context, AttributeSet attrs) {
        super(context, attrs);
        injectSelf(context);
    }

    public ProfileSettingsView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        injectSelf(context);
    }


    /**
     * This is how the presenter is injected on to this view.
     * Important to note component type is how the DaggerService finds the right component
     */
    private void injectSelf(Context context) {
        if (isInEditMode()) { return; }
        DaggerService.<ProfileSettingsScreen.Component>
                getDaggerComponent(context, DaggerService.DAGGER_SERVICE)
                .inject(this);
    }


    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        if (isInEditMode()) { return; }
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
