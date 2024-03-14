package com.anki.cloneword.util;

public class StringUtils {

    private StringUtils() {
    }

    public static boolean isEmpty(String s) {
        return s == null || s.isEmpty();
    }

    public static String trim(String s) {
        return s != null ? s.trim() : "";
    }
}
