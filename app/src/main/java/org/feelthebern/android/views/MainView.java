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
import android.graphics.LightingColorFilter;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ProgressBar;

import org.feelthebern.android.R;
import org.feelthebern.android.adapters.CollectionRecyclerAdapter;
import org.feelthebern.android.models.Collection;
import org.feelthebern.android.mortar.DaggerService;
import org.feelthebern.android.screens.Main;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.ButterKnife;
import timber.log.Timber;

public class MainView extends FrameLayout {

    @Inject
    Main.Presenter presenter;

    @Bind(R.id.mainRecyclerView)
    RecyclerView recyclerView;

    @Bind(R.id.progressWheel)
    ProgressBar progressWheel;



    public MainView(Context context, AttributeSet attrs) {
        super(context, attrs);
        DaggerService.<Main.Component>
                getDaggerComponent(context, DaggerService.DAGGER_SERVICE)
                .inject(this);

    }

    private void setLayoutManager(Context context) {
        GridLayoutManager gridLayoutManager
                = new GridLayoutManager(context, context.getResources().getInteger(R.integer.num_cols));
        recyclerView.setLayoutManager(gridLayoutManager);
    }

    public GridLayoutManager getLayoutManager() {
        return (GridLayoutManager) recyclerView.getLayoutManager();
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        ButterKnife.bind(this, this);
        setLayoutManager(this.getContext());
        progressWheel.getIndeterminateDrawable().setColorFilter(new LightingColorFilter(0xFF000000, 0xFFFFFF));
        Timber.v("onFinishInflate");
    }

    @Override
    public void onAttachedToWindow() {
        super.onAttachedToWindow();
        presenter.takeView(this);
    }

    @Override
    protected void onDetachedFromWindow() {
        presenter.dropView(this);
        super.onDetachedFromWindow();
    }


    public void setData(Collection collection) {
        recyclerView.setAdapter(new CollectionRecyclerAdapter(collection));
    }

    public void showLoadingAnimation() {
        progressWheel.setVisibility(View.VISIBLE);
        recyclerView.setVisibility(View.INVISIBLE);
    }

    public void hideLoadingAnimation() {
        progressWheel.setVisibility(View.GONE);
        recyclerView.setVisibility(View.VISIBLE);
    }

}
