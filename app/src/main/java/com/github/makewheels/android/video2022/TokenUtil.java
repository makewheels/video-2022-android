package com.github.makewheels.android.video2022;

public class TokenUtil {
    private static String token;

    public static String get() {
        return token;
    }

    public static String set(String token) {
        return TokenUtil.token = token;
    }
}
