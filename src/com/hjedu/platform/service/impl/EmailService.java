package com.hjedu.platform.service.impl;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.velocity.app.VelocityEngine;
import org.springframework.ui.velocity.VelocityEngineUtils;

import com.hjedu.platform.dao.ISystemEmailBoxDAO;
import com.hjedu.platform.entity.SystemEmailBoxModel;
import com.huajie.util.SendEmail;
import com.huajie.util.SpringHelper;

public class EmailService implements Serializable {

    ISystemEmailBoxDAO esmss;
    private VelocityEngine velocityEngine;//spring配置中定义

    public ISystemEmailBoxDAO getEsmss() {
        return esmss;
    }

    public void setEsmss(ISystemEmailBoxDAO esmss) {
        this.esmss = esmss;
    }

    public VelocityEngine getVelocityEngine() {
        return velocityEngine;
    }

    public void setVelocityEngine(VelocityEngine velocityEngine) {
        this.velocityEngine = velocityEngine;
    }

    public boolean sendEmail(String toEmail,
            String title, String content) {
        boolean ifs = false;
        List<SystemEmailBoxModel> es = esmss.findAllEmailBox();

        for (SystemEmailBoxModel e : es) {
            String host = e.getSmtpHost();
            String port = e.getSmtpPort();
            String urn = e.getSmtpUrn();
            String pwd = e.getSmtpPwd();
            String addr = e.getAddress();
            String alias = e.getAlias();
            boolean ssl = e.getSsl();
            boolean auth = e.getAuth();
            try {
                ifs = SendEmail.sendEmail("smtp",
                        host, port, auth,
                        ssl,
                        urn, pwd, addr,
                        alias, toEmail, title,
                        content);
                if (ifs) {
                    break;
                }
            } catch (Exception ee) {
                ee.printStackTrace();
            }
        }
        return ifs;
    }

    public boolean sendWithTemplate(String toEmail, String title, String templatePath, Map model) {
        String result = null;
        try {
            result = VelocityEngineUtils.mergeTemplateIntoString(this.getVelocityEngine(), templatePath, "UTF-8", model);
            boolean b=this.sendEmail(toEmail, title, result);
            return b;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public static void main(String args[]) {
        EmailService es = SpringHelper.getSpringBean("EmailService");
        Map map=new HashMap();
        map.put("username","Litie");
        map.put("url","http://test.huajie.com/exam2");
        map.put("sysname","华杰软件");
        es.sendWithTemplate("lteb2002@163.com", "test", "reg_mail.vm", map);
    }
}
