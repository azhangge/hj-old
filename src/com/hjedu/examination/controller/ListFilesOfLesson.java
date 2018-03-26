package com.hjedu.examination.controller;


import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;
import javax.servlet.http.HttpServletRequest;

import com.hjedu.customer.controller.AdminSessionBean;
import com.hjedu.customer.dao.IAdminDAO;
import com.hjedu.customer.dao.IBbsFileDAO;
import com.hjedu.customer.entity.AdminInfo;
import com.hjedu.platform.controller.ChangeFileDir;
import com.hjedu.platform.entity.BbsFileModel;
import com.hjedu.platform.service.ILogService;
import com.huajie.util.JsfHelper;
import com.huajie.util.SpringHelper;

@ManagedBean
@ViewScoped
public class ListFilesOfLesson {

    
    IBbsFileDAO proDAO = SpringHelper.getSpringBean("BbsFileDAO");
    ILogService logger = SpringHelper.getSpringBean("LogService");
    List<BbsFileModel> pro1s;
    String searchStr = "";
    String field = "";
    String uid = "1";
    String fatherID = "0";
    String grapaID = "0";
    String folderName = "00";
    String temp = "";
    List<SelectItem> searchFields = new ArrayList();

    public String getField() {
        return field;
    }

    public void setField(String field) {
        this.field = field;
    }

    public List<SelectItem> getSearchFields() {
        return searchFields;
    }

    public void setSearchFields(List<SelectItem> searchFields) {
        this.searchFields = searchFields;
    }

    public String getSearchStr() {
        return searchStr;
    }

    public void setSearchStr(String searchStr) {
        this.searchStr = searchStr;
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

    public List<BbsFileModel> getPro1s() {
        return pro1s;
    }

    public void setPro1s(List<BbsFileModel> pro1ss) {
        this.pro1s = pro1ss;
        FacesContext fc = FacesContext.getCurrentInstance();
        AdminSessionBean ab = JsfHelper.getBean("adminSessionBean");
        List t = new ArrayList();
        for (BbsFileModel p : pro1ss) {
            t.add(p);
        }
        System.out.println(t.toString());
        System.out.println("Pro2 value changed!");
    }

    @PostConstruct
    public void init() {
        loadSelects();
        HttpServletRequest request = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
        String fid = request.getParameter("id");
        if (fid != null) {
            this.fatherID = fid;
        }
        synDB();
//        this.genGrapaID();
    }

    private void loadSelects() {
        this.searchFields.add(new SelectItem("fileName", "文件名称"));
        this.searchFields.add(new SelectItem("uploader", "文件上传人"));
        this.searchFields.add(new SelectItem("dept", "部门"));
    }

    public void synDB() {
        this.pro1s = this.proDAO.findAllFilesOfLesson(
                this.fatherID);

    }

    private void genGrapaID() {
        if (!this.fatherID.equals("0")) {
            String g = this.proDAO.findClientFile(this.fatherID).getFatherID();
            this.grapaID = g;
        }
    }

    public String delPro() {
        HttpServletRequest request = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
        String idt = request.getParameter("id");
        if (idt != null) {
            this.proDAO.delClientFile(idt);
        }
        this.pro1s = this.proDAO.findAllClientFile();
        return "null";
    }
    
    public String batDel() {
        for (BbsFileModel cf : this.pro1s) {
            if (cf.isSelected()) {
            	String owner = "";
            	if(cf.getUser()!=null){
            		owner = "用户‘"+ cf.getUser().getName();
            	}else{
            		IAdminDAO ad = SpringHelper.getSpringBean("AdminDAO");
            		AdminInfo admin = ad.findAdmin(cf.getAdminId());
            		if(admin!=null){
            			owner = "管理员‘"+ admin.getNickname();
            		}
            	}
                this.logger.log("删除了" + owner +"’的文件："+cf.getFileName());
                this.proDAO.delClientFileAndAllDescendants(cf.getId());
            }
        }
        synDB();
        return null;
    }
    
    public String batChangeDir() {
        List<BbsFileModel> files = new ArrayList();
        for (BbsFileModel cf : this.pro1s) {
            if (cf.isSelected()) {
                files.add(cf);
            }
        }
        ChangeFileDir cfd = (ChangeFileDir) JsfHelper.getBean("changeFileDir");
        cfd.change(files);
        return null;
    }

    public String search() {
        String s = this.searchStr;
        System.out.println("Field:" + this.field + ",KeyWords:" + s);
        setPro1s(this.proDAO.searchClientFile(this.field, this.searchStr));
        return null;
    }
}