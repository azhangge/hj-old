package com.hjedu.examination.controller;

import java.io.Serializable;
import java.util.Collections;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.servlet.http.HttpServletRequest;

import com.hjedu.examination.dao.ICaseQuestionDAO;
import com.hjedu.examination.dao.IExamModule2DAO;
import com.hjedu.examination.entity.CaseQuestion;
import com.hjedu.examination.entity.ChoiceQuestion;
import com.hjedu.examination.entity.ExamModuleModel;
import com.hjedu.examination.entity.GeneralQuestion;
import com.hjedu.platform.controller.BasketSession;
import com.hjedu.platform.service.ILogService;
import com.huajie.exam.pool.ExamPaperPool;
import com.huajie.util.CookieUtils;
import com.huajie.util.JsfHelper;
import com.huajie.util.SpringHelper;

@ManagedBean
@ViewScoped
public class ListCaseQuestion implements Serializable {

    ILogService logger = SpringHelper.getSpringBean("LogService");
    ICaseQuestionDAO questionDAO = SpringHelper.getSpringBean("CaseQuestionDAO");
    IExamModule2DAO moduleDAO = SpringHelper.getSpringBean("ExamModule2DAO");
    List<CaseQuestion> questions;
    ExamModuleModel module;
    List<ExamModuleModel> targets;
    ExamModuleModel target;
    
    public List<CaseQuestion> getQuestions() {
        return questions;
    }
    
    public void setQuestions(List<CaseQuestion> questions) {
        this.questions = questions;
    }
    
    public ExamModuleModel getModule() {
        return module;
    }
    
    public void setModule(ExamModuleModel module) {
        this.module = module;
    }
    
    public ExamModuleModel getTarget() {
        return target;
    }
    
    public void setTarget(ExamModuleModel target) {
        this.target = target;
    }
    
    public List<ExamModuleModel> getTargets() {
        return targets;
    }
    
    public void setTargets(List<ExamModuleModel> targets) {
        this.targets = targets;
    }
    
    @PostConstruct
    public void init() {
        HttpServletRequest request = JsfHelper.getRequest();
        this.targets = this.moduleDAO.findAllExamModuleModel(CookieUtils.getBusinessId(request));
        String id = request.getParameter("module_id");
        this.module = this.moduleDAO.findExamModuleModel(id);
        if (id == null || "".equals(id)) {
            this.questions = this.questionDAO.findAllCaseQuestion();
        } else {
            this.questions = this.questionDAO.findCaseQuestionByModule(id);
        }
        Collections.sort(questions);
    }
    
    public void delete(String id) {
        CaseQuestion cq = this.questionDAO.findCaseQuestion(id);
        this.logger.log("删除了综合题：" + cq.getCleanName());
        this.questionDAO.deleteCaseQuestion(id);
        this.questions = this.questionDAO.findCaseQuestionByModule(this.module.getId());
        //ExamPaperPool.checkExamination();//更新试卷池中的考试
    }
    
    public void batchDelete() {
        for (CaseQuestion c : this.questions) {
            if (c.isSelected()) {
                this.logger.log("删除了综合题：" + c.getCleanName());
                this.questionDAO.deleteCaseQuestion(c.getId());
            }
        }
        this.questions = this.questionDAO.findCaseQuestionByModule(this.module.getId());
        //ExamPaperPool.checkExamination();//更新试卷池中的考试
    }
    
    public void deleteAll() {
        this.logger.log("删除了模块" + module.getName() + "下的所有综合题");
        this.questionDAO.deleteCaseQuestionByModule(this.module.getId());
        this.questions = this.questionDAO.findCaseQuestionByModule(this.module.getId());
        //ExamPaperPool.checkExamination();//更新试卷池中的考试
    }
    
    public void batchMove() {
        for (CaseQuestion c : this.questions) {
            if (c.isSelected()) {
                this.logger.log("移动综合题：" + c.getCleanName() + "到模块：" + target.getName());
                c.setModule(target);
                this.questionDAO.updateCaseQuestion(c);
            }
        }
        this.questions = this.questionDAO.findCaseQuestionByModule(this.module.getId());
        //ExamPaperPool.checkExamination();//更新试卷池中的考试
    }
    
    public String editOrd(String id) {
        for (CaseQuestion cq : this.questions) {
            if (id.equals(cq.getId())) {
                this.questionDAO.updateCaseQuestion(cq);
                break;
            }
        }
        return null;
    }
    
    public void addToBasket() {
        BasketSession bs = JsfHelper.getBean("basketSession");
        for (CaseQuestion c : questions) {
            if (c.isSelected()) {
                CaseQuestion cc = (CaseQuestion) c;
                if (!bs.getBasket().getCases().contains(cc)) {
                    bs.getBasket().getCases().add(cc);
                }
            }
        }
    }
}
