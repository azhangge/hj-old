package com.hjedu.examination.controller;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

import com.hjedu.examination.dao.IExamCheatDAO;
import com.hjedu.examination.entity.ExamCheat;
import com.hjedu.platform.service.ILogService;
import com.huajie.util.SpringHelper;

@ManagedBean
@ViewScoped
public class ListExamCheat implements Serializable{

    ILogService logger = SpringHelper.getSpringBean("LogService");
    List<ExamCheat> examCheatList = new ArrayList();
    IExamCheatDAO examCheatDAO = SpringHelper.getSpringBean("ExamCheatDAO");

    public List<ExamCheat> getExamCheatList() {
        return examCheatList;
    }

    public void setExamCheatList(List<ExamCheat> examCheatList) {
        this.examCheatList = examCheatList;
    }

    

    @PostConstruct
    public void init() {

        synchronizeDB();
    }

    private void synchronizeDB() {
        this.examCheatList = examCheatDAO.findAllExamCheat();
    }

    public void deleteExamCheat(String id) {
        examCheatDAO.deleteExamCheat(id);
        synchronizeDB();
    }

    public void delAll() {
        examCheatDAO.deleteAll();
        this.synchronizeDB();
    }

    public void batchDel() {
        for (ExamCheat c : this.examCheatList) {
            if (c.isSelected()) {
                examCheatDAO.deleteExamCheat(c.getId());
            }
        }
        this.synchronizeDB();
    }
}
