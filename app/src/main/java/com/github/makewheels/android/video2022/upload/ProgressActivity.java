package com.github.makewheels.android.video2022.upload;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.alibaba.fastjson.JSONObject;
import com.github.makewheels.android.video2022.R;
import com.github.makewheels.android.video2022.utils.HttpUtils;
import com.github.makewheels.android.video2022.utils.ToastUtil;


public class ProgressActivity extends AppCompatActivity {
    private TextView tv_videoSize;
    private ProgressBar progressBar;
    private TextView tv_percent;
    private TextView tv_shortUrl;
    private Button btn_copy_shortUrl;

    private Uri uri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_progress);
        init();
        addListeners();
        new Thread(this::createVideo).start();
    }

    private void init() {
        tv_videoSize = findViewById(R.id.tv_item_name);
        progressBar = findViewById(R.id.progressBar);
        tv_percent = findViewById(R.id.tv_percent);
        tv_shortUrl = findViewById(R.id.tv_shortUrl);
        btn_copy_shortUrl = findViewById(R.id.btn_copy_shortUrl);
    }

    private void addListeners() {
        btn_copy_shortUrl.setOnClickListener(v -> {
            String shortUrl = tv_shortUrl.getText().toString();
            ClipboardManager clipboardManager = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
            ClipData clipData = ClipData.newPlainText("shareUrl", shortUrl);
            clipboardManager.setPrimaryClip(clipData);
            ToastUtil.success(ProgressActivity.this, "Copied!");
        });
    }

    private void createVideo() {
        uri = getIntent().getExtras().getParcelable("uri");
        //创建视频
        JSONObject createRequest = new JSONObject();
        createRequest.put("type", "USER_UPLOAD");
        createRequest.put("originalFilename", "1.mp4");
        JSONObject createResponse = HttpUtils.post("/video/create", createRequest.toJSONString());
        JSONObject createResponseData = createResponse.getJSONObject("data");
        String shortUrl = createResponseData.getString("shortUrl");
        runOnUiThread(() -> {
            tv_shortUrl.setText(shortUrl);
            tv_shortUrl.setTextColor(Color.BLACK);
        });

        //获取上传凭证
        String fileId = createResponseData.getString("fileId");
        String videoId = createResponseData.getString("videoId");
        JSONObject getUploadCredentialsResponse = HttpUtils.get("/file/getUploadCredentials?fileId=" + fileId);

        //上传文件
        JSONObject credentials = getUploadCredentialsResponse.getJSONObject("data");

        //通知文件上传完成
        HttpUtils.get("/file/uploadFinish?fileId=" + fileId);
        //通知视频源文件上传完成
        HttpUtils.get("/video/originalFileUploadFinish?videoId=" + videoId);
    }
}