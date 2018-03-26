package com.hjedu.platform.controller;

import java.io.Serializable;
import java.util.Date;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import com.hjedu.customer.dao.IBbsUserDAO;
import com.hjedu.platform.dao.ICasusDAO;
import com.hjedu.platform.entity.CasusModel;
import com.hjedu.platform.service.ILogService;
import com.hjedu.platform.service.IMessageService;
import com.huajie.exam.thread.EmailSender;
import com.huajie.util.Cat;
import com.huajie.util.CookieUtils;
import com.huajie.util.ExternalUserUtil;
import com.huajie.util.JsfHelper;
import com.huajie.util.SpringHelper;

@ManagedBean
@ViewScoped
public class AACasus implements Serializable{
    
    CasusModel casus;
    ICasusDAO casusDAO = SpringHelper.getSpringBean("CasusDAO");
    String nothing;
    private boolean flag = false;
    boolean ifSendEmail = false;
    boolean ifSendMessage = false;
    
    String bussinessId = CookieUtils.getBusinessId(JsfHelper.getRequest());
    
    public boolean isIfSendMessage() {
		return ifSendMessage;
	}

	public void setIfSendMessage(boolean ifSendMessage) {
		this.ifSendMessage = ifSendMessage;
	}

	IBbsUserDAO cud = SpringHelper.getSpringBean("BbsUserDAO");
    ILogService logger = SpringHelper.getSpringBean("LogService");
    
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
    
    public CasusModel getCasus() {
        return this.casus;
    }
    
    public void setCasus(CasusModel casus) {
        this.casus = casus;
    }
    
    public boolean isFlag() {
        return flag;
    }
    
    public void setFlag(boolean flag) {
        this.flag = flag;
    }
    
    @PostConstruct
    public void init() {
        this.casus = new CasusModel();
        this.casus.setInputdate(new Date());
        String idt = JsfHelper.getRequest().getParameter("id");
        if (idt != null) {
            this.casus = this.casusDAO.findCasus(idt);
            this.flag = true;
        }
    }
    
    public String submit_action() {
        if (this.ifSendEmail) {
            EmailSender ess = new EmailSender(this.casus.getTitle(),
                    this.casus.getContent());
            ThreadPoolTaskExecutor exec = SpringHelper.getSpringBean("taskExecutor");
            exec.execute(ess);
        }
        if (this.ifSendMessage) {
        	IMessageService messageService = SpringHelper.getSpringBean("MessageService");
        	messageService.sendMessageToAllUsers(bussinessId, casus.getTitle(), casus.getContent());
        }
        this.casus.setAdmin(ExternalUserUtil.getAdminBySession());
        this.casus.setBusinessId(CookieUtils.getBusinessId(JsfHelper.getRequest()));
        try {
            if (!this.flag) {
                CasusModel n = this.casus;
                this.casusDAO.addCasus(n);
                this.logger.log("添加了新通知："+casus.getTitle());
            } else {
                CasusModel nn = this.casus;
                this.casusDAO.updateCasus(nn);
                this.logger.log("修改了通知："+casus.getTitle());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        return "ListCasus?faces-redirect=true";
    }
}
