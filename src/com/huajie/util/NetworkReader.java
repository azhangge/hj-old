/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.huajie.util;

import java.io.BufferedInputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 *
 * @author Administrator
 */
public class NetworkReader {

    public static String readUrl(String urlt, String charSet) {
        StringBuilder sb = new StringBuilder();
        try {
            URL url = new URL(urlt);
            HttpURLConnection urlCon = (HttpURLConnection) url.openConnection();
            urlCon.setConnectTimeout(10000);
            urlCon.setReadTimeout(10000);
            Reader reader = new InputStreamReader(new BufferedInputStream(urlCon.getInputStream()), charSet);
            int c;
            while ((c = reader.read()) != -1) {
                sb.append((char) c);
            }
            reader.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return sb.toString();

    }

    public static void main(String args[]) {
        String url = "https://mapi.alipay.com/gateway.do?service=notify_verify&partner=2088002054778535&notify_id=RqPnCoPT3K9%2Fvwbh3InR%2BbHz%2B%2FW7xFa7hJc9OKax9VbLw4L44pVF4xIl8DUDHTwh17AQ F";
        String result = NetworkReader.readUrl(url, "utf-8");
        System.out.println(result);
    }

}
