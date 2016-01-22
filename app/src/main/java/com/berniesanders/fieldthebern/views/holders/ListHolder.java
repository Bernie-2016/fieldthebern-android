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

import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.berniesanders.fieldthebern.R;
import com.berniesanders.fieldthebern.adapters.BaseViewHolder;
import com.berniesanders.fieldthebern.models.List;

/**
 *
 */
public class ListHolder extends BaseViewHolder<List> {

  LinearLayout container;

  ListHolder(View itemView) {
    super(itemView);
    container = (LinearLayout) itemView;
  }

  @Override public void setModel(final List model) {
    super.setModel(model);
    setTextList(model.getList());
  }

  private void setTextList(java.util.List<String> stringList) {
    container.removeAllViews();
    if (stringList == null) {
      return;
    }
    for (String listItem : stringList) {
      LinearLayout li = (LinearLayout) LayoutInflater.from(container.getContext())
          .inflate(R.layout.list_item, container, false);
      TextView textView = (TextView) li.findViewById(R.id.text);
      textView.setText(listItem);
      container.addView(li);
    }
  }
}
