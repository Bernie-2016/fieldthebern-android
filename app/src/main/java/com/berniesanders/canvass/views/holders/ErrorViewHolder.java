package com.berniesanders.canvass.views.holders;

import android.view.View;
import android.widget.TextView;

import com.berniesanders.canvass.adapters.BaseViewHolder;
import com.berniesanders.canvass.models.Content;

/**
 *
 */
public class ErrorViewHolder extends BaseViewHolder<Content> {

    TextView textView;

    ErrorViewHolder(View itemView) {
        super(itemView);
        textView = (TextView)itemView.findViewById(com.berniesanders.canvass.R.id.text);
    }

    @Override
    public void setModel(final Content model) {
        super.setModel(model);
        textView.setText(model.getText());
    }
}
