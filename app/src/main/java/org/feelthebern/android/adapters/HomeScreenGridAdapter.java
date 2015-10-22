package org.feelthebern.android.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import org.feelthebern.android.R;
import org.feelthebern.android.models.ApiItem;

import java.util.List;

/**
 */
public class HomeScreenGridAdapter extends BaseAdapter {
    private final List<ApiItem> apiItems;
    private final Context context;

    public HomeScreenGridAdapter(Context context, List<ApiItem> apiItems) {
        this.apiItems = apiItems;
        this.context = context;
    }

    @Override
    public int getCount() {
        return apiItems.size();
    }

    @Override
    public ApiItem getItem(int position) {
        return apiItems.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater
                    .from(parent.getContext())
                    .inflate(R.layout.item_issue, parent, false);
        }

        ApiItem apiItem = getItem(position);
        TextView issueTextView = (TextView) convertView.findViewById(R.id.issue_TextView);
        ImageView issueImageView = (ImageView) convertView.findViewById(R.id.issue_ImageView);

        issueTextView.setText(apiItem.getTitle());

        Picasso.with(context)
                .load(apiItem.getImageUrlThumb())
                .into(issueImageView);

        return convertView;
    }
}
