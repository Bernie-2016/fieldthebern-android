package org.feelthebern.android.views;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;

import org.feelthebern.android.mortar.DaggerService;
import org.feelthebern.android.screens.PageScreen;

import javax.inject.Inject;

import timber.log.Timber;

/**
 *
 */
public class PageView extends RecyclerView {

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
                .inject(this);
    }

    private void setLayoutManager(Context context) {
        LinearLayoutManager llm = new LinearLayoutManager(context);
        setLayoutManager(llm);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        Timber.v("onFinishInflate");
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        setLayoutManager(getContext());
        presenter.takeView(this);
    }

    @Override
    protected void onDetachedFromWindow() {
        presenter.dropView(this);
        super.onDetachedFromWindow();
    }


}
