package com.hjedu.examination.controller;


import java.io.Serializable;
import java.util.Collections;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.servlet.http.HttpServletRequest;

import com.hjedu.examination.dao.IExamContestDAO;
import com.hjedu.examination.dao.IExamModule2DAO;
import com.hjedu.examination.entity.ExamModuleModel;
import com.hjedu.examination.entity.contest.ExamContestSession;
import com.hjedu.platform.service.ILogService;
import com.huajie.exam.agent.ContestAgent;
import com.huajie.util.JsfHelper;
import com.huajie.util.SpringHelper;

@ManagedBean
@ViewScoped
public class ListExamContest implements Serializable {

    ILogService logger = SpringHelper.getSpringBean("LogService");
    IExamContestDAO examDAO = SpringHelper.getSpringBean("ExamContestDAO");
    IExamModule2DAO moduleDAO = SpringHelper.getSpringBean("ExamModule2DAO");
    List<ExamContestSession> exams;
    ExamModuleModel module;

    public List<ExamContestSession> getExams() {
        return exams;
    }

    public void setExams(List<ExamContestSession> exams) {
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
        this.exams = this.examDAO.findAllExamContest();
        Collections.sort(exams);
        Collections.reverse(exams);
    }

    public void delete(String id) {
        ExamContestSession ee = this.examDAO.findExamContest(id);
        this.logger.log("删除了竞赛：" + ee.getName());
        //将竞赛池中的竞赛同步删除，否则会造成后台已经删除，但前台未删除的情况，因为竞赛是加载在内存中的
        ContestAgent.removeContestList(id);
        this.examDAO.deleteExamContest(id);
        this.exams = this.examDAO.findAllExamContest();
        Collections.sort(exams);
        Collections.reverse(exams);
    }
    
    
    public String editOrd(String id) {
        for (ExamContestSession cq : this.exams) {
            if (id.equals(cq.getId())) {
                this.examDAO.updateExamContest(cq);
                break;
            }
        }
        return null;
    }
}
