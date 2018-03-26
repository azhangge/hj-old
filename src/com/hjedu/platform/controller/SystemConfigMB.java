package com.hjedu.platform.controller;

import java.io.Serializable;
import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;

import com.hjedu.platform.dao.ISystemConfigDAO;
import com.hjedu.platform.entity.SystemConfig;
import com.hjedu.platform.service.ILogService;
import com.huajie.util.CookieUtils;
import com.huajie.util.JsfHelper;
import com.huajie.util.SpringHelper;

@ManagedBean
@ViewScoped
public class SystemConfigMB  implements Serializable{
ILogService logger = SpringHelper.getSpringBean("LogService");
    SystemConfig sc;
    ISystemConfigDAO ts = SpringHelper.getSpringBean("SystemConfigDAO");
//    @ManagedProperty(value = "#{applicationBean}")
//    ApplicationBean ab;

//    public ApplicationBean getAb() {
//        return ab;
//    }
//
//    public void setAb(ApplicationBean ab) {
//        this.ab = ab;
//    }

    public SystemConfig getSc() {
        return sc;
    }

    public void setSc(SystemConfig sc) {
        this.sc = sc;
    }

    @PostConstruct
    public void init() {
    	String businessId = CookieUtils.getBusinessId(JsfHelper.getRequest());
        this.sc = ts.getSystemConfigByBusinessId(businessId);
    }

    public void updateTimesNum() {
    	this.sc.setBusinessId(CookieUtils.getBusinessId(JsfHelper.getRequest()));
    	if(sc.getSystemClosed()==2){
    		sc.setAutoCheck(false);
    	}else if(sc.getSystemClosed()==1){
    		sc.setAutoCheck(true);
    	}
        this.ts.updateSystemConfig(this.sc);
//        this.ab.setSystemConfig(this.sc);
        this.logger.log("进行了系统设置");
        JsfHelper.info("修改完成！");
    }

}
