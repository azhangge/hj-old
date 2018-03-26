package com.hjedu.customer.controller;

import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

import com.hjedu.customer.dao.IBbsUserGradeDAO;
import com.hjedu.examination.entity.ExamModuleModel;
import com.hjedu.platform.entity.BbsUserGrade;
import com.hjedu.platform.service.ILogService;
import com.huajie.util.SpringHelper;

@ManagedBean
@ViewScoped
public class ListUserGrade implements Serializable {

    ILogService logger = SpringHelper.getSpringBean("LogService");
    IBbsUserGradeDAO gradeDAO = SpringHelper.getSpringBean("BbsUserGradeDAO");
    List<BbsUserGrade> grades = new LinkedList();

    public List<BbsUserGrade> getGrades() {
        return grades;
    }

    public void setGrades(List<BbsUserGrade> grades) {
        this.grades = grades;
    }
    
    
    
    @PostConstruct
    public void init() {
        this.grades = gradeDAO.findAllUserGrade();
    }
    
    public void delete(String id) {
        BbsUserGrade em = this.gradeDAO.findUserGrade(id);
        this.logger.log("删除了积分等级：" + em.getGradeName());
        this.gradeDAO.deleteUserGrade(id);
        this.grades = this.gradeDAO.findAllUserGrade();
    }
}
