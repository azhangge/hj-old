package com.hjedu.examination.controller;

import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.huajie.util.SpringHelper;

public class ExamMessageSender implements Runnable {

    int port = SpringHelper.getSpringBean("external_exam_stat_port");
    String ip = SpringHelper.getSpringBean("external_exam_stat_addr");
    ExamMessage msg;
    Socket socket = null;

    public ExamMessageSender(ExamMessage msg) {
        this.msg = msg;
    }

    public void run() {
        try {
            socket = new Socket(ip, port);
            OutputStream os = socket.getOutputStream();
            ObjectOutputStream oos = new ObjectOutputStream(os);
            oos.writeObject(msg);
            oos.flush();
            oos.close();
            os.close();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (socket != null) {
                    socket.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }

    public static void main(String args[]) {
        ExamMessage em = new ExamMessage();
        em.setDateStr(new SimpleDateFormat("yyyy-MM-dd").format(new Date()));
        em.setUrn("1001");
        em.setIfPass(true);
        new Thread(new ExamMessageSender(em)).start();

    }

}
