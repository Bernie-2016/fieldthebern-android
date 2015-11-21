package com.berniesanders.canvass.views.holders;

import android.view.View;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import com.berniesanders.canvass.R;
import com.berniesanders.canvass.adapters.BaseViewHolder;
import com.berniesanders.canvass.models.Img;
import com.berniesanders.canvass.screens.PhotoScreen;

import flow.Flow;

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

        imageView.setOnClickListener(onClickListener);
    }


    View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Flow.get(v).set(new PhotoScreen(model));
        }
    };
}
