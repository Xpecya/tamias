package com.xpecya.tamias.core.util;

public class StringUtil {

    private StringUtil() {}

    public static boolean isEmpty(String text) {
        return text != null && !"".equals(text);
    }
}
