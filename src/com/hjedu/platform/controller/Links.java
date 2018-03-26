package com.hjedu.platform.controller;

import java.util.ArrayList;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.model.SelectItem;

import com.hjedu.platform.dao.IPartnerDAO;
import com.hjedu.platform.dao.IPartnerTypeDAO;
import com.hjedu.platform.entity.PartnerModel;
import com.hjedu.platform.entity.PartnerType;
import com.huajie.util.SpringHelper;

@ManagedBean
@RequestScoped
public class Links {

    IPartnerDAO partnerDAO = SpringHelper.getSpringBean("PartnerDAO");
    IPartnerTypeDAO typeDAO = SpringHelper.getSpringBean("PartnerTypeDAO");
    List sss = new ArrayList();
    List<PartnerModel> allPartner;

    public List getSss() {
        return sss;
    }

    public void setSss(List sss) {
        this.sss = sss;
    }

    public List<PartnerModel> getAllPartner() {
        return allPartner;
    }

    public void setAllPartner(List<PartnerModel> allPartner) {
        this.allPartner = allPartner;
    }
    
    

    @PostConstruct
    public void init() {

        this.allPartner = this.partnerDAO.findAllPartnerModel();

        List<PartnerType> pts = typeDAO.findAllPartnerType();
        for (PartnerType pt : pts) {
            List<SelectItem> sis = new ArrayList();
            SelectItem st = new SelectItem("#", "－－－¤" + pt.getName() + "¤－－－");
            sis.add(st);
            List<PartnerModel> pms = partnerDAO.findPartnerModelByType(pt.getId());
            for (PartnerModel pm : pms) {
                SelectItem si = new SelectItem(pm.getUrl(), pm.getName());
                sis.add(si);
            }
            sss.add(sis);
        }
    }
}