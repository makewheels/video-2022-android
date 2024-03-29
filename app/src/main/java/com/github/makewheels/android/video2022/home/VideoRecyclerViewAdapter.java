package com.github.makewheels.android.video2022.home;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.alibaba.fastjson.JSONObject;
import com.bumptech.glide.Glide;
import com.github.makewheels.android.video2022.R;

import java.util.List;

import cn.hutool.core.date.DateUtil;

public class VideoRecyclerViewAdapter extends RecyclerView.Adapter<VideoRecyclerViewAdapter.ViewHolder> {
    private List<JSONObject> data;
    private LayoutInflater inflater;

    public VideoRecyclerViewAdapter(Context context, List<JSONObject> data) {
        this.inflater = LayoutInflater.from(context);
        this.data = data;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.item_video, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        JSONObject video = data.get(position);
        String coverUrl = video.getString("coverUrl");
        Glide.with(inflater.getContext()).load(coverUrl).into(holder.iv_cover);
        String title = video.getString("title");
        if (title == null || title.equals("")) {
            title = "No title";
            holder.tv_title.setTextColor(Color.GRAY);
        } else {
            holder.tv_title.setTextColor(Color.BLACK);
        }
        holder.tv_title.setText(title);
        int minute;
        int second;
        long duration = video.getLongValue("duration");
        duration /= 1000;
        minute = (int) (duration / 60);
        second = (int) (duration % 60);
        holder.tv_duration.setText(minute + ":" + second);
        holder.tv_watchCount.setText("views: " + video.getIntValue("watchCount") + "");
        holder.tv_createTime.setText(DateUtil.formatDateTime(video.getDate("createTime")));
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView iv_cover;
        TextView tv_title;
        TextView tv_duration;
        TextView tv_watchCount;
        TextView tv_createTime;

        ViewHolder(View itemView) {
            super(itemView);
            iv_cover = itemView.findViewById(R.id.iv_cover);
            tv_title = itemView.findViewById(R.id.tv_title);
            tv_duration = itemView.findViewById(R.id.tv_duration);
            tv_watchCount = itemView.findViewById(R.id.tv_watchCount);
            tv_createTime = itemView.findViewById(R.id.tv_createTime);
        }

    }

}
