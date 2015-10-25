package org.feelthebern.android.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.feelthebern.android.R;
import org.feelthebern.android.models.Content;
import org.feelthebern.android.models.Page;

import java.util.List;

/**
 *
 */
public class PageRecyclerAdapter extends RecyclerView.Adapter<PageRecyclerAdapter.ContentItemViewHolder>{


    private final List<Content> items;

    public PageRecyclerAdapter (Page page) {

        this.items = page.getContent();
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    @Override
    public ContentItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.row_h1, parent, false);
        return new ContentItemViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ContentItemViewHolder holder, int position) {
        holder.textView.setText(items.get(position).getText());
    }





    public static class ContentItemViewHolder extends RecyclerView.ViewHolder {
        TextView textView;

        ContentItemViewHolder(View itemView) {
            super(itemView);
            textView = (TextView)itemView.findViewById(R.id.row_h1_text);
        }
    }
}
