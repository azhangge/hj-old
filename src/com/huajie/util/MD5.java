package com.huajie.util;

import java.security.MessageDigest;

public class MD5 {

    private static final char HEX_DIGITS[] = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F'};

    private static String toHexString(byte[] b) {
        StringBuilder sb = new StringBuilder(b.length * 2);
        for (int i = 0; i < b.length; i++) {
            sb.append(HEX_DIGITS[(b[i] & 0xf0) >>> 4]);
            sb.append(HEX_DIGITS[b[i] & 0x0f]);
        }
        return sb.toString();
    }

    public static String bit32(String SourceString) {
        try {
            MessageDigest digest = java.security.MessageDigest.getInstance("MD5");
            digest.update(SourceString.getBytes());
            byte messageDigest[] = digest.digest();
            return toHexString(messageDigest);
        } catch (Exception e) {
            return null;
        }

    }

    public static String bit16(String SourceString) {
        try {
            return bit32(SourceString).substring(8, 24);
        } catch (Exception e) {
            return null;
        }

    }

    public static void main(String[] args) {
        String s1 = "";
        String s2 = "";
        System.out.println(MD5.bit32("litie."));
    }
}
