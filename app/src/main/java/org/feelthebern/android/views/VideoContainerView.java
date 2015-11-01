package org.feelthebern.android.views;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.FrameLayout;

/**
 *
 */
public class VideoContainerView extends FrameLayout {

    OnDetachListener onDetachListener;

    public VideoContainerView(Context context) {
        super(context);
    }

    public VideoContainerView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public VideoContainerView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }


    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        if (onDetachListener!=null) {
            onDetachListener.onDetach(this);
        }
    }

    public void setOnDetachListener(OnDetachListener onDetachListener) {
        this.onDetachListener = onDetachListener;
    }

    public interface OnDetachListener {
        void onDetach(VideoContainerView videoContainerView);
    }
}
