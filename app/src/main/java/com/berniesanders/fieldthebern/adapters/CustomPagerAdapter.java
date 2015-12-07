package com.berniesanders.fieldthebern.adapters;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.berniesanders.fieldthebern.R;

public class CustomPagerAdapter extends PagerAdapter {

    private Context context;

    public CustomPagerAdapter(Context context) {
        this.context = context;
    }

    @Override
    public Object instantiateItem(ViewGroup collection, int position) {

        LayoutInflater inflater = LayoutInflater.from(context);
        ViewGroup layout = null;

        switch (position) {
            case 0:
                layout = (ViewGroup) inflater.inflate(R.layout.app_intro_page_1, collection, false);
                break;
            case 1:
                layout = (ViewGroup) inflater.inflate(R.layout.app_intro_page_2, collection, false);
                break;
            case 2:
                layout = (ViewGroup) inflater.inflate(R.layout.app_intro_page_3, collection, false);
                break;
        }

        collection.addView(layout);
        return layout;
    }

    @Override
    public void destroyItem(ViewGroup collection, int position, Object view) {
        collection.removeView((View) view);
    }

    @Override
    public int getCount() {
        return 3;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return "";
    }

}