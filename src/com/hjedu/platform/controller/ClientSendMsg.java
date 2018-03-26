package com.hjedu.platform.controller;

import java.io.Serializable;
import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;

import com.hjedu.customer.dao.IBbsUserDAO;
import com.hjedu.customer.entity.BbsUser;
import com.hjedu.platform.dao.IBbsMessageDAO;
import com.hjedu.platform.entity.BbsMessage;
import com.huajie.util.JsfHelper;
import com.huajie.util.SpringHelper;

@ManagedBean
@ViewScoped
public class ClientSendMsg  implements Serializable{

    IBbsMessageDAO messageDAO = SpringHelper.getSpringBean("BbsMessageDAO");
    IBbsUserDAO clientUserDAO = SpringHelper.getSpringBean("BbsUserDAO");

    ClientSession cs=JsfHelper.getBean("clientSession");
    BbsMessage mm = new BbsMessage();
    HttpServletRequest request = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
    boolean ifSendEmail;
    boolean ifSendOK = false;

    public boolean isIfSendOK() {
        return ifSendOK;
    }

    public void setIfSendOK(boolean ifSendOK) {
        this.ifSendOK = ifSendOK;
    }

    public boolean isIfSendEmail() {
        return this.ifSendEmail;
    }

    public void setIfSendEmail(boolean ifSendEmail) {
        this.ifSendEmail = ifSendEmail;
    }

    public BbsMessage getMm() {
        return this.mm;
    }

    public void setMm(BbsMessage mm) {
        this.mm = mm;
    }

    @PostConstruct
    public void init() {
        String idt = this.request.getParameter("uid");
        if (idt != null) {
            BbsUser bu = clientUserDAO.findBbsUser(idt);
            if (bu != null) {
                this.mm.setReceiver(this.clientUserDAO.findBbsUser(idt));
            }
            if (cs == null) {
                return;
            }
            BbsUser cu = cs.getUsr();
            if (cu != null) {
                this.mm.setSender(cu);
            }
        }
    }

    public String send() {
        this.messageDAO.addMessageModel(this.mm);
        this.ifSendOK=true;
        return "ClientSendMsgSuccess";
    }

    public void begainSend(String to) {
        this.mm=new BbsMessage();
        BbsUser bu = this.clientUserDAO.findBbsUser(to);
        if (bu != null) {
            this.mm.setReceiver(bu);
        }
        BbsUser cu = cs.getUsr();
        if (cu != null) {
            this.mm.setSender(cu);
        }
    }

    public void sendOK() {
        this.messageDAO.addMessageModel(this.mm);
        this.ifSendOK=true;
    }
}