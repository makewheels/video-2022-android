package com.github.makewheels.android.video2022.home;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.alibaba.fastjson.JSONObject;
import com.github.makewheels.android.video2022.R;
import com.github.makewheels.android.video2022.utils.HttpUtils;

import java.util.List;

public class HomeFragment extends Fragment {
    private RecyclerView rv_video_list;
    private VideoRecyclerViewAdapter adapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        loadVideoList();
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        rv_video_list = view.findViewById(R.id.rv_video_list);
        return view;
    }

    private void loadVideoList() {
        int skip = 0;
        int limit = 0;
        new Thread(() -> {
            JSONObject res = HttpUtils.get("/video/getMyVideoList?skip=" + skip + "&limit=" + limit);
            List<JSONObject> videos = res.getJSONArray("data").toJavaList(JSONObject.class);

            getActivity().runOnUiThread(() -> {

                rv_video_list.setLayoutManager(new LinearLayoutManager(getActivity()));
                adapter = new VideoRecyclerViewAdapter(getActivity(), videos);
//                adapter.setClickListener((view, position) -> {
//                    Toast.makeText(getActivity(),
//                            "You clicked " + position + " on row number " + position,
//                            Toast.LENGTH_SHORT).show();
//                });
              //  Log.e("tag", res.toJSONString());

                rv_video_list.setAdapter(adapter);
            });
        }).start();
    }

}