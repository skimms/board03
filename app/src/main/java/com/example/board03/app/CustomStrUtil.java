package com.example.board03.app;

/**
 * Created by gkimms on 2014-04-30.
 */
public final class CustomStrUtil {
    public static String padRight(String s, int n) {
        return String.format("%1$-" + n + "s", s);
    }

    public static String padLeft(String s, int n) {
        return String.format("%1$" + n + "s", s);
    }
}
