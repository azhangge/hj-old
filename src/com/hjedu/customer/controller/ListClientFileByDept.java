package com.hjedu.customer.controller;


import java.util.ArrayList;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

import com.hjedu.customer.dao.IBbsFileDAO;
import com.hjedu.platform.controller.ClientSession;
import com.hjedu.platform.dao.ComplexFileLogic;
import com.hjedu.platform.entity.BbsFileModel;
import com.huajie.util.JsfHelper;
import com.huajie.util.SpringHelper;

@ManagedBean
@ViewScoped
public class ListClientFileByDept  {

    
    IBbsFileDAO project2DAO = SpringHelper.getSpringBean("BbsFileDAO");
    List<BbsFileModel> pro2s=new ArrayList();
    List<BbsFileModel> files;
    String uid = "1";
    String fatherID = "0";
    String grapaID = "0";
    String temp = "";
    String folderName = "00";
    ComplexFileLogic cxLogic = SpringHelper.getSpringBean("ComplexFileLogic");
    List<BbsFileModel> familyTree = new ArrayList();

    public List<BbsFileModel> getFamilyTree() {
        return familyTree;
    }

    public void setFamilyTree(List<BbsFileModel> familyTree) {
        this.familyTree = familyTree;
    }

    public List<BbsFileModel> getPro2s() {
        return this.pro2s;
    }

    public void setPro2s(List<BbsFileModel> pro2s) {
        this.pro2s = pro2s;
    }

    public String getFatherID() {
        return fatherID;
    }

    public void setFatherID(String fatherID) {
        this.fatherID = fatherID;
    }

    public String getGrapaID() {
        return grapaID;
    }

    public void setGrapaID(String grapaID) {
        this.grapaID = grapaID;
    }

    public String getFolderName() {
        return folderName;
    }

    public void setFolderName(String folderName) {
        this.folderName = folderName;
    }

    @PostConstruct
    public void init() {
        ClientSession cs = JsfHelper.getBean("clientSession");
        String dept = cs.getUsr().getGroupStr();
        this.files = this.project2DAO.findAllAccessableClientFileByDept(dept, "0");
        System.out.println("部门文件共计数量：" + files.size());

        this.load();
    }

    public String load() {
        pro2s.clear();
        pro2s.addAll(cxLogic.buildQulifiedStructureByDic(this.fatherID, files));
        this.genGrapaID();
        this.familyTree = this.cxLogic.genComplexFamilyTree(this.fatherID, files);
        //System.out.println("数据装载完成。" );
        return null;
    }

    public String reload(String fid) {
        if (fid != null) {
            this.fatherID = fid;
        }
        this.load();
        return null;
    }

    public void synDB() {
    }

    private void genGrapaID() {
        this.grapaID = this.cxLogic.genComplexGrapaID(fatherID, files);
    }
}