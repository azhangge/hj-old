package com.hjedu.platform.controller;

import java.io.Serializable;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;

import com.hjedu.customer.entity.BbsUser;
import com.hjedu.platform.dao.IBbsMessageDAO;
import com.hjedu.platform.entity.BbsMessage;
import com.huajie.util.JsfHelper;
import com.huajie.util.SpringHelper;

@ManagedBean
@ViewScoped
public class ClientListMsg  implements Serializable{

    List<BbsMessage> incomes;
    List<BbsMessage> outgos;
    IBbsMessageDAO bbsMessageDAO = SpringHelper.getSpringBean("BbsMessageDAO");
    ClientSession cs = JsfHelper.getBean("clientSession");
    BbsMessage mm = new BbsMessage();
    HttpServletRequest request = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();

    public List<BbsMessage> getIncomes() {
        return incomes;
    }

    public void setIncomes(List<BbsMessage> incomes) {
        this.incomes = incomes;
    }

    public List<BbsMessage> getOutgos() {
        return outgos;
    }

    public void setOutgos(List<BbsMessage> outgos) {
        this.outgos = outgos;
    }

    public BbsMessage getMm() {
        return mm;
    }

    public void setMm(BbsMessage mm) {
        this.mm = mm;
    }

    @PostConstruct
    public void init() {
        synchronizeDB();
    }

    private void synchronizeDB() {
        BbsUser cu = cs.getUsr();
        this.incomes = this.bbsMessageDAO.findMessageModelByReceiver(cu.getId());
        this.outgos = this.bbsMessageDAO.findMessageModelBySender(cu.getId());

    }

    public String senderMarkdel(String id) {
        this.bbsMessageDAO.senderMarkDel(id);
        synchronizeDB();
        return null;
    }

    public String receiverMarkdel(String id) {
        this.bbsMessageDAO.receiverMarkDel(id);
        synchronizeDB();
        return null;
    }

    public void viewMM(String id) {
        this.mm = this.bbsMessageDAO.findMessageModel(id);
    }
}
