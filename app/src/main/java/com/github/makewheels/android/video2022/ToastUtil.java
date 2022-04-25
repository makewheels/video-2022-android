package com.github.makewheels.android.video2022;

import android.content.Context;
import android.widget.Toast;

import es.dmoral.toasty.Toasty;

public class ToastUtil {
    public static void info(Context context, String message) {
        Toasty.info(context, message, Toast.LENGTH_SHORT, true).show();
    }

    public static void success(Context context, String message) {
        Toasty.success(context, message, Toast.LENGTH_SHORT, true).show();
    }

    public static void warning(Context context, String message) {
        Toasty.warning(context, message, Toast.LENGTH_SHORT, true).show();
    }

    public static void error(Context context, String message) {
        Toasty.error(context, message, Toast.LENGTH_SHORT, true).show();
    }
}
