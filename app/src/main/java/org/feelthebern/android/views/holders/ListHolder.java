package org.feelthebern.android.views.holders;

import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.feelthebern.android.R;
import org.feelthebern.android.adapters.BaseViewHolder;
import org.feelthebern.android.models.List;

/**
 *
 */
public class ListHolder extends BaseViewHolder<List> {

    LinearLayout container;

    ListHolder(View itemView) {
        super(itemView);
        container = (LinearLayout)itemView;
    }

    @Override
    public void setModel(final List model) {
        super.setModel(model);
        setTextList(model.getList());
    }

    private void setTextList(java.util.List<String> stringList) {
        container.removeAllViews();
        if (stringList == null) {
            return;
        }
        for (String listItem : stringList) {
            LinearLayout li = (LinearLayout) LayoutInflater
                    .from(container.getContext())
                    .inflate(R.layout.list_item, container, false);
            TextView textView = (TextView) li.findViewById(R.id.text);
            textView.setText(listItem);
            container.addView(li);
        }
    }
}
