package com.example.yotam707.feedmeapp;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

/**
 * Created by yotam707 on 9/10/2016.
 */
public class ViewPagerAdapter extends FragmentPagerAdapter {
    public ViewPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        if (position ==0) return new TopFragment();
        else return new CategoriesFragment();
    }

    @Override
    public int getCount() {
        return 2;
    }
}
