package com.github.makewheels.android.video2022.upload;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.graphics.Color;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.alibaba.fastjson.JSONObject;
import com.github.makewheels.android.video2022.R;
import com.github.makewheels.android.video2022.utils.HttpUtils;
import com.github.makewheels.android.video2022.utils.ToastUtil;

public class TransferActivity extends AppCompatActivity {

    private EditText et_url;
    private Button btn_submit;
    private TextView tv_shortUrl;
    private Button btn_copy_shortUrl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transfer);
        init();
        addListener();
    }

    private void init() {
        et_url = findViewById(R.id.et_url);
        btn_submit = findViewById(R.id.btn_submit);
        tv_shortUrl = findViewById(R.id.tv_shortUrl);
        btn_copy_shortUrl = findViewById(R.id.btn_copy_shortUrl);
    }

    private void addListener() {
        btn_submit.setOnClickListener(v -> {
            String youtubeUrl = et_url.getText().toString();
            if (youtubeUrl.equals("")
                    || (!youtubeUrl.startsWith("https://www.youtube.com/")
                    && !youtubeUrl.startsWith("https://youtu.be/"))
            ) {
                ToastUtil.error(TransferActivity.this, "Please enter correct url!");
                return;
            }
            new Thread(() -> {
                JSONObject request = new JSONObject();
                request.put("type", "YOUTUBE");
                request.put("youtubeUrl", youtubeUrl);
                JSONObject response = HttpUtils.post("/video/create", request.toJSONString());
                JSONObject data = response.getJSONObject("data");
                String shortUrl = data.getString("shortUrl");
                runOnUiThread(() -> {
                    tv_shortUrl.setText(shortUrl);
                    tv_shortUrl.setTextColor(Color.BLACK);
                });
            }).start();
        });

        btn_copy_shortUrl.setOnClickListener(v -> {
            String shortUrl = tv_shortUrl.getText().toString();
            ClipboardManager clipboardManager = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
            ClipData clipData = ClipData.newPlainText("shareUrl", shortUrl);
            clipboardManager.setPrimaryClip(clipData);
            ToastUtil.success(TransferActivity.this,"Copied!");
        });
    }

}