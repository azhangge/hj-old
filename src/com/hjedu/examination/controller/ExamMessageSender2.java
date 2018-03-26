package com.hjedu.examination.controller;

import java.io.BufferedWriter;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.Socket;

import com.huajie.util.SpringHelper;

public class ExamMessageSender2 implements Runnable {

    int port = SpringHelper.getSpringBean("external_exam_stat_port2");
    String ip = SpringHelper.getSpringBean("external_exam_stat_addr2");
    ExamMessage msg;
    Socket socket = null;

    public ExamMessageSender2(ExamMessage msg) {
        this.msg = msg;
    }

    public void run() {
        try {
            socket = new Socket(ip, port);
            OutputStream os=socket.getOutputStream();
                BufferedWriter writer=new BufferedWriter(new OutputStreamWriter(os));
                writer.write("0008"+msg.getUrn()+(msg.isIfPass()?"1":"0"));
                writer.flush();
                
                writer.close();
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
        em.setDateStr("2014-07.19");
        em.setUrn("1001");
        em.setIfPass(true);
        new Thread(new ExamMessageSender2(em)).start();

    }

}
