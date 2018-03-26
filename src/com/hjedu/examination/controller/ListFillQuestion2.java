package com.hjedu.examination.controller;

import java.io.Serializable;
import java.util.Collections;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.servlet.http.HttpServletRequest;

import com.hjedu.examination.dao.IExamModule2DAO;
import com.hjedu.examination.dao.IFillQuestionDAO;
import com.hjedu.examination.entity.ExamModuleModel;
import com.hjedu.examination.entity.FillQuestion;
import com.hjedu.examination.entity.lazy.LazyFillQuestionList;
import com.hjedu.platform.controller.BasketSession;
import com.hjedu.platform.service.ILogService;
import com.huajie.exam.pool.ExamPaperPool;
import com.huajie.util.CookieUtils;
import com.huajie.util.JsfHelper;
import com.huajie.util.SpringHelper;

@ManagedBean
@ViewScoped
public class ListFillQuestion2  implements Serializable{
    ILogService logger = SpringHelper.getSpringBean("LogService");
    IFillQuestionDAO questionDAO = SpringHelper.getSpringBean("FillQuestionDAO");
    IExamModule2DAO moduleDAO = SpringHelper.getSpringBean("ExamModule2DAO");
    List<FillQuestion> questions;
    LazyFillQuestionList lcqs;
    ExamModuleModel module;
    List<ExamModuleModel> targets;
    ExamModuleModel target;
    
    public List<FillQuestion> getQuestions() {
        return questions;
    }
    
    public void setQuestions(List<FillQuestion> questions) {
        this.questions = questions;
    }

    public ExamModuleModel getModule() {
        return module;
    }

    public void setModule(ExamModuleModel module) {
        this.module = module;
    }

    public List<ExamModuleModel> getTargets() {
        return targets;
    }

    public void setTargets(List<ExamModuleModel> targets) {
        this.targets = targets;
    }

    public ExamModuleModel getTarget() {
        return target;
    }

    public void setTarget(ExamModuleModel target) {
        this.target = target;
    }
    
    @PostConstruct
    public void init() {
        HttpServletRequest request = JsfHelper.getRequest();
        this.targets = this.moduleDAO.findAllExamModuleModel(CookieUtils.getBusinessId(request));
        String id = request.getParameter("module_id");
        this.module=this.moduleDAO.findExamModuleModel(id);
        if (id == null||"".equals(id)) {
            this.questions = this.questionDAO.findAllFillQuestion();
        } else {
            this.questions = this.questionDAO.findFillQuestionByModule(id);
        }
        Collections.sort(questions);
    }
    
    
    public void delete(String id) {
        this.questionDAO.deleteFillQuestion(id);
        this.questions = this.questionDAO.findFillQuestionByModule(this.module.getId());
        //ExamPaperPool.checkExamination();//更新试卷池中的考试
    }
    
    public void batchDelete() {
        List<FillQuestion> cqs=this.lcqs.getModels();
        for (FillQuestion c : cqs) {
            if (c.isSelected()) {
                this.logger.log("删除了填空题："+c.getCleanName());
                this.questionDAO.deleteFillQuestion(c.getId());
            }
        }
        this.questions = this.questionDAO.findFillQuestionByModule(this.module.getId());
        //ExamPaperPool.checkExamination();//更新试卷池中的考试
    }
    
    public void deleteAll() {
        this.logger.log("删除了模块"+module.getName()+"下的所有填空题");
        this.questionDAO.deleteFillQuestionByModule(this.module.getId());
        this.questions = this.questionDAO.findFillQuestionByModule(this.module.getId());
        //ExamPaperPool.checkExamination();//更新试卷池中的考试
    }
    
    
    public void batchMove() {
        List<FillQuestion> cqs=this.lcqs.getModels();
        for (FillQuestion c : cqs) {
            if (c.isSelected()) {
                this.logger.log("移动填空题："+c.getCleanName()+"到模块："+target.getName());
                c.setModule(target);
                this.questionDAO.updateFillQuestion(c);
            }
        }
        this.questions = this.questionDAO.findFillQuestionByModule(this.module.getId());
        //ExamPaperPool.checkExamination();//更新试卷池中的考试
    }
    
    public String editOrd(String id) {
        List<FillQuestion> cqs=this.lcqs.getModels();
        for (FillQuestion cq : cqs) {
            if (id.equals(cq.getId())) {
                this.questionDAO.updateFillQuestion(cq);
                break;
            }
        }
        return null;
    }
    
    public void addToBasket() {
        BasketSession bs = JsfHelper.getBean("basketSession");
        List<FillQuestion> cqs = this.lcqs.getModels();
        for (FillQuestion c : cqs) {
            if (c.isSelected()) {
                if (!bs.getBasket().getFills().contains(c)) {
                    bs.getBasket().getFills().add(c);
                }
            }
        }
    }
    
}
