package com.hjedu.platform.controller;


import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;
import javax.servlet.http.HttpServletRequest;

import com.hjedu.platform.dao.IPartnerDAO;
import com.hjedu.platform.dao.IPartnerTypeDAO;
import com.hjedu.platform.entity.PartnerModel;
import com.hjedu.platform.entity.PartnerType;
import com.huajie.util.Cat;
import com.huajie.util.SpringHelper;

@ManagedBean
@ViewScoped
public class AAPartner  implements Serializable{

    PartnerModel partner = new PartnerModel();
    String tit = "";
    IPartnerDAO partnerDAO = SpringHelper.getSpringBean("PartnerDAO");
    IPartnerTypeDAO typeDAO = SpringHelper.getSpringBean("PartnerTypeDAO");
    boolean flag = false;
    String nothing;
    List<SelectItem> ss = new ArrayList();

    public String getNothing() {
        return Cat.getUniqueId();
    }

    public void setNothing(String nothing) {
        this.nothing = nothing;
    }

    public String getTit() {
        return tit;
    }

    public void setTit(String tit) {
        this.tit = tit;
    }

    public PartnerModel getPartner() {
        return this.partner;
    }

    public void setPartner(PartnerModel partner) {
        this.partner = partner;
    }

    public List<SelectItem> getSs() {
        return ss;
    }

    public void setSs(List<SelectItem> ss) {
        this.ss = ss;
    }

    @PostConstruct
    public void init() {
        this.tit = "添加友情链接";
        HttpServletRequest req = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
        String id = req.getParameter("id");

        if (id != null) {
            this.partner = this.partnerDAO.findPartnerModel(id);
            this.tit = "修改友情链接";
            this.flag = true;
        }
        if (this.partner.getUrl() == null) {
            this.partner.setUrl("http://");
        }
        this.loadSelects();
    }

    public void loadSelects() {
        List<PartnerType> pts = this.typeDAO.findAllPartnerType();
        this.ss.clear();
        for (PartnerType pt : pts) {
            SelectItem si = new SelectItem(pt.getId(), pt.getName());
            ss.add(si);
        }
    }

    public String save_action() {
        PartnerModel pp = this.partner;
        try {
            if (!flag) {
                partnerDAO.addPartnerModel(this.partner);
                //logger.log("添加了友情链接："+this.partner.getName()+",ID:"+this.partner.getId());
            } else {
                partnerDAO.updatePartnerModel(this.partner);
                //logger.log("修改了友情链接："+this.partner.getName()+",ID:"+this.partner.getId());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return "ListPartner?faces-redirect=true";
    }
}