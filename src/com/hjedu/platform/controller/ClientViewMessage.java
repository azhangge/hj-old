package com.hjedu.platform.controller;

import java.io.Serializable;
import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.servlet.http.HttpServletRequest;

import com.hjedu.platform.dao.IBbsMessageDAO;
import com.hjedu.platform.entity.BbsMessage;
import com.huajie.util.JsfHelper;
import com.huajie.util.SpringHelper;

@ManagedBean
@ViewScoped
public class ClientViewMessage  implements Serializable{
    
    IBbsMessageDAO messageDAO = SpringHelper.getSpringBean("BbsMessageDAO");
    BbsMessage mm = new BbsMessage();
    HttpServletRequest request = JsfHelper.getRequest();
    BbsMessage mn = new BbsMessage();
    
    public BbsMessage getMm() {
        return this.mm;
    }
    
    public void setMm(BbsMessage mm) {
        this.mm = mm;
    }
    
    public BbsMessage getMn() {
        return this.mn;
    }
    
    public void setMn(BbsMessage mn) {
        this.mn = mn;
    }
    
    public IBbsMessageDAO getBbsMessageDAO() {
        return this.messageDAO;
    }
    
    public void setBbsMessageDAO(IBbsMessageDAO messageDAO) {
        this.messageDAO = messageDAO;
    }
    
    @PostConstruct
    public void init() {
        String idt = this.request.getParameter("mid");
        if (idt != null) {
            this.mm = this.messageDAO.findMessageModel(idt);
            this.mm.setReaded(true);
            this.messageDAO.updateMessageReaded(mm.getId());
            
            this.mn.setReceiver(this.mm.getSender());
            this.mn.setSender(this.mm.getReceiver());
            this.mn.setTitle("回复：" + this.mm.getTitle());
            
        }
    }
    
    public String send() {
        this.messageDAO.addMessageModel(this.mn);
        return "ClientSendMsgSuccess?faces-redirect=true";
    }
}
