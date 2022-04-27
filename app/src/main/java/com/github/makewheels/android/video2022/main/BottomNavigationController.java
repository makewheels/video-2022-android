package com.github.makewheels.android.video2022.main;

import android.view.MenuItem;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;
import java.util.List;

public class BottomNavigationController {
    private static class Holder {
        private static final BottomNavigationController INSTANCE = new BottomNavigationController();
    }

    private BottomNavigationView bottomNavigationView;
    private MenuItem menuItem;
    private List<Integer> menuItemIdList;

    private ViewPager viewPager;
    private ViewPagerAdapter viewPagerAdapter;
    private List<Fragment> fragmentList;

    private AppCompatActivity activity;

    private BottomNavigationController() {
    }

    private void init() {
        //设置bottomNavigationView的事件监听
        bottomNavigationView.setOnItemSelectedListener(item -> {
            menuItem = item;
            int itemId = item.getItemId();
            for (int i = 0; i < menuItemIdList.size(); i++) {
                if (itemId == menuItemIdList.get(i)) {
                    viewPager.setCurrentItem(i, false);
                    return true;
                }
            }
            return false;
        });

        //设置viewPager的事件监听
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                if (menuItem != null) {
                    menuItem.setChecked(false);
                } else {
                    bottomNavigationView.getMenu().getItem(0).setChecked(false);
                }
                menuItem = bottomNavigationView.getMenu().getItem(position);
                menuItem.setChecked(true);
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });
        viewPagerAdapter.setList(fragmentList);
        viewPager.setAdapter(viewPagerAdapter);
    }

    public static BottomNavigationController getInstance() {
        return Holder.INSTANCE;
    }

    public static class Builder {

        private final BottomNavigationController INSTANCE;

        public Builder() {
            INSTANCE = Holder.INSTANCE;
            INSTANCE.menuItemIdList = new ArrayList<>();
            INSTANCE.fragmentList = new ArrayList<>();
        }

        public Builder setActivity(AppCompatActivity activity) {
            INSTANCE.activity = activity;
            INSTANCE.viewPagerAdapter = new ViewPagerAdapter(activity.getSupportFragmentManager());
            return this;
        }

        public Builder setBottomNavigationView(int bottomNavigationViewId) {
            INSTANCE.bottomNavigationView = INSTANCE.activity.findViewById(bottomNavigationViewId);
            return this;
        }

        public Builder setViewPager(int viewPagerId) {
            INSTANCE.viewPager = INSTANCE.activity.findViewById(viewPagerId);
            return this;
        }

        public Builder addFragmentAndMenuItem(Fragment fragment, Integer menuItemId) {
            INSTANCE.fragmentList.add(fragment);
            INSTANCE.menuItemIdList.add(menuItemId);
            return this;
        }

        public BottomNavigationController build() {
            INSTANCE.init();
            return INSTANCE;
        }
    }
}
