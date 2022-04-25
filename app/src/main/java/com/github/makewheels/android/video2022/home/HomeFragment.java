package com.github.makewheels.android.video2022.home;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.alibaba.fastjson.JSONObject;
import com.github.makewheels.android.video2022.R;
import com.github.makewheels.android.video2022.utils.HttpUtils;

public class HomeFragment extends Fragment {
    public HomeFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    private void getVideos() {
        int skip = 0;
        int limit = 0;
        new Thread(() -> {
            JSONObject res = HttpUtils.get("/video/getMyVideoList?skip=" + skip + "&limit=" + limit);
        }).start();
    }
}