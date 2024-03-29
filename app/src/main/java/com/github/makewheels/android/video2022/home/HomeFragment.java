package com.github.makewheels.android.video2022.home;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.alibaba.fastjson.JSONObject;
import com.github.makewheels.android.video2022.R;
import com.github.makewheels.android.video2022.utils.HttpUtils;

import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends Fragment {
    private RecyclerView rv_video_list;
    private VideoRecyclerViewAdapter adapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        loadVideoList();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        rv_video_list = view.findViewById(R.id.rv_video_list);
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    private void loadVideoList() {
        int skip = 0;
        int limit = 100;
        new Thread(() -> {
            List<JSONObject> videos = new ArrayList<>();
            JSONObject res = HttpUtils.get("/video/getMyVideoList?skip=" + skip + "&limit=" + limit);
            if (res != null) {
                videos = res.getJSONArray("data").toJavaList(JSONObject.class);
            }

            List<JSONObject> finalVideos = videos;
            getActivity().runOnUiThread(() -> {
                rv_video_list.setLayoutManager(new LinearLayoutManager(getActivity()));
                rv_video_list.addItemDecoration(new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL));
                adapter = new VideoRecyclerViewAdapter(getActivity(), finalVideos);
                if (res != null) {
                    Log.e("tag", res.toJSONString());
                }
                rv_video_list.setAdapter(adapter);
            });
        }).start();
    }

}