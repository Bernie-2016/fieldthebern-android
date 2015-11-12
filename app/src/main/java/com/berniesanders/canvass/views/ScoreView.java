package com.berniesanders.canvass.views;

import android.content.Context;
import android.support.v7.widget.AppCompatSpinner;
import android.util.AttributeSet;
import android.widget.ArrayAdapter;
import android.widget.RelativeLayout;

import com.berniesanders.canvass.R;
import com.berniesanders.canvass.mortar.DaggerService;
import com.berniesanders.canvass.screens.AddPersonScreen;
import com.berniesanders.canvass.screens.MapScreen;
import com.berniesanders.canvass.screens.ScoreScreen;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import flow.Flow;
import flow.History;
import timber.log.Timber;

/**
 */
public class ScoreView extends RelativeLayout {

    /**
     */
    @Inject
    ScoreScreen.Presenter presenter;


    public ScoreView(Context context) {
        super(context);
        injectSelf(context);
    }

    public ScoreView(Context context, AttributeSet attrs) {
        super(context, attrs);
        injectSelf(context);
    }

    public ScoreView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        injectSelf(context);
    }


    /**
     */
    private void injectSelf(Context context) {
        if (isInEditMode()) { return; }
        DaggerService.<ScoreScreen.Component>
                getDaggerComponent(context, DaggerService.DAGGER_SERVICE)
                .inject(this);
    }


    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        if (isInEditMode()) { return; }
        Timber.v("onFinishInflate");
        ButterKnife.bind(this, this);
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

    @OnClick(R.id.submit)
    public void score() {
        Flow.get(this).setHistory(History.single(new MapScreen()), Flow.Direction.REPLACE);
    }

}
