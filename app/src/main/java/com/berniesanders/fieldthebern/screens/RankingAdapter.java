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

package com.berniesanders.fieldthebern.screens;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import butterknife.Bind;
import butterknife.ButterKnife;
import com.berniesanders.fieldthebern.R;
import com.berniesanders.fieldthebern.models.Rankings;
import com.berniesanders.fieldthebern.models.UserAttributes;
import com.berniesanders.fieldthebern.models.UserData;
import com.squareup.picasso.Picasso;
import java.util.List;

/**
 */
public class RankingAdapter extends ArrayAdapter<String> {
  private final Context context;
  private List<UserData> userDatas;
  private List<Rankings.Data> datas;

  public RankingAdapter(Context context, List<UserData> userDatas, List<Rankings.Data> datas) {
    super(context, R.layout.screen_profile_row, new String[userDatas.size()]);
    this.context = context;
    this.userDatas = userDatas;
    this.datas = datas;
  }

  public View getView(int position, View view, ViewGroup viewGroup) {
    ViewHolder viewHolder;
    if (view != null) {
      viewHolder = (ViewHolder) view.getTag();
    } else {
      view = LayoutInflater.from(context).inflate(R.layout.screen_profile_row, null, true);
      viewHolder = new ViewHolder(view);
    }
    UserAttributes attributes = this.userDatas.get(position).attributes();
    Rankings.Attributes attributes1 = this.datas.get(position).attributes();

    String name = attributes.getFirstName() + " " + attributes.getLastName();
    String score = String.valueOf(attributes1.score());
    viewHolder.bind(view.getContext(), attributes1.rank(), name, score,
        attributes.getPhotoThumbUrl());
    return view;
  }

  private class ViewHolder {
    @Bind(R.id.position)
    TextView position;
    @Bind(R.id.name)
    TextView name;
    @Bind(R.id.score)
    TextView score;
    @Bind(R.id.profile_image)
    ImageView avatar;

    public ViewHolder(View view) {
      ButterKnife.bind(this, view);
    }

    public void bind(Context context, String position, String name, String score, String avatar) {
      this.position.setText(position);
      this.name.setText(name);
      this.score.setText(score);
      Picasso.with(context).load(avatar).into(this.avatar);
    }
  }
}
