/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.hjedu.examination.controller;

import java.io.Serializable;
import java.util.List;
import java.util.UUID;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.model.UploadedFile;

import com.hjedu.examination.dao.IFileQuestionDAO;
import com.huajie.util.SpringHelper;

@ManagedBean
@ViewScoped
public class ExamCaseFileUploader implements Serializable{

    String fileId;
    String examId;
    String fileName="";
    IFileQuestionDAO fileQuestionDAO = SpringHelper.getSpringBean("FileQuestionDAO");

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getExamId() {
        return examId;
    }

    public void setExamId(String examId) {
        this.examId = examId;
    }
    
    
    

    public String getFileId() {
        return fileId;
    }

    public void setFileId(String fileId) {
        this.fileId = fileId;
    }
    
    

    public String prepareToUpload(String fid,String examId) {
        this.fileId = fid;
        this.examId=examId;
        this.fileName="";
        return null;
    }

    public List up_action(FileUploadEvent event) {
        try {
            UploadedFile item = event.getFile();
            String str = "";
            Long tl = item.getSize();
            //this.project2.setRealLength(tl);
            Float k = 1024F;
            Float m = 1024 * 1024F;
            Float g = 1024 * 1024 * 1024F;
            if (tl < k) {
                str = tl.toString() + "B";
            } else if (k <= tl && tl < m) {
                Float te = Math.round(tl / k * 100) / 100.00F;
                str = te.toString() + "KB";
            } else if (tl >= m && tl < g) {
                Float te = Math.round(tl / m * 100) / 100.00F;
                str = te.toString() + "MB";
            } else {
                Float te = Math.round(tl / g * 100) / 100.00F;
                str = te.toString() + "GB";
            }
            //this.project2.setFileSize(str);
            String n = item.getFileName();
            int l1 = n.lastIndexOf("\\");
            int l2 = n.lastIndexOf(".");
            String ext = "." + n.substring(l2 + 1).toLowerCase();
            String name = n.substring(l1 + 1, l2);
            this.setFileName(name+ext);
            //HttpServletRequest request = JsfHelper.getRequest();
            String imgId = UUID.randomUUID().toString();
            if (this.fileId != null) {
                imgId = this.fileId;
            }

            this.fileQuestionDAO.saveFile(item.getInputstream(), imgId);
            return null;
        } catch (Exception ee) {
            ee.printStackTrace();
            return null;
        }
    }

}
