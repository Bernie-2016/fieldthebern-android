package com.berniesanders.canvass.views;

import android.content.Context;
import android.support.v7.widget.AppCompatButton;
import android.text.Html;
import android.util.AttributeSet;
import android.widget.LinearLayout;

import com.berniesanders.canvass.R;
import com.berniesanders.canvass.mortar.DaggerService;
import com.berniesanders.canvass.screens.HomeScreen;
import com.berniesanders.canvass.screens.Main;
import com.berniesanders.canvass.screens.MapScreen;
import com.berniesanders.canvass.screens.TemplateScreen;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.BindString;
import butterknife.ButterKnife;
import butterknife.OnClick;
import flow.Flow;
import timber.log.Timber;

/**
 * Created by Patrick on 11/9/2015.
 */
public class HomeView extends LinearLayout {


    /**
     * Make sure you are pointing at the correct presenter type
     * YourScreen.Presenter
     */
    @Inject
    HomeScreen.Presenter presenter;

    public HomeView(Context context) {
        super(context);
        injectSelf(context);
    }

    public HomeView(Context context, AttributeSet attrs) {
        super(context, attrs);
        injectSelf(context);
    }

    public HomeView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        injectSelf(context);
    }


    /**
     * This is how the presenter is injected on to this view
     *
     * Important to note component type is how the DaggerService finds the right component
     */
    private void injectSelf(Context context) {
        DaggerService.<HomeScreen.Component>
                getDaggerComponent(context, DaggerService.DAGGER_SERVICE)
                .inject(this);
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
        super.onDetachedFromWindow();
        presenter.dropView(this);
    }

}
