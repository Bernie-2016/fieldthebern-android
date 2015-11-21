package com.berniesanders.canvass.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

import com.berniesanders.canvass.annotations.Layout;
import com.berniesanders.canvass.models.Content;
import com.berniesanders.canvass.models.Page;
import com.berniesanders.canvass.views.holders.ViewHolderFactory;

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
