package com.github.makewheels.android.video2022.utils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

import cn.hutool.http.HttpUtil;

public class UserHttpUtils {
    public static JSONObject get(String url) {
        String res = HttpUtil.get(HttpUtils.getUserMicroServiceBaseUrl() + "/user-micro-service-2022" + url);
        return JSON.parseObject(res);
    }

}
