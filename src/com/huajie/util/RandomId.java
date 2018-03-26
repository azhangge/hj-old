package com.huajie.util;

import java.util.Random;

public class RandomId {

    private Random random;
    private String table;

    public RandomId() {
        random = new Random();
        table = "0123456789";
    }

    public String randomId(long id) {
        String ret = null, num = String.format("%05d", id);
        //System.out.println(num);
        int key = random.nextInt(10), seed = random.nextInt(100);
        Caesar caesar = new Caesar(table, seed);
        num = caesar.encode(key, num);
        //ret = num + String.format("%01d", key) + String.format("%02d", seed);
        ret=num;
        return ret;
    }
    

    public static void main(String[] args) {
        RandomId r = new RandomId();
        int j=0;
        for (int i = 0; i < 30; i += 1) {
            System.out.println(r.randomId(10057+i));
        }
    }
}
