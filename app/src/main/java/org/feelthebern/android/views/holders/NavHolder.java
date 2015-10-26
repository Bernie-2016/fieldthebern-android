package org.feelthebern.android.views.holders;

import android.view.View;
import android.widget.TextView;

import org.feelthebern.android.R;
import org.feelthebern.android.adapters.BaseViewHolder;
import org.feelthebern.android.models.H3;
import org.feelthebern.android.models.Nav;

/**
 *
 */
public class NavHolder extends BaseViewHolder<Nav> {

    TextView textView;

    NavHolder(View itemView) {
        super(itemView);
        textView = (TextView)itemView.findViewById(R.id.text);
    }

    @Override
    public void setModel(final Nav model) {
        textView.setText(model.getText());
    }
}
