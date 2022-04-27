package com.github.makewheels.android.video2022.user;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.github.makewheels.android.video2022.R;
import com.github.makewheels.android.video2022.launch.LoginActivity;

public class UserFragment extends Fragment {
    private TextView tv_phone;
    private Button btn_logout;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_user, container, false);
        tv_phone = view.findViewById(R.id.tv_phone);
        btn_logout = view.findViewById(R.id.btn_logout);
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("user", Context.MODE_PRIVATE);
        String phone = sharedPreferences.getString("phone", null);
        tv_phone.setText(phone);
        btn_logout.setOnClickListener(v -> {
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.clear().apply();
            startActivity(new Intent(getActivity(), LoginActivity.class));
            getActivity().finish();
        });
        return view;
    }

}