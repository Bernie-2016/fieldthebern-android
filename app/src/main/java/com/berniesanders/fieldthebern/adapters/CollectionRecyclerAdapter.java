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

package com.berniesanders.fieldthebern.adapters;

import android.animation.ObjectAnimator;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import com.berniesanders.fieldthebern.annotations.Layout;
import com.berniesanders.fieldthebern.models.ApiItem;
import com.berniesanders.fieldthebern.models.Collection;
import com.berniesanders.fieldthebern.models.Page;
import com.berniesanders.fieldthebern.screens.CollectionScreen;
import com.berniesanders.fieldthebern.screens.PageScreen;
import com.berniesanders.fieldthebern.views.holders.ViewHolderFactory;
import flow.Flow;
import java.lang.annotation.Annotation;
import java.util.List;
import timber.log.Timber;

/**
 * {@inheritDoc}
 */
public class CollectionRecyclerAdapter extends MultiAdapter {

  private final List<ApiItem> items;

  public CollectionRecyclerAdapter(Collection parentCollection) {
    this.items = parentCollection.getApiItems();
  }

  @Override
  public int getItemCount() {
    return items.size();
  }

  @Override
  public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
    BaseViewHolder holder = ViewHolderFactory.create(viewType, parent);
    return holder;
  }

  @Override
  public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
    ((BaseViewHolder) holder).setModel(items.get(position));
    ((BaseViewHolder) holder).setItemClickListener(onGridItemClick);
  }

  @Override
  public int getItemViewType(final int position) {
    ApiItem item = items.get(position);
    Annotation annotation = item.getClass().getAnnotation(Layout.class);
    Layout layoutAnnotation = (Layout) annotation;
    return layoutAnnotation.value();
  }

  MultiAdapter.ClickListener onGridItemClick = (model, v) -> {
    animateClick(v);

    ApiItem apiItem = (ApiItem) model;

    Timber.v("onGridItemClick: %s %s", apiItem.getClass().getSimpleName(), apiItem.getTitle());

    if (apiItem instanceof Page) {
      Flow.get(v).set(new PageScreen((Page) apiItem));
    } else if (apiItem instanceof Collection) {
      Flow.get(v).set(new CollectionScreen((Collection) apiItem));
    }

    Timber.v("Flow.get v= %s", Flow.get(v).toString());
  };

  void animateClick(View v) {
    ObjectAnimator.ofFloat(v, "alpha", 1f, 0f, 1f).setDuration(100).start();
  }
}
