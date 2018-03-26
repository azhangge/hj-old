package com.huajie.util;

import java.security.MessageDigest;

public class SHA1 {

    private static String convertToHex(byte[] data) {
        StringBuffer buf = new StringBuffer();
        for (int i = 0; i < data.length; ++i) {
            int halfbyte = data[i] >>> 4 & 0xF;
            int two_halfs = 0;
            do {
                if ((halfbyte >= 0) && (halfbyte <= 9)) {
                    buf.append((char) (48 + halfbyte));
                } else {
                    buf.append((char) (97 + (halfbyte - 10)));
                }
                halfbyte = data[i] & 0xF;
            } while (two_halfs++ < 1);
        }
        return buf.toString();
    }

    public static String toSHA1Code(String text) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-1");
            byte[] sha1hash = new byte[40];
            md.update(text.getBytes("UTF-8"), 0, text.length());
            sha1hash = md.digest();
            return convertToHex(sha1hash);
        } catch (Exception e) {
            return null;
        }

    }

    public static void main(String args[]) {
        String s1 = "图中显示的是？A人物图画狮子老虎";
        String s2 = "图中显示的是？D人物老虎狮子老虎";
        System.out.println(MD5.bit32(s1));
        System.out.println(MD5.bit32(s2));

    }

}
