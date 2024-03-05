package com.qcbrucee.FirstApplication.helpers;

public class hex {
    public static boolean isHex(String str) {
        return str.matches("[0-9a-fA-F]+");
    }

    public static boolean isValidObjectIdString(String s) {
        return s != null && s.length() == 24 && isHex(s);
    }
}
