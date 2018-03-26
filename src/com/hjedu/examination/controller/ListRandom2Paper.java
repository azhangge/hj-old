package com.hjedu.examination.controller;

import java.io.Serializable;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.servlet.http.HttpServletRequest;

import com.hjedu.examination.dao.IExamPaperRandom2DAO;
import com.hjedu.examination.entity.random2.ExamPaperRandom2;
import com.hjedu.platform.service.ILogService;
import com.huajie.util.CookieUtils;
import com.huajie.util.JsfHelper;
import com.huajie.util.SpringHelper;

@ManagedBean
@ViewScoped
public class ListRandom2Paper implements Serializable {

    ILogService logger = SpringHelper.getSpringBean("LogService");
    IExamPaperRandom2DAO examDAO = SpringHelper.getSpringBean("ExamPaperRandom2DAO");
    //IExamModule2DAO moduleDAO = SpringHelper.getSpringBean("ExamModule2DAO");
    List<ExamPaperRandom2> exams;
    //ExamModuleModel module;

    public List<ExamPaperRandom2> getExams() {
        return exams;
    }

    public void setExams(List<ExamPaperRandom2> exams) {
        this.exams = exams;
    }


    @PostConstruct
    public void init() {
        HttpServletRequest request = JsfHelper.getRequest();
        String businessId = CookieUtils.getBusinessId(request);
        this.exams = this.examDAO.findAllExamPaperRandom2(businessId);
        //Collections.sort(exams);
        //Collections.reverse(exams);
    }

    public void delete(String id) {
        ExamPaperRandom2 ee = this.examDAO.findExamPaperRandom2(id);
        this.logger.log("删除了考试：" + ee.getName());
        this.examDAO.deleteExamPaperRandom2(id);
        this.exams = this.examDAO.findAllExamPaperRandom2(CookieUtils.getBusinessId(JsfHelper.getRequest()));
        //Collections.sort(exams);
        //Collections.reverse(exams);
    }
    
    
    public String editOrd(String id) {
        for (ExamPaperRandom2 cq : this.exams) {
            if (id.equals(cq.getId())) {
                this.examDAO.updateExamPaperRandom2(cq);
                break;
            }
        }
        return null;
    }
}
