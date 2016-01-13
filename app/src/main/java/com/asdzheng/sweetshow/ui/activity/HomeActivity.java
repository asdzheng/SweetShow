package com.asdzheng.sweetshow.ui.activity;

import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.Toast;

import com.asdzheng.sweetshow.R;
import com.asdzheng.sweetshow.ui.adapter.HomeFragmentAdapter;

/**
 * Created by Administrator on 2016-1-11.
 */
public class HomeActivity extends BaseActivity implements View.OnClickListener {

    Toolbar homeToolbar;
    ViewPager vpHome;
    FragmentPagerAdapter fragmentPagerAdapter;

    ImageView ivHome, ivDiscover, ivHot;


    @Override
    protected int setLayout() {
        return R.layout.activity_home;
    }

    @Override
    protected void initViews() {
        // 经测试在代码里直接声明透明状态栏更有效
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            WindowManager.LayoutParams localLayoutParams = getWindow().getAttributes();
            localLayoutParams.flags = (WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS | localLayoutParams.flags);
        }

        homeToolbar = (Toolbar) findViewById(R.id.toolbar);
        ivHome = (ImageView) findViewById(R.id.iv_toolbar_home);
        ivDiscover = (ImageView) findViewById(R.id.iv_toolbar_discover);
        ivHot = (ImageView) findViewById(R.id.iv_toolbar_hot);

        setSupportActionBar(homeToolbar);

        vpHome = (ViewPager) findViewById(R.id.vp_home);
    }

    @Override
    protected void initData(Bundle savedInstanceState) {
        fragmentPagerAdapter = new HomeFragmentAdapter(getSupportFragmentManager());
        vpHome.setAdapter(fragmentPagerAdapter);
        vpHome.setOffscreenPageLimit(2);
        vpHome.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                if (position == 0) {
                    selectFragment(R.drawable.home_selected, R.drawable.discover_night, R.drawable.profile_night);

                } else if (position == 1) {
                    selectFragment(R.drawable.home_night, R.drawable.discover_selected, R.drawable.profile_night);

                } else {
                    selectFragment(R.drawable.home_night, R.drawable.discover_night, R.drawable.profile_selected);
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        homeToolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                Toast.makeText(HomeActivity.this, "setting", Toast.LENGTH_SHORT).show();
                return false;
            }
        });

        ivDiscover.setOnClickListener(this);
        ivHome.setOnClickListener(this);
        ivHot.setOnClickListener(this);
    }

    private void selectFragment(int home_night, int discover_night, int profile_selected) {
        ivHome.setImageResource(home_night);
        ivDiscover.setImageResource(discover_night);
        ivHot.setImageResource(profile_selected);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.menu_home, menu);
        return true;
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if(id == R.id.iv_toolbar_home) {
            selectFragment(R.drawable.home_selected, R.drawable.discover_night, R.drawable.profile_night);
            vpHome.setCurrentItem(0, true);
        } else if(id == R.id.iv_toolbar_discover) {
            selectFragment(R.drawable.home_night, R.drawable.discover_selected, R.drawable.profile_night);
            vpHome.setCurrentItem(1, true);
        } else if(id == R.id.iv_toolbar_hot) {
            selectFragment(R.drawable.home_night, R.drawable.discover_night, R.drawable.profile_selected);
            vpHome.setCurrentItem(2, true);

        }
    }
}
