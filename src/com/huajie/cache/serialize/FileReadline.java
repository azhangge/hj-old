/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.huajie.cache.serialize;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.RandomAccessFile;

/**
 *
 * @author huajie
 */
public class FileReadline {

    public static void main(String args[]) throws IOException {
        int modifyLine = 4;//要修改的行
        RandomAccessFile rf = new RandomAccessFile("C:\\ttt.txt", "rw");
        String line;
        int count = 0;
        long oldPos = 0;
        long pos = 0;
        while ((line = rf.readLine()) != null) {
            if (count != 0) {
                oldPos = pos;
            }
            pos = rf.getFilePointer();
            String line2 = new String(line.getBytes("ISO8859-1"), "UTF-8");
            System.out.println(line2 + ":" + pos+",oldPos："+oldPos);
            if (line2.startsWith("e")) {
                
                rf.seek(oldPos);
                String str = "d";
                str = new String(str.getBytes("UTF-8"), "ISO8859-1");
                rf.writeBytes(str);
                break;
            }
            count++;
        }
        rf.close();
    }
}
