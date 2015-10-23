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
import org.feelthebern.android.models.Page;
import org.feelthebern.android.screens.PageScreen;

import java.util.List;

import flow.Flow;
import timber.log.Timber;

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

        convertView.setTag(apiItem);

        convertView.setOnClickListener(onGridItemClick);

        return convertView;
    }


    View.OnClickListener onGridItemClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            ApiItem tagData = (ApiItem) v.getTag();

            Timber.v("onGridItemClick: %s %s",
                    tagData.getClass().getSimpleName(),
                    tagData.getTitle());

            if (tagData instanceof Page) {
                Flow.get(v).set(new PageScreen((Page) tagData));
            }

            Timber.v("Flow.get v= %s", Flow.get(v).toString());
        }
    };
}
