package org.feelthebern.android.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.feelthebern.android.R;
import org.feelthebern.android.annotations.Layout;
import org.feelthebern.android.models.Content;
import org.feelthebern.android.models.Img;
import org.feelthebern.android.models.Page;
import org.feelthebern.android.views.holders.ViewHolderFactory;

import java.lang.annotation.Annotation;
import java.util.List;

import timber.log.Timber;

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
