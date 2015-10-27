package org.feelthebern.android.views.holders;

import android.view.View;
import android.widget.TextView;

import org.feelthebern.android.R;
import org.feelthebern.android.adapters.BaseViewHolder;
import org.feelthebern.android.models.H3;
import org.feelthebern.android.models.P;

/**
 *
 */
public class PHolder extends BaseViewHolder<P> {

    TextView textView;

    PHolder(View itemView) {
        super(itemView);
        textView = (TextView)itemView.findViewById(R.id.text);
    }

    @Override
    public void setModel(final P model) {
        super.setModel(model);
        textView.setText(model.getText());
    }
}
