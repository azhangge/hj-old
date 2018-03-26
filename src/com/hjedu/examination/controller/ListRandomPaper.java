package com.hjedu.examination.controller;

import java.io.Serializable;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.servlet.http.HttpServletRequest;

import com.hjedu.customer.controller.AdminSessionBean;
import com.hjedu.examination.dao.IExamModule2DAO;
import com.hjedu.examination.dao.IExamPaperRandomDAO;
import com.hjedu.examination.entity.ExamModuleModel;
import com.hjedu.examination.entity.ExamPaperRandom;
import com.hjedu.platform.service.ILogService;
import com.huajie.exam.pool.ExamPaperPool;
import com.huajie.util.CookieUtils;
import com.huajie.util.JsfHelper;
import com.huajie.util.SpringHelper;

@ManagedBean
@ViewScoped
public class ListRandomPaper implements Serializable {

    ILogService logger = SpringHelper.getSpringBean("LogService");
    IExamPaperRandomDAO paperDAO = SpringHelper.getSpringBean("ExamPaperRandomDAO");
    IExamModule2DAO moduleDAO = SpringHelper.getSpringBean("ExamModule2DAO");
    List<ExamPaperRandom> papers;
    ExamModuleModel module;

    public List<ExamPaperRandom> getPapers() {
        return papers;
    }

    public void setPapers(List<ExamPaperRandom> papers) {
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
    	AdminSessionBean asb = JsfHelper.getBean("adminSessionBean");
    	if(asb.isIfSuper()){
    		this.papers = this.paperDAO.findAllExamPaperRandom(businessId);
    	}else{
    		this.papers = this.paperDAO.findAllExamPaperRandomByAdmin(businessId);
    	}
    }

    public void delete(String id) {
        ExamPaperRandom ee = this.paperDAO.findExamPaperRandom(id);
        this.logger.log("删除了试卷：" + ee.getName());
        this.paperDAO.deleteExamPaperRandom(id);
        this.papers = this.paperDAO.findAllExamPaperRandom(CookieUtils.getBusinessId(JsfHelper.getRequest()));
        //ExamPaperPool.checkExamination();//更新试卷池中的考试
    }
    
    
    public String editOrd(String id) {
        for (ExamPaperRandom cq : this.papers) {
            if (id.equals(cq.getId())) {
                this.paperDAO.updateExamPaperRandom(cq);
                break;
            }
        }
        return null;
    }
}
