package com.hjedu.platform.controller;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;

import com.hjedu.platform.dao.IManualDAO;
import com.hjedu.platform.entity.ManualModel;
import com.huajie.util.JsfHelper;
import com.huajie.util.SpringHelper;
import com.huajie.util.XmlManualUtils;

@ManagedBean
@ViewScoped
public class ListManual implements Serializable {

    List<ManualModel> manualList = new ArrayList();
    List<ManualModel> filteredList = new ArrayList();
    IManualDAO manualDAO = SpringHelper.getSpringBean("ManualDAO");
    ManualModel manual;
    private String manualPath="resources/manuals";

    boolean ifAlter = true;

    public List<ManualModel> getManualList() {
        return this.manualList;
    }

    public void setManualList(List<ManualModel> manualList) {
        this.manualList = manualList;
    }

    public List<ManualModel> getFilteredList() {
        return filteredList;
    }

    public void setFilteredList(List<ManualModel> filteredList) {
        this.filteredList = filteredList;
    }

    public boolean isIfAlter() {
        return ifAlter;
    }

    public void setIfAlter(boolean ifAlter) {
        this.ifAlter = ifAlter;
    }

    public ManualModel getManual() {
        return manual;
    }

    public void setManual(ManualModel manual) {
        this.manual = manual;
    }

    @PostConstruct
    public void init() {
        //加载文档文件所在的目录路径
        manualPath=FacesContext.getCurrentInstance().getExternalContext().getRealPath(manualPath);
        
        String act = JsfHelper.getRequest().getParameter("action");
        if (act != null) {
            this.ifAlter = true;
        }
        synchronizeDB();
    }

    private void synchronizeDB() {
        if (ifAlter) {
            this.manualList = this.manualDAO.findAllManual();
        } else {
            this.manualList=XmlManualUtils.parseDirectoryfromXML(manualPath);
        }
    }

    public void deleteManual(String idt) {
        if (idt != null) {
            this.manualDAO.deleteManual(idt);
        }
        synchronizeDB();
    }

    public String viewManual(String id) {
        //manual = this.manualDAO.findManual(id);
        manual=XmlManualUtils.parseManualfromXML(manualPath, id);
        return null;
    }

    public String editOrd(String id) {
        for (ManualModel cq : this.manualList) {
            if (id.equals(cq.getId())) {
                this.manualDAO.updateManual(cq);
                break;
            }
        }
        return null;
    }
    
    public String outputAllManual() {
        List<ManualModel> mms = this.manualDAO.findAllManual();
        XmlManualUtils.writeAllManualToXML(manualPath, mms);
        JsfHelper.info("All manuals have been outputed.");
        return null;
    }
    
}
