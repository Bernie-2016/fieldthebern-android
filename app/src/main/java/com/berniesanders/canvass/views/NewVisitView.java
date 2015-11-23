package com.berniesanders.canvass.views;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;

import com.berniesanders.canvass.R;
import com.berniesanders.canvass.mortar.DaggerService;
import com.berniesanders.canvass.screens.AddPersonScreen;
import com.berniesanders.canvass.screens.NewVisitScreen;
import com.berniesanders.canvass.screens.ScoreScreen;
import com.berniesanders.canvass.screens.TemplateScreen;

import javax.inject.Inject;

import butterknife.ButterKnife;
import butterknife.OnClick;
import flow.Flow;
import timber.log.Timber;

/**
 * Example mortar screen.
 * Change what it extends as needed. Any View/Layout type is fine to extend
 */
public class NewVisitView extends RelativeLayout {

    /**
     * Make sure you are pointing at the correct presenter type
     * YourScreen.Presenter
     */
    @Inject
    NewVisitScreen.Presenter presenter;

    public NewVisitView(Context context) {
        super(context);
        injectSelf(context);
    }

    public NewVisitView(Context context, AttributeSet attrs) {
        super(context, attrs);
        injectSelf(context);
    }

    public NewVisitView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        injectSelf(context);
    }


    /**
     * This is how the presenter is injected on to this view
     *
     * Important to note component type is how the DaggerService finds the right component
     */
    private void injectSelf(Context context) {
        if (isInEditMode()) {return;}
        DaggerService.<NewVisitScreen.Component>
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
        presenter.takeView(this);
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        presenter.dropView(this);
    }

    @OnClick(R.id.add_person)
    public void addPerson() {
        Flow.get(this).set(new AddPersonScreen());
    }

    @OnClick(R.id.submit)
    public void score() {
        Flow.get(this).set(new ScoreScreen());
    }
}
