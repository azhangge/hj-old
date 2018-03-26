package com.hjedu.customer.controller;

import java.io.Serializable;
import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;

import com.hjedu.customer.dao.IBbsUserDAO;
import com.hjedu.customer.entity.BbsUser;
import com.hjedu.platform.controller.ClientSession;
import com.huajie.util.JsfHelper;
import com.huajie.util.SpringHelper;

@ManagedBean
@ViewScoped
public class UserCenter  implements Serializable{

    private IBbsUserDAO userDAO = SpringHelper.getSpringBean("BbsUserDAO");
    private BbsUser user;

    public BbsUser getUser() {
        return user;
    }

    public void setUser(BbsUser user) {
        this.user = user;
    }

   @PostConstruct
    public void init() {
        HttpServletRequest request = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
        String temp = request.getParameter("id");
        if (temp != null) {
            this.user = this.userDAO.findBbsUser(temp.trim());
        } else {
            ClientSession cs = JsfHelper.getBean("clientSession");
            this.user = cs.getUsr();
        }
    }
}
