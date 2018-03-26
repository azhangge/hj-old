package com.hjedu.platform.controller;


import java.io.Serializable;
import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

import com.hjedu.platform.dao.ISystemInfoDAO;
import com.hjedu.platform.entity.SystemInfo;
import com.hjedu.platform.service.ILogService;
import com.huajie.app.util.FileUtil;
import com.huajie.util.CookieUtils;
import com.huajie.util.JsfHelper;
import com.huajie.util.SpringHelper;

@ManagedBean
@ViewScoped
public class AASystemInfo implements Serializable {

    ILogService logger = SpringHelper.getSpringBean("LogService");
    private ISystemInfoDAO infoDAO = SpringHelper.getSpringBean("SystemInfoDAO");
    private SystemInfo info;
    private String businessId;

    public SystemInfo getInfo() {
        return info;
    }

    public void setInfo(SystemInfo info) {
        this.info = info;
    }
    
    public String getBusinessId() {
        return businessId;
    }

    public void setBusinessId(String businessId) {
        this.businessId = businessId;
    }

    @PostConstruct
    public void init() {
    	this.setBusinessId( CookieUtils.getBusinessId(JsfHelper.getRequest()));
        //this.info = this.infoDAO.findSystemInfo();
        this.info = this.infoDAO.findSystemInfoByBusinessId(this.getBusinessId());
    }

    public String apply() {
    	info.setBusinessId(this.businessId);
        this.infoDAO.updateSystemInfo(info);
        ApplicationBean ab=JsfHelper.getBean("applicationBean");
        ab.setInfo(info);
        FileUtil.setUrl(info.getUrl());
        FileUtil.setWelcomePage(info.getWelcomePage());
        JsfHelper.info("应用新的系统信息成功！");
        this.logger.log("应用新的系统信息");
        return null;
    }
}
