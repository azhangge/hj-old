package com.huajie.util;

/**
 * @className:MyLevenshtein.java
 * @classDescription:Levenshtein Distance 算法实现 可以使用的地方：DNA分析 拼字检查 语音辨识 抄袭侦测
 * @author:donghai.wan
 * @createTime:2012-1-12
 */
public class SimilarAlgorithm {

    public static void main(String[] args) {
        //要比较的两个字符串
        String str1 = "今天是星期三";
        String str2 = "今天是星期五";
        float s = SimilarAlgorithm.getSimilarity(str1, str2);
        System.out.println(s);
    }

    /**
     * DNA分析 拼字检查 语音辨识 抄袭侦测
     *
     * @createTime 2012-1-12
     */
    private static int levenshtein(String str1, String str2) {
        //计算两个字符串的长度。
        if (str1 == null) {
            str1 = "";
        }
        if (str2 == null) {
            str2 = "";
        }
        int len1 = str1.length();
        int len2 = str2.length();
        //建立上面说的数组，比字符长度大一个空间
        int[][] dif = new int[len1 + 1][len2 + 1];
        //赋初值，步骤B。
        for (int a = 0; a <= len1; a++) {
            dif[a][0] = a;
        }
        for (int a = 0; a <= len2; a++) {
            dif[0][a] = a;
        }
        //计算两个字符是否一样，计算左上的值
        int temp;
        for (int i = 1; i <= len1; i++) {
            for (int j = 1; j <= len2; j++) {
                if (str1.charAt(i - 1) == str2.charAt(j - 1)) {
                    temp = 0;
                } else {
                    temp = 1;
                }
                //取三个值中最小的
                dif[i][j] = min(dif[i - 1][j - 1] + temp, dif[i][j - 1] + 1,
                        dif[i - 1][j] + 1);
            }
        }
        //System.out.println("字符串\"" + str1 + "\"与\"" + str2 + "\"的比较");
        //取数组右下角的值，同样不同位置代表不同字符串的比较
        //System.out.println("差异步骤：" + dif[len1][len2]);
        return dif[len1][len2];
        //计算相似度

    }

    public static float getSimilarity(String s1, String s2) {
        try {
            int i = SimilarAlgorithm.levenshtein(s1, s2);
            Float similarity = 1 - (float) i / Math.max(s1.length(), s2.length());
            //System.out.println("相似度：" + similarity);
            if (similarity.isNaN()) {
                similarity = 0F;
            }
            return similarity;
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

    //得到最小值
    private static int min(int... is) {
        int min = Integer.MAX_VALUE;
        for (int i : is) {
            if (min > i) {
                min = i;
            }
        }
        return min;
    }
}
