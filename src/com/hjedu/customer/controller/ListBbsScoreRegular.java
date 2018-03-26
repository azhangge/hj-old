package com.hjedu.customer.controller;

import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;

import com.hjedu.platform.dao.IBbsScoreRegularDAO;
import com.hjedu.platform.entity.BbsScoreRegular;
import com.huajie.util.SpringHelper;

@ManagedBean
@RequestScoped
public class ListBbsScoreRegular implements Serializable {

    IBbsScoreRegularDAO moduleDAO = SpringHelper.getSpringBean("BbsScoreRegularDAO");
    List<BbsScoreRegular> modules = new LinkedList();

    public List<BbsScoreRegular> getModules() {
        return modules;
    }

    public void setModules(List<BbsScoreRegular> modules) {
        this.modules = modules;
    }
    
    
    
    @PostConstruct
    public void init() {
        this.modules.add(this.moduleDAO.findScoreRegular());
    }
    
    
}
