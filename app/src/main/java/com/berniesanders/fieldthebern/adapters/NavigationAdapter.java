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

package com.berniesanders.fieldthebern.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.berniesanders.fieldthebern.R;

/**
 *
 */
public class NavigationAdapter extends BaseAdapter {


    private final String[] items;
    private final int[] icons;

    public NavigationAdapter(String [] items, int[] icons) {
        this.items = items;
        this.icons = icons;
    }

    @Override
    public int getCount() {
        return items.length;
    }

    @Override
    public Object getItem(int position) {
        return items[position];
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if (convertView==null) {
            convertView = LayoutInflater
                    .from(parent.getContext())
                    .inflate(R.layout.drawer_list_item, parent, false);
        }

        ImageView imageView = (ImageView) convertView.findViewById(R.id.icon);
        imageView.setImageResource(icons[position]);
        TextView textView = (TextView) convertView.findViewById(R.id.drawer_list_item_text);
        textView.setText(items[position]);
        return convertView;
    }
}
