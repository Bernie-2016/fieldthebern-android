package org.feelthebern.android.views.holders;

import android.view.View;
import android.widget.TextView;

import org.feelthebern.android.R;
import org.feelthebern.android.adapters.BaseViewHolder;
import org.feelthebern.android.models.H1;

/**
 *
 */
public class H1Holder extends BaseViewHolder<H1> {

    TextView textView;

    H1Holder(View itemView) {
        super(itemView);
        textView = (TextView)itemView.findViewById(R.id.text);
    }

    @Override
    public void setModel(final H1 model) {
        super.setModel(model);
        textView.setText(model.getText());
    }
}
