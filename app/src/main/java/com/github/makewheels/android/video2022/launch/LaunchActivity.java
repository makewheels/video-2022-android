package com.github.makewheels.android.video2022.launch;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import com.alibaba.fastjson.JSONObject;
import com.github.makewheels.android.video2022.R;
import com.github.makewheels.android.video2022.main.MainActivity;
import com.github.makewheels.android.video2022.utils.HttpUtils;
import com.github.makewheels.android.video2022.utils.TokenUtil;

public class LaunchActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_launch);

        //关闭暗黑模式
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);

        checkUpdate();
    }

    private void checkUpdate() {
        new Thread(() -> {
            JSONObject res = HttpUtils.get("/app/checkUpdate?platform=android");
            JSONObject data = res.getJSONObject("data");
            //如果版本落后
            if (data.getInteger("versionCode") > 1) {
                //如果不是强制更新，检查登陆状态，继续往下跳转
                if (!data.getBoolean("isForceUpdate")) {
                    checkLogin();
                } else {
                    //如果是强制更新，立即更新
                }
            } else {
                //如果我现在是最新版
                checkLogin();
            }
        }).start();
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
            JSONObject res = HttpUtils.get("/user/getUserByToken?token=" + token);
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