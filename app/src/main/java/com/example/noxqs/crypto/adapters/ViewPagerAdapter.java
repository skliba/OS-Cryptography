package com.example.noxqs.crypto.adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import java.util.ArrayList;

/**
 * Created by noxqs on 28.11.15..
 */
public class ViewPagerAdapter extends FragmentStatePagerAdapter {

    private ArrayList<Fragment> arrayList;

    public ViewPagerAdapter(FragmentManager fm, ArrayList<Fragment> arrayList) {
        super(fm);
        this.arrayList = arrayList;
    }

    @Override
    public Fragment getItem(int position) {
        return arrayList != null ? arrayList.get(position) : null;
    }

    @Override
    public int getCount() {
        return arrayList != null ? arrayList.size() : 0;
    }
}
