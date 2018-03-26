package com.hjedu.customer.controller;

import java.util.ArrayList;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;

import com.hjedu.customer.dao.IBbsFileDAO;
import com.hjedu.platform.controller.ClientSession;
import com.hjedu.platform.dao.ComplexFileLogic;
import com.hjedu.platform.dao.IShareDAO;
import com.hjedu.platform.entity.BbsFileModel;
import com.huajie.util.JsfHelper;
import com.huajie.util.SpringHelper;

@ManagedBean
@ViewScoped
public class MyDeptFiles {

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
        this.grapaID=this.cxLogic.genComplexGrapaID(fatherID, files);
    }

    @PostConstruct
    public void init() {
        this.synDB();
        System.out.println("我分享到部门的文件数量共计：" + files.size());
        this.load();
    }

    public void load() {
        this.pro2s = cxLogic.buildQulifiedStructureByDic(fatherID, files);
        this.genGrapaID();
        this.familyTree = this.cxLogic.genComplexFamilyTree(this.fatherID,files);
    }

    public String reload(String fid) {
        if (fid != null) {
            this.fatherID = fid;
        }
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
        files = this.project2DAO.findAllDeptSharedClientFileByUsr(cs.getUsr().getId());
    }

    public String delDeptShare(String id) {
        if (id != null) {
            BbsFileModel cf = this.project2DAO.findClientFile(id);
            List<BbsFileModel> cfs = this.project2DAO.loadAllDescendants(id);
            cfs.add(cf);
            for (BbsFileModel c : cfs) {
                c.setScope("pers");
                this.project2DAO.updateClientFileInfo(c);
            }
            this.synDB();
            this.load();
        }
        ListClientFileByDept lcf = JsfHelper.getBean(FacesContext.getCurrentInstance(), "listClientFilebyDept");
        if (lcf != null) {
            lcf.init();
        }

        return null;
    }
}