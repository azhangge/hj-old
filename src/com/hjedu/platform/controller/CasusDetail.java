package com.hjedu.platform.controller;

import java.io.Serializable;
import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;

import com.hjedu.platform.dao.ICasusDAO;
import com.hjedu.platform.entity.CasusModel;
import com.huajie.util.SpringHelper;

@ManagedBean
@RequestScoped
public class CasusDetail implements Serializable {

    ICasusDAO casusDAO = SpringHelper.getSpringBean("CasusDAO");
    CasusModel casus;


    public CasusModel getCasus() {
        return this.casus;
    }

    public void setCasus(CasusModel casus) {
        this.casus = casus;
    }

    @PostConstruct
    public void init() {
        HttpServletRequest request = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
        String idt = request.getParameter("id");
        if (idt != null) {
            casusDAO.updateCasusCountPlusOne(idt);
            this.casus = casusDAO.findCasus(idt);
           
        }
    }
}
