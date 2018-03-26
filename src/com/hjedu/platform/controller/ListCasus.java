package com.hjedu.platform.controller;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

import com.hjedu.customer.entity.AdminInfo;
import com.hjedu.platform.dao.ICasusDAO;
import com.hjedu.platform.entity.CasusModel;
import com.huajie.util.CookieUtils;
import com.huajie.util.ExternalUserUtil;
import com.huajie.util.JsfHelper;
import com.huajie.util.SpringHelper;

@ManagedBean
@ViewScoped
public class ListCasus implements Serializable {

    List<CasusModel> casusList = new ArrayList();
    ICasusDAO casusDAO = SpringHelper.getSpringBean("CasusDAO");
    CasusModel casus;
    String businessId;

    public List<CasusModel> getCasusList() {
        return this.casusList;
    }

    public void setCasusList(List<CasusModel> casusList) {
        this.casusList = casusList;
    }

    public ICasusDAO getCasusDAO() {
        return casusDAO;
    }

    public void setCasusDAO(ICasusDAO casusDAO) {
        this.casusDAO = casusDAO;
    }

    
    
    public void setBusinessId(String businessId) {
        this.businessId = businessId;
    }

    public String getbusinessId() {
        return businessId;
    }

    public CasusModel getCasus() {
        return casus;
    }
    public void setCasus(CasusModel casus) {
        this.casus = casus;
    }

    @PostConstruct
    public void init() {
    	this.setBusinessId(CookieUtils.getBusinessId(JsfHelper.getRequest()));
        synchronizeDB();
    }

    private void synchronizeDB() {
        this.casusList = this.casusDAO.findAllCasusesByBusinessId(this.businessId);
//        AdminInfo ai = ExternalUserUtil.getAdminBySession();
//        this.casusList = this.casusDAO.findCasusesByAdmin(ai.getId());
    }

    public void deleteCasus(String idt) {
        if (idt != null) {
            this.casusDAO.deleteCasus(idt);
        }
        synchronizeDB();
    }

    public String viewCasus(String id) {
        casus = this.casusDAO.findCasus(id);
        return null;
    }
    
    public String editOrd(String id) {
        for (CasusModel cq : this.casusList) {
            if (id.equals(cq.getId())) {
                this.casusDAO.updateCasus(cq);
                break;
            }
        }
        return null;
    }
}
