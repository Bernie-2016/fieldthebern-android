package com.berniesanders.fieldthebern.views;


import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.widget.Button;
import android.widget.FrameLayout;

import com.berniesanders.fieldthebern.R;
import com.berniesanders.fieldthebern.adapters.LearnPagerAdapter;
import com.berniesanders.fieldthebern.models.User;
import com.berniesanders.fieldthebern.mortar.DaggerService;
import com.berniesanders.fieldthebern.screens.ChooseSignupScreen;
import com.berniesanders.fieldthebern.screens.HomeScreen;
import com.berniesanders.fieldthebern.screens.LearnScreen;
import com.berniesanders.fieldthebern.screens.Main;
import com.f2prateek.rx.preferences.RxSharedPreferences;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import flow.Flow;
import flow.History;
import me.relex.circleindicator.CircleIndicator;

public class LearnView extends FrameLayout {

    @Bind(R.id.viewpager)
    ViewPager viewPager;
    @Bind(R.id.circleIndicator)
    CircleIndicator circleIndicator;
    @Bind(R.id.doneButton)
    Button doneButton;

    @Inject
    LearnScreen.Presenter presenter;
    @Inject
    RxSharedPreferences rxPrefs;

    public LearnView(Context context) {
        super(context);
        injectSelf(context);
    }

    public LearnView(Context context, AttributeSet attrs) {
        super(context, attrs);
        injectSelf(context);
    }

    public LearnView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        injectSelf(context);
    }

    private void injectSelf(Context context) {
        if (isInEditMode()) {
            return;
        }
        DaggerService.<LearnScreen.Component>
                getDaggerComponent(context, DaggerService.DAGGER_SERVICE)
                .inject(this);
    }


    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        ButterKnife.bind(this, this);
        viewPager.setAdapter(new LearnPagerAdapter(getContext()));
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
    @OnClick(R.id.doneButton)
    public void done() {
        Flow.get(this).goBack();
    }
}
