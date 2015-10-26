package org.feelthebern.android.views.holders;

import android.view.View;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import org.feelthebern.android.R;
import org.feelthebern.android.adapters.BaseViewHolder;
import org.feelthebern.android.models.Collection;
import org.feelthebern.android.models.Img;

/**
 *
 */
public class CollectionHolder extends BaseViewHolder<Collection> {

    ImageView imageView;

    CollectionHolder(View itemView) {
        super(itemView);
        imageView = (ImageView)itemView.findViewById(R.id.img);
    }

    @Override
    public void setModel(final Collection model) {
        super.setModel(model);

        Picasso.with(imageView.getContext())
                .load(model.getImageUrlThumb())
                .into(imageView);
    }
}
