package com.hjedu.examination.controller;

import java.io.Serializable;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.servlet.http.HttpServletRequest;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import com.hjedu.examination.dao.IExamModule2DAO;
import com.hjedu.examination.dao.IExaminationDAO;
import com.hjedu.examination.entity.ExamModuleModel;
import com.hjedu.examination.entity.Examination;
import com.hjedu.platform.service.ILogService;
import com.huajie.exam.pool.ExamPaperPool;
import com.huajie.exam.thread.ExamCaseRanker;
import com.huajie.util.JsfHelper;
import com.huajie.util.SpringHelper;

@ManagedBean
@ViewScoped
public class ListExternalExamination implements Serializable {

    ILogService logger = SpringHelper.getSpringBean("LogService");
    IExaminationDAO examDAO = SpringHelper.getSpringBean("ExaminationDAO");
    IExamModule2DAO moduleDAO = SpringHelper.getSpringBean("ExamModule2DAO");
    List<Examination> exams;
    ExamModuleModel module;

    public List<Examination> getExams() {
        return exams;
    }

    public void setExams(List<Examination> exams) {
        this.exams = exams;
    }

    public ExamModuleModel getModule() {
        return module;
    }

    public void setModule(ExamModuleModel module) {
        this.module = module;
    }

    @PostConstruct
    public void init() {
        HttpServletRequest request = JsfHelper.getRequest();
        this.exams = this.examDAO.findAllExternalExamination();
        Collections.sort(exams);
        Collections.reverse(exams);
    }

    public void delete(String id) {
        Examination ee = this.examDAO.findExamination(id);
        this.logger.log("删除了考试：" + ee.getName());
        this.examDAO.deleteExamination(id);
        this.exams = this.examDAO.findAllExternalExamination();
        Collections.sort(exams);
        Collections.reverse(exams);
        ExamPaperPool.deleteExamination(id);//更新试卷池中的考试
    }
    
    
    public String editOrd(String id) {
        for (Examination cq : this.exams) {
            if (id.equals(cq.getId())) {
                this.examDAO.updateExamination(cq);
                break;
            }
        }
        return null;
    }
    
    public String rankExamCase(String id) {
        Examination ee = this.examDAO.findExamination(id);
        ee.setLastRankTime(new Date());
        this.examDAO.updateExamination(ee);
        ThreadPoolTaskExecutor exec = SpringHelper.getSpringBean("taskExecutor");
        exec.execute(new ExamCaseRanker(id));
        JsfHelper.info("手动生成排名信息的线程已经启动，请稍后在成绩管理中查看排名信息。");
        return null;
    }
    
    
}
