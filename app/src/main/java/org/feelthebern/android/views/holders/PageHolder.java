package org.feelthebern.android.views.holders;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import org.feelthebern.android.R;
import org.feelthebern.android.adapters.BaseViewHolder;
import org.feelthebern.android.models.Collection;
import org.feelthebern.android.models.Page;

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
