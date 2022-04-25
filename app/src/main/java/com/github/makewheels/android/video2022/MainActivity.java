package com.github.makewheels.android.video2022;

import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;

import com.alibaba.fastjson.JSONObject;

public class MainActivity extends AppCompatActivity {

    private String token;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        SharedPreferences sharedPreferences = getSharedPreferences("user", MODE_PRIVATE);
        String token = sharedPreferences.getString("token", null);
        int skip = 0;
        int limit = 0;
        new Thread(() -> {
            JSONObject res = HttpUtils.get("/video/getMyVideoList?skip=" + skip + "&limit=" + limit);
            Log.e("tag", res.toJSONString());
        }).start();

    }
}