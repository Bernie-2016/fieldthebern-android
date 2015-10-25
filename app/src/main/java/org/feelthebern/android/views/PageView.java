package org.feelthebern.android.views;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;

import org.feelthebern.android.mortar.DaggerService;
import org.feelthebern.android.screens.PageScreen;

import javax.inject.Inject;

/**
 *
 */
public class PageView extends RecyclerView {

    @Inject
    PageScreen.Presenter presenter;

    public PageView(Context context) {
        super(context);
        injectSelf(context);
        setLayoutManager(context);
    }

    public PageView(Context context, AttributeSet attrs) {
        super(context, attrs);
        injectSelf(context);
        setLayoutManager(context);
    }

    public PageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        injectSelf(context);
        setLayoutManager(context);
    }



    private void injectSelf(Context context) {
        DaggerService.<PageScreen.Component>
                getDaggerComponent(context, DaggerService.DAGGER_SERVICE)
//                getDaggerComponent(context, PageScreen.class.getName())
                .inject(this);
    }

    private void setLayoutManager(Context context) {
        LinearLayoutManager llm = new LinearLayoutManager(context);
        setLayoutManager(llm);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
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
