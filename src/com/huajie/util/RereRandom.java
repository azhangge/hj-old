/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.huajie.util;

import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;
import org.apache.commons.math3.random.RandomDataGenerator;

import com.huajie.seller.dao.IOrderSerialDAO;

/**
 *
 * @author 铁铁
 */
public class RereRandom {

    /**
     * 随机指定范围内N个不重复的数 最简单最基本的方法
     *
     * @param min 指定范围最小值
     * @param max 指定范围最大值
     * @param n 随机数个数
     */
    public static int[] randomCommon(int min, int max, int n) {
        if (n > (max - min + 1) || max < min) {
            return null;
        }
        int[] result = new int[n];
        int count = 0;
        while (count < n) {
            int num = (int) (Math.random() * (max - min)) + min;
            boolean flag = true;
            for (int j = 0; j < n; j++) {
                if (num == result[j]) {
                    flag = false;
                    break;
                }
            }
            if (flag) {
                result[count] = num;
                count++;
            }
        }
        return result;
    }

    /**
     * 随机指定范围内N个不重复的数 利用HashSet的特征，只能存放不同的值
     *
     * @param min 指定范围最小值
     * @param max 指定范围最大值
     * @param n 随机数个数
     * @param HashSet<Integer> set 随机数结果集
     */
    public static void randomSet(int min, int max, int n, HashSet<Integer> set) {
        if (n > (max - min + 1) || max < min) {
            return;
        }
        for (int i = 0; i < n; i++) {
            // 调用Math.random()方法  
            int num = (int) (Math.random() * (max - min)) + min;
            set.add(num);// 将不同的数存入HashSet中  
        }
        int setSize = set.size();
        // 如果存入的数小于指定生成的个数，则调用递归再生成剩余个数的随机数，如此循环，直到达到指定大小  
        if (setSize < n) {
            randomSet(min, max, n - setSize, set);// 递归  
        }
    }

    /**
     * 随机指定范围内N个不重复的数 在初始化的无重复待选数组中随机产生一个数放入结果中，
     * 将待选数组被随机到的数，用待选数组(len-1)下标对应的数替换 然后从len-2里随机产生下一个随机数，如此类推
     *
     * @param max 指定范围最大值
     * @param min 指定范围最小值
     * @param n 随机数个数
     * @return int[] 随机数结果集
     */
    public static int[] randomArray(int min, int max, int n) {
        int len = max - min + 1;

        if (max < min || n > len) {
            return null;
        }

        //初始化给定范围的待选数组  
        int[] source = new int[len];
        for (int i = min; i < min + len; i++) {
            source[i - min] = i;
        }

        int[] result = new int[n];
        Random rd = new Random();
        int index = 0;
        for (int i = 0; i < result.length; i++) {
            //待选数组0到(len-2)随机一个下标  
            index = Math.abs(rd.nextInt() % len--);
            //将随机到的数放入结果集  
            result[i] = source[index];
            //将待选数组中被随机到的数，用待选数组(len-1)下标对应的数替换  
            source[index] = source[len];
        }
        return result;
    }

    public static Set randomSet(long min, long max, long num) {
        Set<Long> set = new HashSet();
        long len = max - min + 1;
        if (max < min || num > len) {
            return set;
        }
        while (set.size() < num) {
            long tem = (long) (min + (max - min) * Math.random());
            set.add(tem);
        }
        
        return set;
    }

    public static Set randomSet2(long min, long max, long num) {
        Set<Long> set = new HashSet();
        long len = max - min + 1;
        if (max < min || num > len) {
            return set;
        }
        RandomDataGenerator randomData = new RandomDataGenerator();
        while (set.size() < num) {
            long value = randomData.nextLong(min, max - 1);
            set.add(value);
        }
        return set;
    }

    public static String fetchOrderId2() {
        String dateStr = new SimpleDateFormat("yyMMdd").format(new Date());
        IOrderSerialDAO serialDAO = SpringHelper.getSpringBean("OrderSerialDAO");
        String serial = new RandomId().randomId(serialDAO.generateOrderSerial());
        StringBuilder sb = new StringBuilder();
        sb.append(dateStr);
        sb.append(serial);
        return sb.toString();
    }

    public static String fetchOrderId() {
        String dateStr = new SimpleDateFormat("yyMMdd").format(new Date());
        IOrderSerialDAO serialDAO = SpringHelper.getSpringBean("OrderSerialDAO");
        String serial = String.format("%05d", serialDAO.generateOrderSerial());
        StringBuilder sb = new StringBuilder();
        sb.append(dateStr);
        sb.append(serial);
        return sb.toString();
    }

    public static void main(String[] args) {
        long b1 = System.currentTimeMillis();
        for (int j = 0; j < 100; j++) {
            Set<Long> longs = RereRandom.randomSet(0, 100, 10);
            for (long l : longs) {
                System.out.print(l + ",");
            }
            System.out.println();
        }
        long e1 = System.currentTimeMillis();
        System.out.println(e1 - b1);
        System.out.println("---------------------------------");
        long b2 = System.currentTimeMillis();
        for (int j = 0; j < 100; j++) {
            Set<Long> longs = RereRandom.randomSet2(0, 100, 10);
            for (long l : longs) {
                System.out.print(l + ",");
            }
            System.out.println();
        }
        long e2 = System.currentTimeMillis();
        System.out.println(e2 - b2);
    }

}
