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

import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.view.View;
import android.widget.TextView;
import butterknife.Bind;
import butterknife.ButterKnife;
import com.berniesanders.fieldthebern.R;
import com.berniesanders.fieldthebern.adapters.BaseViewHolder;
import com.berniesanders.fieldthebern.models.Linkable;

import static com.berniesanders.fieldthebern.parsing.Linky.hasLinks;
import static com.berniesanders.fieldthebern.parsing.Linky.linkify;

/**
 *
 */
public class LinkableTextHolder<M extends Linkable> extends BaseViewHolder<M> {

  @Bind(R.id.text) TextView textView;

  LinkableTextHolder(View itemView) {
    super(itemView);
    ButterKnife.bind(this, itemView);
  }

  @Override public void setModel(final M model) {
    super.setModel(model);
    setText(model);
  }

  protected void setText(final M model) {
    if (hasLinks(model.getLinks()) && hasText(model)) {
      textView.setMovementMethod(LinkMovementMethod.getInstance());
      textView.setText(Html.fromHtml(linkify(model.getLinks(), model.getText())));
    } else if (hasText(model)) {
      textView.setText(model.getText());
    }
  }

  protected boolean hasText(final M model) {
    return model.getText() != null && !model.getText().isEmpty();
  }
}
