package org.feelthebern.android.views.holders;

import android.view.View;
import android.widget.TextView;

import org.feelthebern.android.R;
import org.feelthebern.android.adapters.BaseViewHolder;
import org.feelthebern.android.models.H3;
import org.feelthebern.android.models.Video;

/**
 *
 */
public class VideoHolder extends BaseViewHolder<Video> {

    TextView textView;

    VideoHolder(View itemView) {
        super(itemView);
        textView = (TextView)itemView.findViewById(R.id.text);
    }

    @Override
    public void setModel(final Video model) {
        super.setModel(model);
        textView.setText(model.getText());
    }
}
