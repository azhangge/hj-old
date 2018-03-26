package com.huajie.exam.thread;

import com.hjedu.customer.dao.IBbsUserDAO;
import com.hjedu.customer.entity.BbsUser;
import com.hjedu.platform.service.impl.EmailService;
import com.huajie.util.CookieUtils;
import com.huajie.util.JsfHelper;
import com.huajie.util.SpringHelper;

public class EmailSender implements Runnable {

    IBbsUserDAO cud = SpringHelper.getSpringBean("BbsUserDAO");
    EmailService emailService = SpringHelper.getSpringBean("EmailService");
    String title;
    String content;

    
    public EmailSender(String title, String content) {
        this.title = title;
        this.content = content;
    }

    @Override
    public void run() {
        for (BbsUser cu : this.cud.findAllBbsUser(CookieUtils.getBusinessId(JsfHelper.getRequest()))) {
            try {
                emailService.sendEmail(cu.getEmail(), title,
                        content);
            } catch (Exception e) {
                e.printStackTrace();
                continue;
            }
        }
    }
}
