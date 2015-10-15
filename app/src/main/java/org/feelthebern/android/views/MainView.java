/*
 * Copyright 2014 Square Inc.
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
 * Copyright 2015 FeelTheBern.org
 */

package org.feelthebern.android.views;


import android.content.Context;
import android.util.AttributeSet;
import android.widget.GridView;
import android.widget.LinearLayout;

import org.feelthebern.android.R;
import org.feelthebern.android.issues.IssuesAdapter;
import org.feelthebern.android.models.Collection;
import org.feelthebern.android.mortar.DaggerService;
import org.feelthebern.android.screens.Main;

import javax.inject.Inject;

public class MainView extends LinearLayout {

    @Inject
    Main.Presenter presenter;

    private GridView gridView;


    public MainView(Context context, AttributeSet attrs) {
        super(context, attrs);
        DaggerService.<Main.Component>getDaggerComponent(context).inject(this);
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


    public void setData(Collection collection) {
        gridView = (GridView) findViewById(R.id.issues_GridView);
        gridView.setAdapter(new IssuesAdapter(getContext(), collection.getApiItems()));
    }

}
