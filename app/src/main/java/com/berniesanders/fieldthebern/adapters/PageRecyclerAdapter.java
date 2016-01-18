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

import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

import com.berniesanders.fieldthebern.annotations.Layout;
import com.berniesanders.fieldthebern.models.Content;
import com.berniesanders.fieldthebern.models.Page;
import com.berniesanders.fieldthebern.views.holders.ViewHolderFactory;

import java.lang.annotation.Annotation;
import java.util.List;

/**
 * {@inheritDoc}
 */
public class PageRecyclerAdapter extends MultiAdapter {


    private final List<Content> items;

    public PageRecyclerAdapter(Page page) {
        this.items = page.getContent();
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return ViewHolderFactory.create(viewType, parent);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ((BaseViewHolder) holder).setModel(items.get(position));
        //((BaseViewHolder) holder).setItemClickListener(itemClickListener);
    }



    @Override
    public int getItemViewType(final int position) {
        Content item = items.get(position);
        Annotation annotation = item.getClass().getAnnotation(Layout.class);
        Layout layoutAnnotation = (Layout) annotation;
        return layoutAnnotation.value();
    }



}
