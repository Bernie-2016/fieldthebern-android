package com.berniesanders.fieldthebern.views;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.LinearLayout;
import com.berniesanders.fieldthebern.mortar.DaggerService;
import com.berniesanders.fieldthebern.screens.EarlyStateScreen;
import javax.inject.Inject;

/**
 */
public class EarlyStateView extends LinearLayout {

  /**
   */
  @Inject
  EarlyStateScreen.Presenter presenter;

  public EarlyStateView(Context context) {
    super(context);
    injectSelf(context);
  }

  public EarlyStateView(Context context, AttributeSet attrs) {
    super(context, attrs);
    injectSelf(context);
  }

  public EarlyStateView(Context context, AttributeSet attrs, int defStyleAttr) {
    super(context, attrs, defStyleAttr);
    injectSelf(context);
  }

  /**
   * This is how the presenter is injected on to this view.
   * Important to note component type is how the DaggerService finds the right component
   */
  private void injectSelf(Context context) {
    if (isInEditMode()) {
      return;
    }
    DaggerService.<EarlyStateScreen.Component>getDaggerComponent(context,
        DaggerService.DAGGER_SERVICE).inject(this);
  }

  @Override
  protected void onFinishInflate() {
    super.onFinishInflate();
    if (isInEditMode()) {
      return;
    }
    //ButterKnife.bind(this, this);
  }

  @Override
  protected void onAttachedToWindow() {
    super.onAttachedToWindow();
    if (isInEditMode()) {
      return;
    }
    presenter.takeView(this);
  }

  @Override
  protected void onDetachedFromWindow() {
    super.onDetachedFromWindow();
    presenter.dropView(this);
  }
}
