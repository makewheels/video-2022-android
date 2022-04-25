package com.github.makewheels.android.video2022;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.alibaba.fastjson.JSONObject;
import com.github.makewheels.android.video2022.utils.ToastUtil;
import com.github.makewheels.android.video2022.utils.TokenUtil;
import com.github.makewheels.android.video2022.utils.UserHttpUtils;

public class LoginActivity extends AppCompatActivity {
    private TextView et_phone;
    private Button btn_get_sms_code;
    private TextView et_code;
    private Button btn_submit;

    private String phone;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        init();
        addListeners();
    }

    private void init() {
        et_phone = findViewById(R.id.et_phone);
        btn_get_sms_code = findViewById(R.id.btn_get_sms_code);
        et_code = findViewById(R.id.et_code);
        btn_submit = findViewById(R.id.btn_submit);
    }

    private void addListeners() {
        //请求验证码
        btn_get_sms_code.setOnClickListener(v -> {
            phone = et_phone.getText().toString();
            new Thread(() -> {
                UserHttpUtils.get("/user/requestVerificationCode?phone=" + phone);
                runOnUiThread(() -> ToastUtil.info(LoginActivity.this, "Sms has sent"));
            }).start();
        });
        //提交验证码
        btn_submit.setOnClickListener(v -> {
            String code = et_code.getText().toString();
            new Thread(() -> {
                JSONObject res = UserHttpUtils.get("/user/submitVerificationCode?phone=" + phone + "&code=" + code);
                //校验失败，提示信息
                if (res.getInteger("code") != 0) {
                    runOnUiThread(() -> ToastUtil.error(LoginActivity.this, res.getString("message")));
                } else {
                    //校验通过，保存token，跳转主页
                    runOnUiThread(() -> ToastUtil.success(LoginActivity.this, "Login success!"));
                    SharedPreferences sharedPreferences = getSharedPreferences("user", MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    JSONObject data = res.getJSONObject("data");
                    String token = data.getString("token");
                    editor.putString("userId", data.getString("id"));
                    editor.putString("phone", data.getString("phone"));
                    editor.putString("token", token);
                    editor.apply();
                    TokenUtil.set(token);
                    startActivity(new Intent(LoginActivity.this, MainActivity.class));
                    finish();
                }
            }).start();
        });
    }

}