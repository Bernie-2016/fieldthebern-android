package com.berniesanders.canvass.views;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.support.v7.widget.AppCompatSpinner;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.LinearInterpolator;
import android.widget.ArrayAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;

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

    @Bind(R.id.points_container)
    ViewGroup pointsContainer;

    @Bind(R.id.for_knocking)
    TextView forKnocking;
    @Bind(R.id.for_updating)
    TextView forUpdating;

    @Bind(R.id.points_label)
    TextView pointsLabel;

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

    public void animateScore() {

        final int[] values = {0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15};

        ValueAnimator valueAnimator = ValueAnimator
                .ofInt(values)
                .setDuration(1000);
        valueAnimator.setInterpolator(new DecelerateInterpolator());
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                    @Override
                    public void onAnimationUpdate(ValueAnimator animation) {
                        TextView pointstextView1 = (TextView) LayoutInflater
                                .from(getContext())
                                .inflate(R.layout.points, pointsContainer, false);
                        Timber.v("onAnimationUpdate");
                        int val = (int) animation.getAnimatedValue();
                        pointstextView1.setText(String.valueOf(val));


                        long duration = (long) (600); //animation.getAnimatedFraction()
                        ObjectAnimator scaleX = ObjectAnimator
                                .ofFloat(pointstextView1, View.SCALE_X, 1f, 1.5f, 1f)
                                .setDuration(duration);

                        ObjectAnimator scaleY = ObjectAnimator
                                .ofFloat(pointstextView1, View.SCALE_Y, 1f, 1.5f, 1f)
                                .setDuration(duration);

                        scaleX.setInterpolator(new DecelerateInterpolator());
                        scaleY.setInterpolator(new DecelerateInterpolator());

                        scaleX.start();
                        scaleY.start();

                        pointsContainer.addView(pointstextView1);

                        if (val < 15) {
                            ObjectAnimator fadeIn = ObjectAnimator
                                    .ofFloat(pointstextView1, View.ALPHA, 0f, 1f, 0f)
                                    .setDuration(duration/2);
                            fadeIn.start();
                        }
                    }
        });
        valueAnimator.start();
    }

    public void animateLabels() {
        forKnocking.setX(forKnocking.getX() + 300);
        forUpdating.setX(forUpdating.getX() + 300);
        //pointsLabel.setX(pointsLabel.getX() - 300);
        forKnocking.setAlpha(0);
        forUpdating.setAlpha(0);
        pointsLabel.setAlpha(0);

        forKnocking.animate().alpha(1).xBy(-300).setDuration(300).setStartDelay(1300).start();
        forUpdating.animate().alpha(1).xBy(-300).setDuration(300).setStartDelay(1400).start();
        pointsLabel.animate().alpha(1).setDuration(300).setStartDelay(200).start();
    }
}
