package com.hjedu.platform.controller;

import java.io.Serializable;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.servlet.http.HttpServletRequest;

import com.hjedu.examination.dao.IExamModule2DAO;
import com.hjedu.examination.dao.IExamPaperManualDAO;
import com.hjedu.examination.entity.ExamModuleModel;
import com.hjedu.examination.entity.ExamPaperManual;
import com.hjedu.platform.service.ILogService;
import com.huajie.exam.pool.ExamPaperPool;
import com.huajie.util.CookieUtils;
import com.huajie.util.JsfHelper;
import com.huajie.util.SpringHelper;
import com.sun.star.ucb.Cookie;

@ManagedBean
@ViewScoped
public class ListManualPaper implements Serializable {

    ILogService logger = SpringHelper.getSpringBean("LogService");
    IExamPaperManualDAO paperDAO = SpringHelper.getSpringBean("ExamPaperManualDAO");
    IExamModule2DAO moduleDAO = SpringHelper.getSpringBean("ExamModule2DAO");
    List<ExamPaperManual> papers;
    ExamModuleModel module;

    public List<ExamPaperManual> getPapers() {
        return papers;
    }

    public void setPapers(List<ExamPaperManual> papers) {
        this.papers = papers;
    }

    
    public ExamModuleModel getModule() {
        return module;
    }

    public void setModule(ExamModuleModel module) {
        this.module = module;
    }

    @PostConstruct
    public void init() {
    	String businessId = CookieUtils.getBusinessId(JsfHelper.getRequest());
        HttpServletRequest request = JsfHelper.getRequest();
        this.papers = this.paperDAO.findAllExamPaperManual(businessId);
    }

    public void delete(String id) {
        ExamPaperManual ee = this.paperDAO.findExamPaperManual(id);
        this.logger.log("删除了试卷：" + ee.getName());
        this.paperDAO.deleteExamPaperManual(id);
        this.papers = this.paperDAO.findAllExamPaperManual(CookieUtils.getBusinessId(JsfHelper.getRequest()));
        //ExamPaperPool.checkExamination();//更新试卷池中的考试
    }
    
    
    public String editOrd(String id) {
        for (ExamPaperManual cq : this.papers) {
            if (id.equals(cq.getId())) {
                this.paperDAO.updateExamPaperManual(cq);
                break;
            }
        }
        return null;
    }
    
}
