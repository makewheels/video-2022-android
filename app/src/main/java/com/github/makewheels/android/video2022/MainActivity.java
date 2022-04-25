package com.github.makewheels.android.video2022;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import com.alibaba.fastjson.JSONObject;
import com.github.makewheels.android.video2022.utils.HttpUtils;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {
    private ViewPager view_pager;
    private BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        init();
        addListeners();
    }

    private void init() {
        view_pager = findViewById(R.id.view_pager);
        bottomNavigationView = findViewById(R.id.bottom_navigation);
    }

    private void addListeners() {
        new BottomNavigationController.Builder()
                .setActivity(this)
                .setBottomNavigationView(R.id.bottom_navigation)
                .setViewPager(R.id.view_pager)
                .addFragmentAndMenuItem(new HomeFragment(), R.id.item_nav_home)
                .addFragmentAndMenuItem(new UploadFragment(), R.id.item_nav_upload)
                .build();
    }

    private void getVideos() {
        int skip = 0;
        int limit = 0;
        new Thread(() -> {
            JSONObject res = HttpUtils.get("/video/getMyVideoList?skip=" + skip + "&limit=" + limit);
        }).start();
    }
}