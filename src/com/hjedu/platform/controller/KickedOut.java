package com.hjedu.platform.controller;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.servlet.http.HttpServletRequest;

import com.hjedu.customer.service.IUserSessionStateService;
import com.hjedu.platform.entity.UserSessionState;
import com.huajie.util.JsfHelper;
import com.huajie.util.SpringHelper;

@ManagedBean
@RequestScoped
public class KickedOut {
    
    UserSessionState us;

    public UserSessionState getUs() {
        return us;
    }

    public void setUs(UserSessionState us) {
        this.us = us;
    }
    
    
    
    

    @PostConstruct
    public void init() {
        HttpServletRequest request = JsfHelper.getRequest();
        String sid=request.getSession().getId();
        
        IUserSessionStateService iussService = SpringHelper.getSpringBean("UserSessionStateService");
        us = iussService.findKickedSession(sid);
        
    }

}
