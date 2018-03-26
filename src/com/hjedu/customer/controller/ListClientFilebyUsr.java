package com.hjedu.customer.controller;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;

import com.hjedu.customer.dao.IBbsFileDAO;
import com.hjedu.platform.controller.ChangeFileDir;
import com.hjedu.platform.controller.ClientSession;
import com.hjedu.platform.dao.ComplexFileLogic;
import com.hjedu.platform.entity.BbsFileModel;
import com.huajie.util.JsfHelper;
import com.huajie.util.SpringHelper;

@ManagedBean
@ViewScoped
public class ListClientFilebyUsr implements Serializable{
    
    //ShareDAO shareDAO = SpringHelper.getSpringBean("shareDAO");
    IBbsFileDAO project2DAO = SpringHelper.getSpringBean("BbsFileDAO");
    ComplexFileLogic complex = SpringHelper.getSpringBean("ComplexFileLogic");
    List<BbsFileModel> pro2s;
    String uid = "1";
    String fatherID = "0";
    String grapaID = "0";
    String folderName = "00";
    String temp = "";
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
    
    public void synDB() {
        ClientSession cs = (ClientSession) JsfHelper.getBean("clientSession");
        this.pro2s = this.project2DAO.findAllClientFileByUsr(cs.getUsr().getId(), this.fatherID);
       
    }
    
    private void genGrapaID() {
        if (!this.fatherID.equals("0")) {
            String g = this.project2DAO.findClientFile(this.fatherID).getFatherID();
            this.grapaID = g;
        }
    }
    
    @PostConstruct
    public void init() {
        HttpServletRequest request = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
        String fid = request.getParameter("fid");
        if (fid != null) {
            this.fatherID = fid;
        }
        synDB();
        this.genGrapaID();
        this.familyTree = complex.genFamilyTree(this.fatherID);
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
    
    public String delPro(String id) {
        if (id != null) {
            this.project2DAO.delClientFileAndAllDescendants(id);
//            BbsFileModel temp = this.project2DAO.findClientFile(idt);
//            String f = temp.getFatherID();
//            this.project2DAO.delClientFile(idt);

            // 如果目录被删除，其子目录与文件将直接移动到上级目录
//            List<BbsFileModel> fs = this.project2DAO.findAllSonClientFile(temp.getId());
//            for (BbsFileModel c : fs) {
//                c.setFatherID(f);
//                this.project2DAO.updateClientFileInfo(c);
//            }
        }
        synDB();
        return null;
    }
    
    public String batDel() {
        for (BbsFileModel cf : this.pro2s) {
            if (cf.isSelected()) {
                this.project2DAO.delClientFileAndAllDescendants(cf.getId());
//                BbsFileModel temp = this.project2DAO.findClientFile(cf.getId());
//                String f = temp.getFatherID();
//                this.project2DAO.delClientFile(cf.getId());

                // 如果目录被删除，其子目录与文件将直接移动到上级目录
//                List<BbsFileModel> fs = this.project2DAO.findAllSonClientFile(temp.getId());
//                for (BbsFileModel c : fs) {
//                    c.setFatherID(f);
//                    this.project2DAO.updateClientFileInfo(c);
//                }
            }
        }
        synDB();
        return null;
    }
    
    public String batChangeDir() {
        List<BbsFileModel> files = new ArrayList();
        for (BbsFileModel cf : this.pro2s) {
            if (cf.isSelected()) {
                files.add(cf);
            }
        }
        ChangeFileDir cfd = (ChangeFileDir) JsfHelper.getBean(FacesContext.getCurrentInstance(), "changeFileDir");
        cfd.change(files);
        return null;
    }
}
