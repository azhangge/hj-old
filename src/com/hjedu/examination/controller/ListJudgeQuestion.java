package com.hjedu.examination.controller;

import java.io.Serializable;
import java.util.Collections;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.servlet.http.HttpServletRequest;

import com.hjedu.examination.dao.IExamModule2DAO;
import com.hjedu.examination.dao.IJudgeQuestionDAO;
import com.hjedu.examination.entity.ChoiceQuestion;
import com.hjedu.examination.entity.ExamModuleModel;
import com.hjedu.examination.entity.FillQuestion;
import com.hjedu.examination.entity.GeneralQuestion;
import com.hjedu.examination.entity.JudgeQuestion;
import com.hjedu.examination.entity.lazy.LazyChoiceQuestionList;
import com.hjedu.examination.entity.lazy.LazyJudgeQuestionList;
import com.hjedu.platform.controller.BasketSession;
import com.hjedu.platform.service.ILogService;
import com.huajie.exam.pool.ExamPaperPool;
import com.huajie.util.CookieUtils;
import com.huajie.util.JsfHelper;
import com.huajie.util.SpringHelper;

@ManagedBean
@ViewScoped
public class ListJudgeQuestion  implements Serializable{
    ILogService logger = SpringHelper.getSpringBean("LogService");
    IJudgeQuestionDAO questionDAO = SpringHelper.getSpringBean("JudgeQuestionDAO");
    IExamModule2DAO moduleDAO = SpringHelper.getSpringBean("ExamModule2DAO");
    //List<JudgeQuestion> questions;
    LazyJudgeQuestionList lcqs;
    ExamModuleModel module;
    List<ExamModuleModel> targets;
    ExamModuleModel target;
    
//    public List<JudgeQuestion> getQuestions() {
//        return questions;
//    }
//    
//    public void setQuestions(List<JudgeQuestion> questions) {
//        this.questions = questions;
//    }

    public LazyJudgeQuestionList getLcqs() {
        return lcqs;
    }

    public void setLcqs(LazyJudgeQuestionList lcqs) {
        this.lcqs = lcqs;
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
            //this.questions = this.questionDAO.findAllJudgeQuestion();
        } else {
            //this.questions = this.questionDAO.findJudgeQuestionByModule(id);
            this.buildLazy();
        }
        //Collections.sort(questions);
    }
    
    
    public void buildLazy() {
        if (lcqs == null) {
            this.lcqs = new LazyJudgeQuestionList(this.module.getId());
        }
        
    }
    
    public void delete(String id) {
        this.questionDAO.deleteJudgeQuestion(id);
        //this.questions = this.questionDAO.findJudgeQuestionByModule(this.module.getId());
        this.buildLazy();
        //ExamPaperPool.checkExamination();//更新试卷池中的考试
    }
    
    public void batchDelete() {
        List<JudgeQuestion> cqs=this.lcqs.getModels();
        for (JudgeQuestion c : cqs) {
            if (c.isSelected()) {
                this.logger.log("删除了判断题："+c.getCleanName());
                this.questionDAO.deleteJudgeQuestion(c.getId());
            }
        }
        //this.questions = this.questionDAO.findJudgeQuestionByModule(this.module.getId());
        this.buildLazy();
        //ExamPaperPool.checkExamination();//更新试卷池中的考试
    }
    
    public void deleteAll() {
        this.logger.log("删除了模块"+module.getName()+"下的所有判断题");
        this.questionDAO.deleteJudgeQuestionByModule(this.module.getId());
        //this.questions = this.questionDAO.findJudgeQuestionByModule(this.module.getId());
        this.buildLazy();
        //ExamPaperPool.checkExamination();//更新试卷池中的考试
    }
    
    
    public void batchMove() {
        List<JudgeQuestion> cqs=this.lcqs.getModels();
        for (JudgeQuestion c : cqs) {
            if (c.isSelected()) {
                this.logger.log("移动判断题："+c.getCleanName()+"到模块："+target.getName());
                c.setModule(target);
                this.questionDAO.updateJudgeQuestion(c);
            }
        }
        //this.questions = this.questionDAO.findJudgeQuestionByModule(this.module.getId());
        //ExamPaperPool.checkExamination();//更新试卷池中的考试
    }
    
    public String editOrd(String id) {
        List<JudgeQuestion> cqs=this.lcqs.getModels();
        for (JudgeQuestion cq : cqs) {
            if (id.equals(cq.getId())) {
                this.questionDAO.updateJudgeQuestion(cq);
                break;
            }
        }
        return null;
    }
    
    public void addToBasket() {
        BasketSession bs = JsfHelper.getBean("basketSession");
        List<JudgeQuestion> cqs = this.lcqs.getModels();
        for (JudgeQuestion c : cqs) {
            if (c.isSelected()) {
                if (!bs.getBasket().getJudges().contains(c)) {
                    bs.getBasket().getJudges().add(c);
                }
            }
        }
    }
    
}
