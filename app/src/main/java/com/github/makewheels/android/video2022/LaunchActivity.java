package com.github.makewheels.android.video2022;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.alibaba.fastjson.JSONObject;

import cn.hutool.core.thread.ThreadUtil;
import cn.hutool.http.HttpUtil;

public class LaunchActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_launch);
        checkLogin();
    }

    /**
     * 检查登陆状态，如果token校验成功返回true，失败返回false
     */
    private void checkLogin() {
        SharedPreferences sharedPreferences = getSharedPreferences("user", MODE_PRIVATE);
        String token = sharedPreferences.getString("token", null);
        if (token == null) {
            jumpToLogin();
            return;
        }
        new Thread(() -> {
            JSONObject res = UserHttpUtils.get("/user/getUserByToken?token=" + token);
            //如果校验未通过，跳转登录页
            if (res.getInteger("code") == 1001) {
                jumpToLogin();
            } else {
                //校验通过，跳转主页
                TokenUtil.set(token);
                jumpToMain();
            }
        }).start();
    }

    private void jumpToLogin() {
        startActivity(new Intent(this, LoginActivity.class));
        finish();
    }

    private void jumpToMain() {
        startActivity(new Intent(this, MainActivity.class));
        finish();
    }
}