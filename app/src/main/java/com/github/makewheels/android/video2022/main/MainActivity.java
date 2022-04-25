package com.github.makewheels.android.video2022.main;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import com.github.makewheels.android.video2022.R;
import com.github.makewheels.android.video2022.home.HomeFragment;
import com.github.makewheels.android.video2022.upload.UploadFragment;
import com.github.makewheels.android.video2022.user.UserFragment;
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
                .addFragmentAndMenuItem(new UserFragment(), R.id.item_nav_user)
                .build();
    }

}