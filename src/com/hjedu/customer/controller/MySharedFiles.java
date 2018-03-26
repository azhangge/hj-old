package com.hjedu.customer.controller;


import java.util.ArrayList;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;

import com.hjedu.customer.dao.IBbsFileDAO;
import com.hjedu.platform.controller.ClientSession;
import com.hjedu.platform.dao.ComplexFileLogic;
import com.hjedu.platform.dao.IShareDAO;
import com.hjedu.platform.entity.BbsFileModel;
import com.huajie.util.JsfHelper;
import com.huajie.util.SpringHelper;

@ManagedBean
@ViewScoped
public class MySharedFiles  {

    
    IShareDAO shareDAO = SpringHelper.getSpringBean("ShareDAO");
    IBbsFileDAO project2DAO = SpringHelper.getSpringBean("BbsFileDAO");
    List<BbsFileModel> pro2s;
    
    ClientSession cs=JsfHelper.getBean("clientSession");
    
    List<BbsFileModel> files;
    String uid = "1";
    String fatherID = "0";
    String grapaID = "0";
    String folderName = "00";
    String temp = "";
    ComplexFileLogic cxLogic = SpringHelper.getSpringBean("ComplexFileLogic");
    List<BbsFileModel> familyTree = new ArrayList();

    public List<BbsFileModel> getFamilyTree() {
        return familyTree;
    }

    public void setFamilyTree(List<BbsFileModel> familyTree) {
        this.familyTree = familyTree;
    }

    public ClientSession getCs() {
        return cs;
    }

    public void setCs(ClientSession cs) {
        this.cs = cs;
    }

    public List<BbsFileModel> getPro2s() {
        return this.pro2s;
    }

    public void setPro2s(List<BbsFileModel> pro2s) {
        this.pro2s = pro2s;
    }

    private void genGrapaID() {
        this.grapaID = this.cxLogic.genComplexGrapaID(fatherID, files);
    }

    @PostConstruct
    public void init() {
        this.synDB();
        System.out.println("我分享的文件共计数量：" + files.size());
        this.load();
    }

    public void load() {
        HttpServletRequest request = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
        String fid = request.getParameter("fid");
        if (fid != null) {
            this.fatherID = fid;
        }
        this.pro2s = cxLogic.buildQulifiedStructureByDic(fatherID, files);
        this.genGrapaID();
        this.familyTree = this.cxLogic.genComplexFamilyTree(this.fatherID, files);
    }

    public String reload() {
        this.load();
        return null;
    }

    public String getFatherID() {
        return fatherID;
    }

    public void setFatherID(String fatherID) {
        this.fatherID = fatherID;
    }

    public String getFolderName() {
        return folderName;
    }

    public void setFolderName(String folderName) {
        this.folderName = folderName;
    }

    public String getGrapaID() {
        return grapaID;
    }

    public void setGrapaID(String grapaID) {
        this.grapaID = grapaID;
    }

    
    public void synDB() {
        files = this.project2DAO.findAllSharedClientFileByUsr(cs.getUsr().getId());
    }

    public String delAllShare(String id) {
        if (id != null) {
            this.shareDAO.updateFileShareUser(id, new ArrayList());
        }
        this.synDB();
        this.load();

        ListSharedClientFile lcf = JsfHelper.getBean(FacesContext.getCurrentInstance(), "listSharedClientFile");
        if (lcf != null) {
            lcf.init();
        }

        return null;
    }
}