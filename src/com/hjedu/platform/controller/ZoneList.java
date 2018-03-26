package com.hjedu.platform.controller;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;

import com.hjedu.customer.dao.IBbsUserDAO;
import com.hjedu.customer.entity.BbsUser;
import com.hjedu.platform.dao.IBbsZoneDAO;
import com.hjedu.platform.entity.BbsZone;
import com.huajie.util.JsfHelper;
import com.huajie.util.SpringHelper;

@ManagedBean
@RequestScoped
public class ZoneList implements Serializable {

    List<BbsZone> zones = new ArrayList();
    IBbsZoneDAO bbsZoneDAO = SpringHelper.getSpringBean("BbsZoneDAO");

    public List<BbsZone> getZones() {
        return zones;
    }

    public void setZones(List<BbsZone> zones) {
        this.zones = zones;
    }

    @PostConstruct
    public void init() {
        //以下代码为适配用户从外部环境中加载试题模块
        String uid = JsfHelper.getRequest().getParameter("uid");
        if (uid != null) {
            ClientSession cs = JsfHelper.getBean("clientSession");
            if (cs == null) {
                cs = new ClientSession();
                JsfHelper.getRequest().getSession().setAttribute("clientSession", cs);
            }
            IBbsUserDAO userDAO = SpringHelper.getSpringBean("BbsUserDAO");
            BbsUser bu = userDAO.findBbsUser(uid);
            if (bu != null) {
                cs.setUsr(bu);
            }
        }
        this.zones = this.bbsZoneDAO.findAll();
    }
}
