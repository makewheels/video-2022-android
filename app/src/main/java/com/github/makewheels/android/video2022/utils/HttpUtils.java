package com.github.makewheels.android.video2022.utils;

import android.util.Log;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import cn.hutool.http.HttpUtil;

public class HttpUtils {
    private static String baseUrl;
    private static Boolean isDevEnv;

    public static boolean isDevEnv() {
        if (isDevEnv != null) return isDevEnv;
        isDevEnv = System.getProperty("os.arch").equals("i686");
        return isDevEnv;
    }

    public static String getBaseUrl() {
        if (baseUrl == null) {
            if (isDevEnv()) {
                baseUrl = "http://192.168.1.3:5022";
            } else {
                baseUrl = "https://oneclick.video";
            }
        }
        return baseUrl;
    }

    public static JSONObject get(String url) {
        HttpRequest request = HttpUtil.createGet(getBaseUrl() + url);
        request.timeout(10 * 1000);
        request.header("token", TokenUtil.get());
        String response = request.execute().body();
        Log.e("tag", response);
        return JSON.parseObject(response);
    }

    public static JSONObject post(String url, String body) {
        HttpRequest request = HttpUtil.createPost(getBaseUrl() + url);
        request.timeout(10 * 1000);
        request.header("token", TokenUtil.get());
        request.body(body);
        String response = request.execute().body();
        Log.e("tag", response);
        return JSON.parseObject(response);
    }
}
