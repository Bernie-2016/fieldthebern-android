package com.berniesanders.fieldthebern.views;


import android.content.Context;
import android.util.AttributeSet;
import android.widget.Button;
import android.widget.ScrollView;

import com.berniesanders.fieldthebern.R;
import com.berniesanders.fieldthebern.mortar.DaggerService;
import com.berniesanders.fieldthebern.screens.AboutScreen;
import com.f2prateek.rx.preferences.RxSharedPreferences;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import flow.Flow;

public class AboutView extends ScrollView {

    @Inject
    AboutScreen.Presenter presenter;
    @Inject
    RxSharedPreferences rxPrefs;

    public AboutView(Context context) {
        super(context);
        injectSelf(context);
    }

    public AboutView(Context context, AttributeSet attrs) {
        super(context, attrs);
        injectSelf(context);
    }

    public AboutView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        injectSelf(context);
    }

    private void injectSelf(Context context) {
        if (isInEditMode()) {
            return;
        }
        DaggerService.<AboutScreen.Component>
                getDaggerComponent(context, DaggerService.DAGGER_SERVICE)
                .inject(this);
    }


    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        ButterKnife.bind(this, this);

    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        if (isInEditMode()) return;
        presenter.takeView(this);
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        presenter.dropView(this);
    }
}
