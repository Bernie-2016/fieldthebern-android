package org.feelthebern.android.views;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.FrameLayout;
import android.widget.ImageView;

import org.feelthebern.android.FTBApplication;
import org.feelthebern.android.R;
import org.feelthebern.android.events.ShowToolbarEvent;
import org.feelthebern.android.mortar.DaggerService;
import org.feelthebern.android.mortar.HandlesBack;
import org.feelthebern.android.screens.PhotoScreen;

import javax.inject.Inject;

import timber.log.Timber;
import uk.co.senab.photoview.PhotoViewAttacher;

/**
 *
 */
public class PhotoScreenView extends FrameLayout implements HandlesBack {

    @Inject
    PhotoScreen.Presenter presenter;
    PhotoViewAttacher attacher;

    public PhotoScreenView(Context context) {
        super(context);
        injectSelf(context);

    }

    public PhotoScreenView(Context context, AttributeSet attrs) {
        super(context, attrs);
        injectSelf(context);
    }

    public PhotoScreenView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        injectSelf(context);
    }



    private void injectSelf(Context context) {
        DaggerService.<PhotoScreen.Component>
                getDaggerComponent(context, DaggerService.DAGGER_SERVICE)
                .inject(this);
    }


    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        Timber.v("onFinishInflate");
        attacher = new PhotoViewAttacher(getImageView());
        attacher.setScaleType(ImageView.ScaleType.CENTER_CROP);
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        presenter.takeView(this);
    }

    @Override
    protected void onDetachedFromWindow() {
        presenter.dropView(this);
        if (attacher!=null) {
            attacher.cleanup();
        }
        super.onDetachedFromWindow();
    }

    public ImageView getImageView() {
        return (ImageView) findViewById(R.id.iv_photo);
    }


    @Override
    public boolean onBackPressed() {
        new ShowToolbarEvent()
                .with(FTBApplication.getEventBus())
                .showToolbar(true)
                .dispatch();
        return false;
    }
}
