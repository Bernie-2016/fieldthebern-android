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
