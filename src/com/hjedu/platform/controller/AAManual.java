package com.hjedu.platform.controller;

import java.io.Serializable;
import java.util.Date;
import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import com.hjedu.customer.dao.IBbsUserDAO;
import com.hjedu.platform.dao.IManualDAO;
import com.hjedu.platform.entity.ManualModel;
import com.huajie.exam.thread.EmailSender;
import com.huajie.util.Cat;
import com.huajie.util.JsfHelper;
import com.huajie.util.SpringHelper;

@ManagedBean
@ViewScoped
public class AAManual implements Serializable{
    
    ManualModel manual;
    IManualDAO manualDAO = SpringHelper.getSpringBean("ManualDAO");
    String nothing;
    private boolean flag = false;
    boolean ifSendEmail = false;
    IBbsUserDAO cud = SpringHelper.getSpringBean("BbsUserDAO");
    
    public String getNothing() {
        return Cat.getUniqueId();
    }
    
    public void setNothing(String nothing) {
        this.nothing = nothing;
    }
    
    public boolean isIfSendEmail() {
        return this.ifSendEmail;
    }
    
    public void setIfSendEmail(boolean ifSendEmail) {
        this.ifSendEmail = ifSendEmail;
    }
    
    public ManualModel getManual() {
        return this.manual;
    }
    
    public void setManual(ManualModel manual) {
        this.manual = manual;
    }
    
    public boolean isFlag() {
        return flag;
    }
    
    public void setFlag(boolean flag) {
        this.flag = flag;
    }
    
    @PostConstruct
    public void init() {
        this.manual = new ManualModel();
        this.manual.setInputdate(new Date());
        String idt = JsfHelper.getRequest().getParameter("id");
        if (idt != null) {
            this.manual = this.manualDAO.findManual(idt);
            this.flag = true;
        }
    }
    
    public String submit_action() {
        if (this.ifSendEmail) {
            EmailSender ess = new EmailSender(this.manual.getTitle(),
                    this.manual.getContent());
            ThreadPoolTaskExecutor exec = SpringHelper.getSpringBean("taskExecutor");
            exec.execute(ess);
        }
        try {
            if (!this.flag) {
                ManualModel n = this.manual;
                this.manualDAO.addManual(n);
            } else {
                ManualModel nn = this.manual;
                this.manualDAO.updateManual(nn);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        return "ListManual?faces-redirect=true";
    }
}
