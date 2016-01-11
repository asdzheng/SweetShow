package com.asdzheng.sweetshow.ui.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.asdzheng.sweetshow.ui.fragment.DiscoverFragment;
import com.asdzheng.sweetshow.ui.fragment.HomeFragment;
import com.asdzheng.sweetshow.ui.fragment.HotFragment;

/**
 * Created by Administrator on 2016-1-11.
 */
public class HomeFragmentAdapter extends FragmentPagerAdapter {


    public HomeFragmentAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0 :
                return new HomeFragment();
            case 1 :
                return new DiscoverFragment();
            case 2:
                return new HotFragment();
        }

        return null;
    }

    @Override
    public int getCount() {
        return 3;
    }
}
