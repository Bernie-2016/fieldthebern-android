package org.feelthebern.android.views.holders;

import android.view.View;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import org.feelthebern.android.R;
import org.feelthebern.android.adapters.BaseViewHolder;
import org.feelthebern.android.models.Img;

/**
 *
 */
public class ImgHolder extends BaseViewHolder<Img> {

    ImageView imageView;

    ImgHolder(View itemView) {
        super(itemView);
        imageView = (ImageView)itemView.findViewById(R.id.img);
    }

    @Override
    public void setModel(final Img model) {
        super.setModel(model);
        Picasso.with(imageView.getContext())
                .load(model.getText())
                .into(imageView);
    }
}
