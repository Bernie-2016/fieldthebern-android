package org.feelthebern.android.views.holders;

import android.view.View;
import android.widget.TextView;

import org.feelthebern.android.R;
import org.feelthebern.android.adapters.BaseViewHolder;
import org.feelthebern.android.models.Content;
import org.feelthebern.android.models.H3;

/**
 *
 */
public class ErrorViewHolder extends BaseViewHolder<Content> {

    TextView textView;

    ErrorViewHolder(View itemView) {
        super(itemView);
        textView = (TextView)itemView.findViewById(R.id.text);
    }

    @Override
    public void setModel(final Content model) {
        textView.setText(model.getText());
    }
}
