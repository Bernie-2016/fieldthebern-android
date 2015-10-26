package org.feelthebern.android.views.holders;

import android.view.View;
import android.widget.TextView;

import org.feelthebern.android.R;
import org.feelthebern.android.adapters.BaseViewHolder;
import org.feelthebern.android.models.H3;
import org.feelthebern.android.models.Quote;

/**
 *
 */
public class QuoteHolder extends BaseViewHolder<Quote> {

    TextView textView;

    QuoteHolder(View itemView) {
        super(itemView);
        textView = (TextView)itemView.findViewById(R.id.text);
    }

    @Override
    public void setModel(final Quote model) {
        super.setModel(model);
        textView.setText(model.getText());
    }
}
