package com.hjedu.examination.controller;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;

import com.huajie.util.SpringHelper;

public class ExternalIpChecker {

    public static boolean checkIp(String urn, String ip) {
        ExamMessage em = new ExamMessage();
        em.setUrn(urn);
        em.setIp(ip);
        String saddr = SpringHelper.getSpringBean("external_exam_stat_addr3");
        int sport = SpringHelper.getSpringBean("external_exam_stat_port3");

        try {//进入考试前首先从调度中心查询IP地址是否正确
            Socket socket = new Socket(saddr, sport);
            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));

            writer.write(urn + ":" + ip);
            writer.write("\n");
            writer.flush();

            BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            String result = reader.readLine();
            result = result.trim().replace("\n", "");
            writer.close();
            reader.close();
            socket.close();

            return Boolean.parseBoolean(result);

        } catch (Exception e) {
            e.printStackTrace();

            return false;
        }

    }

    public static void main(String args[]) {
        String urn = "9601103";
        String ip = "192.168.2.200";
        boolean result = ExternalIpChecker.checkIp(urn, ip);
        System.out.println(result);
    }

}
