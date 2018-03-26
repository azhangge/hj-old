package com.huajie.util;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * 本类用于将一个文本文件中键值对应的内容加载为Map 区别于Properties类的是本类适用于任意格式编码
 *
 * @author huajie.com
 */
public class I18nHelper {

    /**
     *
     * @param filePath 要将其内容加载为MAP的文件的位置
     * @return 将文件内容返回为一个Map对象
     */
    public static Map loadAsMap(String filePath) {
        Map map = new HashMap();
        InputStream is = null;
        BufferedReader br = null;
        try {
            String code = "UTF-8";
            is = new BufferedInputStream(new FileInputStream(filePath));
            String tc = codeString(is);
            code = (tc == null) ? code : tc;
            //System.out.println(code);
            br = new BufferedReader(new InputStreamReader(is, "UTF-8"));
            String line = null;
            while ((line = br.readLine()) != null) {
                try {
                    line = line.trim();
                    if (line.startsWith("#")) {
                        continue;
                    }
                    if (line.equals("")) {
                        continue;
                    }
                    String[] ls = line.split("=");
                    if (ls != null) {
                        if (ls.length == 2) {
                            String key = ls[0];
                            String value = ls[1];
                            if (key != null) {
                                key = key.trim();
                                if (value != null) {
                                    value = value.trim();
                                }
                                map.put(key, value);
                            }
                        }
                    }

                } catch (Exception e) {
                    System.out.println(e.toString());
                }
            }
            br.close();
            is.close();

        } catch (Exception e) {
            System.out.println(e.toString());
        } finally {
        }
        return map;
    }

    /**
     * 判断文件的编码格式
     *
     * @param fileName :file
     * @return 文件编码格式
     * @throws Exception
     */
    private static String codeString(InputStream bin) throws Exception {
        int p = (bin.read() << 8) + bin.read();
        String code = null;
        switch (p) {
            case 0xefbb:
                code = "UTF-8";
                break;
            case 0xfffe:
                code = "Unicode";
                break;
            case 0xfeff:
                code = "UTF-16BE";
                break;
            default:
                code = "GBK";
        }
        //System.out.println(code);
        return code;
    }

    public static void main(String args[]) {
        String path = "G:\\work\\exam3\\build\\web\\language\\zh_CN\\talk\\default.la";
        Map map = I18nHelper.loadAsMap(path);
        Set<String> set = map.keySet();
        for (String s : set) {
            System.out.println(s + ":" + map.get(s));
        }
    }

}
