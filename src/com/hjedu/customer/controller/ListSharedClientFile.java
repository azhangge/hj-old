package com.hjedu.customer.controller;

import java.util.ArrayList;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
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
public class ListSharedClientFile {

    IShareDAO shareDAO = SpringHelper.getSpringBean("ShareDAO");
    IBbsFileDAO project2DAO = SpringHelper.getSpringBean("BbsFileDAO");
    List<BbsFileModel> files;
    List<BbsFileModel> pro2s;
    String uid = "1";
    String fatherID = "0";
    String grapaID = "0";
    String temp = "";
    String folderName = "00";
    ComplexFileLogic cxLogic = SpringHelper.getSpringBean("ComplexFileLogic");
    List<BbsFileModel> familyTree = new ArrayList();

    public List<BbsFileModel> getPro2s() {
        return this.pro2s;
    }

    public void setPro2s(List<BbsFileModel> pro2s) {
        this.pro2s = pro2s;
    }

    public List<BbsFileModel> getFamilyTree() {
        return familyTree;
    }

    public void setFamilyTree(List<BbsFileModel> familyTree) {
        this.familyTree = familyTree;
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

    public String getTemp() {
        return temp;
    }

    public void setTemp(String temp) {
        this.temp = temp;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    @PostConstruct
    public void init() {
        ClientSession cs = JsfHelper.getBean("clientSession");
        this.files = this.shareDAO.findSharedBbsFileByUsr(cs.getUsr().getId());
        if (!files.isEmpty()) {
            System.out.println("共享文件数量为：" + files.size());
        }
        this.load();
    }

    public void load() {//主要作用是构建目录结构和当前路径
        this.pro2s = cxLogic.buildQulifiedStructureByDic(this.fatherID, files);
        this.genGrapaID();
        this.familyTree = this.cxLogic.genComplexFamilyTree(this.fatherID, files);
    }

    public String reload() {
        //主要作用是获取参数，更改BEAN状态，再次调用目录结构
        HttpServletRequest request = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
        String fid = request.getParameter("fid");
        if (fid != null) {
            this.fatherID = fid;
        }
        this.load();
        return null;
    }

    public void synDB() {
    }

    private void genGrapaID() {
        this.grapaID=this.cxLogic.genComplexGrapaID(fatherID, files);
    }
}
