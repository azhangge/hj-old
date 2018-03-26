package com.hjedu.examination.controller;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.component.UIComponent;
import javax.faces.component.UIData;
import javax.faces.event.ActionEvent;

import com.hjedu.platform.dao.ISystemEmailBoxDAO;
import com.hjedu.platform.entity.SystemEmailBoxModel;
import com.hjedu.platform.service.ILogService;
import com.huajie.util.SpringHelper;

@ManagedBean
@ViewScoped
public class EditSystemEmailBox implements Serializable {

    ILogService logger = SpringHelper.getSpringBean("LogService");
    ISystemEmailBoxDAO systemEmailBoxDAO = SpringHelper.getSpringBean("SystemEmailBoxDAO");
    List<SystemEmailBoxModel> emailboxes = new ArrayList();
    SystemEmailBoxModel eb = new SystemEmailBoxModel();

    public SystemEmailBoxModel getEb() {
        return eb;
    }

    public void setEb(SystemEmailBoxModel eb) {
        this.eb = eb;
    }

    public List<SystemEmailBoxModel> getEmailboxes() {
        return emailboxes;
    }

    public void setEmailboxes(List<SystemEmailBoxModel> emailboxes) {
        this.emailboxes = emailboxes;
    }

    @PostConstruct
    public void init() {
        this.emailboxes = this.systemEmailBoxDAO.findAllEmailBox();
    }

    public void delEmailBox(ActionEvent ee) {

        UIComponent ui = ee.getComponent();
        SystemEmailBoxModel nm = null;
        while ((ui != null) && (!(ui instanceof UIData))) {
            ui = ui.getParent();
        }
        if ((ui != null) && (ui instanceof UIData)) {
            Object rowData = ((UIData) ui).getRowData();
            if (rowData instanceof SystemEmailBoxModel) {
                nm = (SystemEmailBoxModel) rowData;
            }
        }
        this.logger.log("删除了系统邮箱" + nm.getAddress());
        this.emailboxes.remove(nm);
    }

    public void addItem(ActionEvent ee) {
        this.emailboxes.add(this.eb);
        this.logger.log("添加了系统邮箱" + eb.getAddress());
        this.eb = new SystemEmailBoxModel();

    }

    public String finish() {
        this.logger.log("修改系统邮箱完成");
        this.systemEmailBoxDAO.synEmailBox(this.emailboxes);
        return "SystemConfig?faces-redirect=true";
    }
}
