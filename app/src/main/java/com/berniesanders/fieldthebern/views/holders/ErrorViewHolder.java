package com.berniesanders.fieldthebern.views.holders;

import android.view.View;
import android.widget.TextView;

import com.berniesanders.fieldthebern.R;
import com.berniesanders.fieldthebern.adapters.BaseViewHolder;
import com.berniesanders.fieldthebern.models.Content;

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
        super.setModel(model);
        textView.setText(model.getText());
    }
}
