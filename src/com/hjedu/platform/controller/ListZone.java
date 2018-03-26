package com.hjedu.platform.controller;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;

import com.hjedu.platform.dao.IBbsZoneDAO;
import com.hjedu.platform.entity.BbsZone;
import com.hjedu.platform.service.ILogService;
import com.huajie.util.SpringHelper;

@ManagedBean
@RequestScoped
public class ListZone  implements Serializable{

    List<BbsZone> zones = new ArrayList();
    IBbsZoneDAO bbsZoneDAO=SpringHelper.getSpringBean("BbsZoneDAO");
    ILogService logger = SpringHelper.getSpringBean("LogService");
    
    public List<BbsZone> getZones() {
        return zones;
    }
    
    public void setZones(List<BbsZone> zones) {
        this.zones = zones;
    }
    
   
    
    @PostConstruct
    public void init() {
        this.zones = this.bbsZoneDAO.findAll();
    }
    
    public void delete(String id) {
        BbsZone bz=bbsZoneDAO.findById(id);
        this.logger.log("删除了版面"+bz.getName());
        this.bbsZoneDAO.delete(id);
        this.zones = this.bbsZoneDAO.findAll();
        
    }
}
