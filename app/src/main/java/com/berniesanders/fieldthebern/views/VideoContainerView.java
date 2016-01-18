/*
 * Copyright (c) 2016 - Bernie 2016, Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */

package com.berniesanders.fieldthebern.views;

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
