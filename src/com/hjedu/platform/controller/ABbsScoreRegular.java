package com.hjedu.platform.controller;

import java.io.Serializable;
import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

import com.hjedu.platform.dao.IBbsScoreRegularDAO;
import com.hjedu.platform.entity.BbsScoreRegular;
import com.hjedu.platform.service.ILogService;
import com.huajie.util.SpringHelper;

@ManagedBean
@ViewScoped
public class ABbsScoreRegular implements Serializable {

    ILogService logger = SpringHelper.getSpringBean("LogService");
    IBbsScoreRegularDAO moduleDAO = SpringHelper.getSpringBean("BbsScoreRegularDAO");
    BbsScoreRegular module = null;

    public BbsScoreRegular getModule() {
        return module;
    }

    public void setModule(BbsScoreRegular module) {
        this.module = module;
    }

    @PostConstruct
    public void init() {
        this.module = this.moduleDAO.findScoreRegular();

    }

    public String done() {
        this.logger.log("修改了积分规则");
        this.moduleDAO.updateScoreRegular(module);
        return "ListBbsScoreRegular?faces-redirect=true";
    }
}
