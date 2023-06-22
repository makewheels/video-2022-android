package com.github.makewheels.android.video2022.upload;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.fragment.app.Fragment;

import com.github.makewheels.android.video2022.R;

public class UploadFragment extends Fragment {
    private Button btn_upload_from_gallery;
    private Button btn_transfer;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //关闭暗黑模式
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_upload, container, false);
        btn_upload_from_gallery = view.findViewById(R.id.btn_upload_from_gallery);
        btn_transfer = view.findViewById(R.id.btn_transfer);
        addListeners();
        return view;
    }

    private void addListeners() {
        ActivityResultLauncher<String> mGetContent = registerForActivityResult(
                new ActivityResultContracts.GetContent(),
                uri -> {
                    if (uri == null) {
                        return;
                    }
                    Intent intent = new Intent(getActivity(), ProgressActivity.class);
                    intent.putExtra("uri", uri);
                    startActivity(intent);
                });
        btn_upload_from_gallery.setOnClickListener(v -> mGetContent.launch("video/*"));

        btn_transfer.setOnClickListener(v -> startActivity(new Intent(getActivity(), TransferActivity.class)));
    }

}