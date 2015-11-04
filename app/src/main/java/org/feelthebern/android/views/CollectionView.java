package org.feelthebern.android.views;

import android.content.Context;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;

import org.feelthebern.android.R;
import org.feelthebern.android.mortar.DaggerService;
import org.feelthebern.android.screens.CollectionScreen;

import javax.inject.Inject;

import timber.log.Timber;

/**
 *
 */
public class CollectionView extends RecyclerView {

    @Inject
    CollectionScreen.Presenter presenter;

    public CollectionView(Context context) {
        super(context);
        injectSelf(context);
        setLayoutManager(context);
    }

    public CollectionView(Context context, AttributeSet attrs) {
        super(context, attrs);
        injectSelf(context);
        setLayoutManager(context);
    }

    public CollectionView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        injectSelf(context);
        setLayoutManager(context);
    }



    private void injectSelf(Context context) {
        DaggerService.<CollectionScreen.Component>
                getDaggerComponent(context, DaggerService.DAGGER_SERVICE)
                .inject(this);
    }

    private void setLayoutManager(Context context) {
        GridLayoutManager gridLayoutManager
                = new GridLayoutManager(context, context.getResources().getInteger(R.integer.num_cols));
        setLayoutManager(gridLayoutManager);
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
        presenter.dropView(this);
        super.onDetachedFromWindow();
    }

}
