package com.hjedu.platform.controller;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;

import com.hjedu.customer.dao.IBbsFileDAO;
import com.hjedu.platform.dao.ComplexFileLogic;
import com.hjedu.platform.entity.BbsFileModel;
import com.huajie.util.SpringHelper;

@ManagedBean
@ViewScoped
public class SearchAdminFile {

    IBbsFileDAO project2DAO = SpringHelper.getSpringBean("BbsFileDAO");
    ComplexFileLogic complex = SpringHelper.getSpringBean("ComplexFileLogic");
    List<BbsFileModel> pro2s;
    String str;
    String field;
    Date fromDate = new Date(0);
    Date toDate = new Date();
    List<SelectItem> ss = new ArrayList();

    public List<SelectItem> getSs() {
        return ss;
    }

    public void setSs(List<SelectItem> ss) {
        this.ss = ss;
    }

    public Date getFromDate() {
        return fromDate;
    }

    public void setFromDate(Date fromDate) {
        this.fromDate = fromDate;
    }

    public Date getToDate() {
        return toDate;
    }

    public void setToDate(Date toDate) {
        this.toDate = toDate;
    }

    public SearchAdminFile() {

        ss.add(new SelectItem("fileName", "文件名"));
        ss.add(new SelectItem("fileExt", "扩展名"));
        ss.add(new SelectItem("user", "上传者"));
    }

    public String getStr() {
        return str;
    }

    public void setStr(String str) {
        this.str = str;
    }

    public String getField() {
        return field;
    }

    public void setField(String field) {
        this.field = field;
    }

    public List<BbsFileModel> getPro2s() {
        return this.pro2s;
    }

    public void setPro2s(List<BbsFileModel> pro2s) {
        this.pro2s = pro2s;
    }

    public String search() {
        if (this.str.trim().equals("")) {
            FacesMessage fm = new FacesMessage();
            fm.setSeverity(FacesMessage.SEVERITY_ERROR);
            fm.setSummary("请输入检索词！");
            FacesContext.getCurrentInstance().addMessage("", fm);
            return null;
        }

        List<BbsFileModel> ps1 = this.project2DAO.findAllClientFile();
        List<BbsFileModel> ps = new ArrayList();
        for (BbsFileModel f : ps1) {
            if (f.getIfFolder()) {
                continue;
            }
            if (this.fromDate.getTime() <= f.getUploadTime().getTime()
                    && f.getUploadTime().getTime() <= this.toDate.getTime()) {
                if (this.field.equals("fileName")) {
                    if (f.getFileName() != null) {
                        if (f.getFileName().toLowerCase().contains(this.str.toLowerCase())) {
                            ps.add(f);
                        }
                    }
                }
                if (this.field.equals("fileAbstract")) {
                    if (f.getFileAbstract() != null) {
                        if (f.getFileAbstract().toLowerCase().contains(this.str.toLowerCase())) {
                            ps.add(f);
                        }
                    }
                }
                if (this.field.equals("fileExt")) {
                    if (f.getFileExt() != null) {
                        if (f.getFileExt().toLowerCase().contains(this.str.toLowerCase())) {
                            ps.add(f);
                        }
                    }
                }
                if (this.field.equals("user")) {
                    if (f.getUser().getName() != null) {
                        if (f.getUser().getName().toLowerCase().contains(this.str.toLowerCase())) {
                            ps.add(f);
                        }
                    }
                }
            }
        }
        for (BbsFileModel f1 : ps) {
            f1.setFamilyTree(this.complex.genFamilyTree(f1.getFatherID()));
        }
        this.setPro2s(ps);
        return null;
    }
}
