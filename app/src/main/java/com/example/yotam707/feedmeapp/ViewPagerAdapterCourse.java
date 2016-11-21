package com.example.yotam707.feedmeapp;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

/**
 * Created by Adir on 17/11/2016.
 */

class ViewPagerAdapterCourse extends FragmentPagerAdapter {
    public ViewPagerAdapterCourse(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        if (position ==0) return new IngredientFragment();
        else return new StepsFragment();
    }

    @Override
    public int getCount() {
        return 2;
    }
}