package com.github.makewheels.android.video2022.upload;

import android.annotation.SuppressLint;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.database.Cursor;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.provider.OpenableColumns;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.sdk.android.oss.ClientException;
import com.alibaba.sdk.android.oss.OSS;
import com.alibaba.sdk.android.oss.OSSClient;
import com.alibaba.sdk.android.oss.ServiceException;
import com.alibaba.sdk.android.oss.common.auth.OSSCredentialProvider;
import com.alibaba.sdk.android.oss.common.auth.OSSStsTokenCredentialProvider;
import com.alibaba.sdk.android.oss.model.CompleteMultipartUploadResult;
import com.alibaba.sdk.android.oss.model.MultipartUploadRequest;
import com.github.makewheels.android.video2022.R;
import com.github.makewheels.android.video2022.utils.HttpUtils;
import com.github.makewheels.android.video2022.utils.ToastUtil;

import cn.hutool.core.io.FileUtil;


public class ProgressActivity extends AppCompatActivity {
    private TextView tv_fileSize;
    private ProgressBar progressBar;
    private TextView tv_percent;
    private TextView tv_shortUrl;
    private Button btn_copy_shortUrl;
    private EditText et_title;
    private EditText et_description;
    private Button btn_updateVideoInfo;

    private Uri uri;
    private OSS oss;

    private String videoId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_progress);
        init();
        addListeners();
        new Thread(this::createVideo).start();
    }

    private void init() {
        tv_fileSize = findViewById(R.id.tv_fileSize);
        progressBar = findViewById(R.id.progressBar);
        tv_percent = findViewById(R.id.tv_percent);
        tv_shortUrl = findViewById(R.id.tv_shortUrl);
        btn_copy_shortUrl = findViewById(R.id.btn_copy_shortUrl);
        et_title = findViewById(R.id.et_title);
        et_description = findViewById(R.id.et_description);
        btn_updateVideoInfo = findViewById(R.id.btn_updateVideoInfo);
    }

    private void addListeners() {
        //复制分享url按钮
        btn_copy_shortUrl.setOnClickListener(v -> {
            String shortUrl = tv_shortUrl.getText().toString();
            String title = et_title.getText().toString();
            ClipboardManager clipboardManager = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
            String copy = "【" + title + "】\n" + shortUrl;
            ClipData clipData = ClipData.newPlainText("btn_copy_shortUrl", copy);
            clipboardManager.setPrimaryClip(clipData);
            ToastUtil.success(ProgressActivity.this, "Copied!");
        });

        //修改视频信息按钮
        btn_updateVideoInfo.setOnClickListener(v -> {
            if (videoId == null) return;
            JSONObject request = new JSONObject();
            request.put("id", videoId);
            request.put("title", et_title.getText().toString());
            request.put("description", et_description.getText().toString());
            new Thread(() -> {
                JSONObject response = HttpUtils.post("/video/updateInfo", request.toJSONString());
                if (response.getInteger("code") == 0) {
                    runOnUiThread(() -> ToastUtil.success(ProgressActivity.this, "Updated"));
                } else {
                    runOnUiThread(() -> ToastUtil.error(ProgressActivity.this, response.getString("message")));
                }
            }).start();
        });
    }

    @SuppressLint("Range")
    public String getFilename(Uri uri) {
        String result = null;
        if (uri.getScheme().equals("content")) {
            try (Cursor cursor = getContentResolver().query(uri, null,
                    null, null, null)) {
                if (cursor != null && cursor.moveToFirst()) {
                    result = cursor.getString(cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME));
                }
            }
        }
        if (result == null) {
            result = uri.getPath();
            int cut = result.lastIndexOf('/');
            if (cut != -1) {
                result = result.substring(cut + 1);
            }
        }
        return result;
    }

    @SuppressLint("Range")
    public long getFileSize(Uri uri) {
        long result = 0;
        if (uri.getScheme().equals("content")) {
            try (Cursor cursor = getContentResolver().query(uri, null,
                    null, null, null)) {
                if (cursor != null && cursor.moveToFirst()) {
                    result = cursor.getLong(cursor.getColumnIndex(OpenableColumns.SIZE));
                }
            }
        }
        return result;
    }

    private void createVideo() {
        uri = getIntent().getExtras().getParcelable("uri");
        String filename = getFilename(uri);
        long size = getFileSize(uri);
        runOnUiThread(() -> tv_fileSize.setText(FileUtil.readableFileSize(size)));

        //创建视频
        JSONObject createRequest = new JSONObject();
        createRequest.put("videoType", "USER_UPLOAD");
        createRequest.put("rawFilename", filename);
        createRequest.put("size", size);
        JSONObject createResponse = HttpUtils.post("/video/create", createRequest.toJSONString());
        JSONObject createResponseData = createResponse.getJSONObject("data");
        videoId = createResponseData.getString("videoId");
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
        OSSCredentialProvider credentialProvider = new OSSStsTokenCredentialProvider(
                credentials.getString("accessKeyId"),
                credentials.getString("secretKey"),
                credentials.getString("sessionToken")
        );
        oss = new OSSClient(getApplicationContext(), credentials.getString("endpoint"), credentialProvider);

        //开始分片上传
        MultipartUploadRequest<MultipartUploadRequest<?>> uploadRequest = new MultipartUploadRequest<>(
                credentials.getString("bucket"), credentials.getString("key"), uri);
        uploadRequest.setPartSize(1024 * 1024);
        uploadRequest.setProgressCallback((request, currentSize, totalSize) -> {
            double percent = currentSize * 1.0 / totalSize * 100;
            runOnUiThread(() -> {
                tv_fileSize.setText(FileUtil.readableFileSize(currentSize) + " / " + FileUtil.readableFileSize(totalSize));
                progressBar.setProgress((int) percent);
                tv_percent.setText(String.format("%.2f", percent) + "%");
            });
        });

        try {
            CompleteMultipartUploadResult result = oss.multipartUpload(uploadRequest);
        } catch (ClientException | ServiceException e) {
            e.printStackTrace();
        }

        //通知文件上传完成
        HttpUtils.get("/file/uploadFinish?fileId=" + fileId);
        //通知视频源文件上传完成
        HttpUtils.get("/video/rawFileUploadFinish?videoId=" + videoId);
        runOnUiThread(() -> ToastUtil.success(this, "Upload finished!"));
    }
}