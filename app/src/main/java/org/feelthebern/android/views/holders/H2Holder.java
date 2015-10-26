package org.feelthebern.android.views.holders;

import android.view.View;
import android.widget.TextView;

import org.feelthebern.android.R;
import org.feelthebern.android.adapters.BaseViewHolder;
import org.feelthebern.android.models.H1;
import org.feelthebern.android.models.H2;

/**
 *
 */
public class H2Holder extends BaseViewHolder<H2> {

    TextView textView;

    H2Holder(View itemView) {
        super(itemView);
        textView = (TextView)itemView.findViewById(R.id.text);
    }

    @Override
    public void setModel(final H2 model) {
        textView.setText(model.getText());
    }
}
