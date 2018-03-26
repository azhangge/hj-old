/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.hjedu.examination.controller;

import java.util.ArrayList;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

import com.hjedu.examination.dao.IExaminationDAO;
import com.hjedu.examination.entity.Examination;
import com.huajie.util.SpringHelper;

@ManagedBean
@ViewScoped
public class ExternalList {

    IExaminationDAO examinationDAO = SpringHelper.getSpringBean("ExaminationDAO");
    List<Examination> externalList = new ArrayList();

    public List<Examination> getExternalList() {
        return externalList;
    }

    public void setExternalList(List<Examination> externalList) {
        this.externalList = externalList;
    }

    
    
    
    
    @PostConstruct
    public void init() {
        List<Examination> temp = this.examinationDAO.findAllExternalExamination();
        for (Examination exam : temp) {
            if (exam.getPaperType().equals("random2")) {
                externalList.add(exam);
            }
        }
    }

}
