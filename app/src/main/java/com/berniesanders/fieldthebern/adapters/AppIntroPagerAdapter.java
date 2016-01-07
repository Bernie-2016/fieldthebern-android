package com.berniesanders.fieldthebern.adapters;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.berniesanders.fieldthebern.R;

public class AppIntroPagerAdapter extends PagerAdapter {

    private Context context;

    public AppIntroPagerAdapter(Context context) {
        this.context = context;
    }

    @Override
    public Object instantiateItem(ViewGroup viewGroup, int position) {

        LayoutInflater inflater = LayoutInflater.from(context);
        ViewGroup layout = null;

        switch (position) {
            case 0:
                layout = (ViewGroup) inflater.inflate(R.layout.app_intro_page_1, viewGroup, false);
                break;
            case 1:
                layout = (ViewGroup) inflater.inflate(R.layout.app_intro_page_2, viewGroup, false);
                break;
            case 2:
                layout = (ViewGroup) inflater.inflate(R.layout.app_intro_page_3, viewGroup, false);
                break;
            case 3:
                layout = (ViewGroup) inflater.inflate(R.layout.app_intro_page_4, viewGroup, false);
                break;
        }

        viewGroup.addView(layout);
        return layout;
    }

    @Override
    public void destroyItem(ViewGroup viewGroup, int position, Object view) {
        viewGroup.removeView((View) view);
    }

    @Override
    public int getCount() {
        return 4;
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
