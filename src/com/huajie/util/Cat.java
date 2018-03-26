/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.huajie.util;

import java.util.UUID;

/**
 *
 * @author huajie
 */
public class Cat {

    private static String sysId = null;

    /**
     * 获得代表当前应用的运行时唯一ID
     * @return 
     */
    public static String getSysId() {
        if (sysId == null) {
            sysId = UUID.randomUUID().toString();
        }
        return sysId;
    }

    public static void paw(Exception e) {
        e.printStackTrace();
    }

    public static String textToHtml(String str) {
        str = str.replaceAll("\r\n", "<br/>");
        str = str.replaceAll(" ", "&nbsp;");
        return str;
    }

    public static <T> void println(T something) {
        System.out.println(something);
    }

    public static String getUniqueId() {
        return UUID.randomUUID().toString();
    }

    public static void sendEmail(String toEmail,
            String title, String content) {
        try {
            boolean ifs = SendEmail.sendEmail("smtp",
                    "smtp.ym.163.com", "25", false,
                    false,
                    "reporter@huajie.com", "122128148", "reporter@huajie.com",
                    "reporter@huajie.com", toEmail, title,
                    content);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
