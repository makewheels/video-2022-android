package com.github.makewheels.android.video2022;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;

import cn.hutool.http.HttpUtil;

public class LaunchActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_launch);
        checkLogin();

    }

    @Override
    protected void onResume() {
        super.onResume();
        checkLogin();
    }

    /**
     * 检查登陆状态，如果token校验成功返回true，失败返回false
     */
    private boolean checkLogin() {
        SharedPreferences sharedPreferences = getSharedPreferences("user", MODE_PRIVATE);
        String token = sharedPreferences.getString("token", null);
//        if (token == null) {
//            return false;
//        }
        new Thread() {
            @Override
            public void run() {
                String s = HttpUtil.get("https://www.baidu.com");
                Log.e("tag", s);
            }
        }.start();

        return true;
    }

}