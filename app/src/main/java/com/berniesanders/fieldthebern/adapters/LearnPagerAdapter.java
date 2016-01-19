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

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.berniesanders.fieldthebern.R;

public class LearnPagerAdapter extends PagerAdapter {

    private Context context;

    public LearnPagerAdapter(Context context) {
        this.context = context;
    }

    @Override
    public Object instantiateItem(ViewGroup viewGroup, int position) {

        LayoutInflater inflater = LayoutInflater.from(context);
        ViewGroup layout = null;

        switch (position) {
            case 0:
                layout = (ViewGroup) inflater.inflate(R.layout.learn_page_1, viewGroup, false);
                break;
            case 1:
                layout = (ViewGroup) inflater.inflate(R.layout.learn_page_2, viewGroup, false);
                break;
            case 2:
                layout = (ViewGroup) inflater.inflate(R.layout.learn_page_3, viewGroup, false);
                break;
            case 3:
                layout = (ViewGroup) inflater.inflate(R.layout.learn_page_4, viewGroup, false);
                break;
            case 4:
                layout = (ViewGroup) inflater.inflate(R.layout.learn_page_5, viewGroup, false);
                break;
            case 5:
                layout = (ViewGroup) inflater.inflate(R.layout.learn_page_6, viewGroup, false);
                break;
            case 6:
                layout = (ViewGroup) inflater.inflate(R.layout.learn_page_7, viewGroup, false);
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
        return 7;
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
