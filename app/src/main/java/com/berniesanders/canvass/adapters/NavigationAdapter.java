package com.berniesanders.canvass.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.berniesanders.canvass.R;

/**
 *
 */
public class NavigationAdapter extends BaseAdapter {


    private final String[] items;

    public NavigationAdapter(String [] items) {

        this.items = items;
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

        TextView textView = (TextView) convertView.findViewById(R.id.drawer_list_item_text);
        textView.setText(items[position]);
        return convertView;
    }
}
