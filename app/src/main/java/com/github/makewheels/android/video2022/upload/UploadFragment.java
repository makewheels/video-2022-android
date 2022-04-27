package com.github.makewheels.android.video2022.upload;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.fragment.app.Fragment;

import com.github.makewheels.android.video2022.R;

public class UploadFragment extends Fragment {
    private Button btn_upload_from_gallery;
    private Button btn_transfer;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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
        btn_upload_from_gallery.setOnClickListener(v -> {

        });
        btn_transfer.setOnClickListener(v -> startActivity(new Intent(getActivity(), TransferActivity.class)));
    }

}