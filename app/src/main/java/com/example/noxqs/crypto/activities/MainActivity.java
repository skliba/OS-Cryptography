package com.example.noxqs.crypto.activities;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;

import com.example.noxqs.crypto.BaseActivity;
import com.example.noxqs.crypto.R;
import com.example.noxqs.crypto.adapters.ViewPagerAdapter;
import com.example.noxqs.crypto.fragments.AESFragment;
import com.example.noxqs.crypto.fragments.RSAFragment;
import com.example.noxqs.crypto.fragments.SignFragment;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;

public class MainActivity extends BaseActivity implements TabLayout.OnTabSelectedListener {

    @Bind(R.id.viewPager)
    ViewPager viewPager;

    @Bind(R.id.tab_layout)
    TabLayout tabLayout;

    private ArrayList<Fragment> fragments;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        initTabs();
        initFragmentList();

        final PagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager(), fragments);
        viewPager.setAdapter(adapter);
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.setOnTabSelectedListener(this);


    }

    private void initTabs() {
        tabLayout.addTab(tabLayout.newTab().setText("RSA"));
        tabLayout.addTab(tabLayout.newTab().setText("AES"));
        tabLayout.addTab(tabLayout.newTab().setText("Sign/Verify"));
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);
    }

    private void initFragmentList() {
        fragments = new ArrayList<>();

        fragments.add(new RSAFragment());
        fragments.add(new AESFragment());
        fragments.add(new SignFragment());
    }

    @Override
    public void onTabSelected(TabLayout.Tab tab) {
        viewPager.setCurrentItem(tab.getPosition());
    }

    @Override
    public void onTabUnselected(TabLayout.Tab tab) {

    }

    @Override
    public void onTabReselected(TabLayout.Tab tab) {

    }
}

