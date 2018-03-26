package com.hjedu.platform.controller;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;

import com.hjedu.customer.dao.IBbsUserDAO;
import com.hjedu.customer.entity.BbsUser;
import com.hjedu.customer.service.impl.ComplexBbsUserService;
import com.hjedu.platform.service.impl.EmailService;
import com.huajie.util.JsfHelper;
import com.huajie.util.SpringHelper;

@ManagedBean
@ViewScoped
public class GetPWD2 implements Serializable {

    String getMethod="email";//email or phone
    String email;
    BbsUser cu;
    IBbsUserDAO clientDAO = SpringHelper.getSpringBean("BbsUserDAO");
    EmailService emailService = SpringHelper.getSpringBean("EmailService");

    public String getEmail() {
        return this.email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getGetMethod() {
        return getMethod;
    }

    public void setGetMethod(String getMethod) {
        this.getMethod = getMethod;
    }

    @PostConstruct
    public void init() {
        this.cu = this.clientDAO.findBbsUserByEmail(this.email);
    }

    public String check() {
        FacesMessage fm = new FacesMessage();

        if (this.email.equals("")) {
            fm.setSummary("电子邮箱地址为空！");
            fm.setSeverity(FacesMessage.SEVERITY_ERROR);
            FacesContext.getCurrentInstance().addMessage("", fm);
            return null;
        }
        BbsUser temp1 = this.clientDAO.findBbsUserByEmail(this.email);
        if (temp1 == null) {
            fm.setSummary("此电子邮箱不存在！");
            fm.setSeverity(FacesMessage.SEVERITY_ERROR);
            FacesContext.getCurrentInstance().addMessage("", fm);
            return null;
        }

        ApplicationBean app = JsfHelper.getBean("applicationBean");
        String siteName = app.getInfo().getSiteName();

        Map map = new HashMap();
//        map.put("username", temp1.getUsername());
        map.put("username", temp1.getTel());
        ComplexBbsUserService cbus=SpringHelper.getSpringBean("ComplexBbsUserService");
        map.put("password", cbus.findBackPwd(temp1));
        map.put("sysname", siteName);
        boolean ifs = this.emailService.sendWithTemplate(temp1.getEmail(), siteName + "- 密码找回", "forgot_pwd.vm", map);

        if (ifs) {
            fm.setSummary("信息已经发出，请查阅您的邮件！");
            fm.setSeverity(FacesMessage.SEVERITY_INFO);
            FacesContext.getCurrentInstance().addMessage("", fm);
        } else {
            fm.setSummary("该邮箱存在，但由于系统错误，邮件未发出，请稍后再试！");
            fm.setSeverity(FacesMessage.SEVERITY_ERROR);
            FacesContext.getCurrentInstance().addMessage("", fm);
        }
        return null;
    }
}
