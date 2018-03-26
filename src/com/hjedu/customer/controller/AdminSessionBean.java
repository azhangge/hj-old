package com.hjedu.customer.controller;

import java.io.Serializable;
import java.util.Date;
import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import org.primefaces.component.accordionpanel.AccordionPanel;
import org.primefaces.event.TabChangeEvent;

import com.hjedu.customer.dao.IAdminDAO;
import com.hjedu.customer.entity.AdminInfo;
import com.huajie.util.MyLogger;
import com.huajie.util.SpringHelper;

@ManagedBean
@SessionScoped
public class AdminSessionBean implements Serializable {

    Date ltime = new Date();
    boolean ifLogin = false;
    AdminInfo admin = null;
    boolean ifSuper = false;
    String activeIndex = "0";

    public AdminInfo getAdmin() {
        return this.admin;
    }

    public void setAdmin(AdminInfo admin) {
        this.admin = admin;
    }

    public Date getLtime() {
        return this.ltime;
    }

    public void setLtime(Date ltime) {
        this.ltime = ltime;
    }

    public String getActiveIndex() {
        return activeIndex;
    }

    public void setActiveIndex(String activeIndex) {

        this.activeIndex = activeIndex;
    }

    public boolean isIfSuper() {
        return this.ifSuper;
    }

    public void setIfSuper(boolean ifSuper) {
        this.ifSuper = ifSuper;
    }

    public boolean isIfLogin() {
        return this.ifLogin;
    }

    public void setIfLogin(boolean ifLogin) {
        this.ifLogin = ifLogin;
    }

    @PostConstruct
    public void init() {
    }

    public void onChange(TabChangeEvent event) {
        AccordionPanel accordionPanel1 = (AccordionPanel) event.getTab().getParent();
        String t = accordionPanel1.getActiveIndex();
        MyLogger.echo(t);
        this.activeIndex = t;
        //...
    }
    
    public void refreshAdmin() {
        IAdminDAO adminDAO = SpringHelper.getSpringBean("AdminDAO");
        if (this.admin != null) {
            admin=adminDAO.findAdmin(admin.getId());
        }
    }

    public String exitSys() {
        this.setAdmin(null);
        this.setIfLogin(false);
        this.setIfSuper(false);
        return "/AdminLogin??faces-redirect=true";
    }
}