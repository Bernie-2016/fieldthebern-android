package org.feelthebern.android.views.holders;

import android.view.View;
import android.widget.TextView;

import org.feelthebern.android.R;
import org.feelthebern.android.adapters.BaseViewHolder;
import org.feelthebern.android.models.H1;
import org.feelthebern.android.models.H3;

/**
 *
 */
public class H3Holder extends BaseViewHolder<H3> {

    TextView textView;

    H3Holder(View itemView) {
        super(itemView);
        textView = (TextView)itemView.findViewById(R.id.text);
    }

    @Override
    public void setModel(final H3 model) {
        textView.setText(model.getText());
    }
}
