package com.hjedu.platform.controller;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.faces.model.SelectItem;

import com.hjedu.customer.dao.IBbsFileDAO;
import com.hjedu.platform.dao.ComplexFileLogic;
import com.hjedu.platform.entity.BbsFileModel;
import com.huajie.util.SpringHelper;

public abstract class NewFileAbstract  implements Serializable{

    BbsFileModel project2 = new BbsFileModel();
    //IBbsFileDAO project2DAO;
    IBbsFileDAO project2DAO = SpringHelper.getSpringBean("BbsFileDAO");
    String fatherID = "0";
    String idt = "0";
    boolean flag = false;// 为true时表示修改，为false时表示添加
    ComplexFileLogic cxLogic = SpringHelper.getSpringBean("ComplexFileLogic");
    List<SelectItem> fts = new ArrayList();
    boolean rename = false;
    String fileName = "";
    String errStr = "";
    String scope = "pers";
    String oldName = "";
    String fnTemp = "";

    public String getScope() {
        return scope;
    }

    public void setScope(String scope) {
        this.scope = scope;
    }

    public String getErrStr() {
        return errStr;
    }

    public void setErrStr(String errStr) {
        this.errStr = errStr;
    }

    public boolean isRename() {
        return rename;
    }

    public void setRename(boolean rename) {
        this.rename = rename;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public abstract void init();

    public abstract void newAdd();

    public abstract void alter();

    public abstract void checkName();

    public void share() {
        this.scope = "dept";
    }

    public void deShare() {
        this.scope = "pers";
    }

    public BbsFileModel getProject2() {
        return project2;
    }

    public void setProject2(BbsFileModel project2) {
        this.project2 = project2;
    }

    public List<SelectItem> getFts() {
        return fts;
    }

    public void setFts(List<SelectItem> fts) {
        this.fts = fts;
    }

    public String getFatherID() {
        return fatherID;
    }

    public void setFatherID(String fatherID) {
        this.fatherID = fatherID;
    }
}
