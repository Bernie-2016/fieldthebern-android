package com.berniesanders.canvass.views.holders;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import com.berniesanders.canvass.R;
import com.berniesanders.canvass.adapters.BaseViewHolder;
import com.berniesanders.canvass.models.Page;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 *
 */
public class PageHolder extends BaseViewHolder<Page> {

    @Bind(R.id.img)
    ImageView imageView;

    @Bind(R.id.txt)
    TextView textView;

    PageHolder(View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
        itemView.setOnClickListener(this);
    }

    @Override
    public void setModel(final Page model) {
        super.setModel(model);

        Picasso.with(imageView.getContext())
                .load(model.getImageUrlThumb())
                .into(imageView);

        textView.setText(model.getTitle());
    }
}
