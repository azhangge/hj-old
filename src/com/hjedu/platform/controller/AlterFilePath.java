package com.hjedu.platform.controller;

import java.io.Serializable;
import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;

import com.hjedu.platform.dao.ISystemConfigDAO;
import com.hjedu.platform.entity.SystemConfig;
import com.huajie.util.JsfHelper;
import com.huajie.util.SpringHelper;

@ManagedBean
@ViewScoped
public class AlterFilePath implements Serializable{

    SystemConfig scm;
    ISystemConfigDAO systemConfigDAO = SpringHelper.getSpringBean("SystemConfigDAO");

    public SystemConfig getScm() {
        return scm;
    }

    public void setScm(SystemConfig scm) {
        this.scm = scm;
    }

    @PostConstruct
    public void init() {
        this.setScm(this.systemConfigDAO.getSystemConfig());
    }

    public String finish() {
        this.systemConfigDAO.updateSystemConfig(this.getScm());
        ApplicationBean ab = (ApplicationBean) JsfHelper.getBean("applicationBean");
        ab.loadFilePath();
        FacesMessage fm = new FacesMessage();
        fm.setSeverity(FacesMessage.SEVERITY_INFO);
        fm.setSummary("修改完成！");
        FacesContext.getCurrentInstance().addMessage("", fm);
        return null;
    }
}
