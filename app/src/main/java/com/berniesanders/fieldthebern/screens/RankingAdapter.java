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
    super(context, R.layout.row_profile, new String[userDatas.size()]);
    this.context = context;
    this.userDatas = userDatas;
    this.datas = datas;
  }

  public View getView(int position, View view, ViewGroup viewGroup) {
    View newView = LayoutInflater.from(context).inflate(R.layout.row_profile, null, true);

    UserAttributes attributes = this.userDatas.get(position).attributes();
    Rankings.Attributes attributes1 = this.datas.get(position).attributes();

    // position
    TextView positionView = (TextView) newView.findViewById(R.id.position);
    positionView.setText(attributes1.rank());

    // name
    String name = attributes.getFirstName() + " " + attributes.getLastName();
    TextView nameView = (TextView) newView.findViewById(R.id.name);
    nameView.setText(name);

    //score
    TextView scoreView = (TextView) newView.findViewById(R.id.score);
    String score = String.valueOf(attributes1.score());
    scoreView.setText(score);

    //pic
    ImageView avatar = (ImageView) newView.findViewById(R.id.profile_image);
    Picasso.with(newView.getContext()).load(attributes.getPhotoThumbUrl()).into(avatar);

    return newView;
  }
}
