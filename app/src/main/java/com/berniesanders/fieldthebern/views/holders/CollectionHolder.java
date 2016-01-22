/*
 * Copyright (c) 2016 - Bernie 2016, Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */

package com.berniesanders.fieldthebern.views.holders;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import butterknife.Bind;
import butterknife.ButterKnife;
import com.berniesanders.fieldthebern.R;
import com.berniesanders.fieldthebern.adapters.BaseViewHolder;
import com.berniesanders.fieldthebern.models.Collection;
import com.squareup.picasso.Picasso;

/**
 *
 */
public class CollectionHolder extends BaseViewHolder<Collection> {

  @Bind(R.id.img)
  ImageView imageView;

  @Bind(R.id.txt)
  TextView textView;

  CollectionHolder(View itemView) {
    super(itemView);
    ButterKnife.bind(this, itemView);
    itemView.setOnClickListener(this);
  }

  @Override
  public void setModel(final Collection model) {
    super.setModel(model);

    Picasso.with(imageView.getContext()).load(model.getImageUrlThumb()).into(imageView);

    textView.setText(model.getTitle());
  }
}
