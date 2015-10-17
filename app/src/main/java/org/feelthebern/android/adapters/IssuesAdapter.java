package org.feelthebern.android.adapters;

import android.content.Context;
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
 * Created by AndrewOrobator on 9/6/15.
 */
public class IssuesAdapter extends BaseAdapter {
    private final List<ApiItem> mApiItems;
    private final Context mContext;

    public IssuesAdapter(Context context, List<ApiItem> apiItems) {
        mApiItems = apiItems;
        mContext = context;
    }

    @Override
    public int getCount() {
        return mApiItems.size();
    }

    @Override
    public ApiItem getItem(int position) {
        return mApiItems.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = View.inflate(mContext, R.layout.item_issue, null);
        }

        ApiItem apiItem = getItem(position);
        TextView issueTextView = (TextView) convertView.findViewById(R.id.issue_TextView);
        issueTextView.setText(apiItem.getTitle());

        ImageView issueImageView = (ImageView) convertView.findViewById(R.id.issue_ImageView);
        Picasso.with(mContext).load(apiItem.getImageUrlThumb()).into(issueImageView);

        return convertView;
    }
}
