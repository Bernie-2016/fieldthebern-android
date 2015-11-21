package com.berniesanders.canvass.adapters;

import android.animation.ObjectAnimator;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import com.berniesanders.canvass.annotations.Layout;
import com.berniesanders.canvass.models.Page;
import com.berniesanders.canvass.screens.PageScreen;
import com.berniesanders.canvass.views.holders.ViewHolderFactory;
import com.berniesanders.canvass.models.ApiItem;
import com.berniesanders.canvass.models.Collection;
import com.berniesanders.canvass.screens.CollectionScreen;

import java.lang.annotation.Annotation;
import java.util.List;

import flow.Flow;
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

    MultiAdapter.ClickListener onGridItemClick = new MultiAdapter.ClickListener() {

        @Override
        public void onClick(Object model, View v) {
            animateClick(v);

            ApiItem apiItem = (ApiItem) model;

            Timber.v("onGridItemClick: %s %s",
                    apiItem.getClass().getSimpleName(),
                    apiItem.getTitle());

            if (apiItem instanceof Page) {
                Flow.get(v).set(new PageScreen((Page) apiItem));
            } else if (apiItem instanceof Collection) {
                Flow.get(v).set(new CollectionScreen((Collection) apiItem));
            }

            Timber.v("Flow.get v= %s", Flow.get(v).toString());
        }
    };

    void animateClick(View v) {
        ObjectAnimator.ofFloat(v, "alpha", 1f, 0f, 1f)
                .setDuration(100)
                .start();
    }

}
