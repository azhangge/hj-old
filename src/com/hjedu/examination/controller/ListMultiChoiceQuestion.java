package com.hjedu.examination.controller;

import java.io.Serializable;
import java.util.Collections;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.servlet.http.HttpServletRequest;

import com.hjedu.examination.dao.IExamModule2DAO;
import com.hjedu.examination.dao.IMultiChoiceQuestionDAO;
import com.hjedu.examination.entity.ChoiceQuestion;
import com.hjedu.examination.entity.ExamModuleModel;
import com.hjedu.examination.entity.GeneralQuestion;
import com.hjedu.examination.entity.JudgeQuestion;
import com.hjedu.examination.entity.MultiChoiceQuestion;
import com.hjedu.examination.entity.lazy.LazyChoiceQuestionList;
import com.hjedu.examination.entity.lazy.LazyMultiChoiceQuestionList;
import com.hjedu.platform.controller.BasketSession;
import com.hjedu.platform.service.ILogService;
import com.huajie.exam.pool.ExamPaperPool;
import com.huajie.util.CookieUtils;
import com.huajie.util.JsfHelper;
import com.huajie.util.SpringHelper;

@ManagedBean
@ViewScoped
public class ListMultiChoiceQuestion implements Serializable {
    ILogService logger=SpringHelper.getSpringBean("LogService");
    IMultiChoiceQuestionDAO questionDAO = SpringHelper.getSpringBean("MultiChoiceQuestionDAO");
    IExamModule2DAO moduleDAO = SpringHelper.getSpringBean("ExamModule2DAO");
    //List<MultiChoiceQuestion> questions=new ArrayList();
    LazyMultiChoiceQuestionList lcqs;
    ExamModuleModel module;
    List<ExamModuleModel> targets;
    ExamModuleModel target;

    public LazyMultiChoiceQuestionList getLcqs() {
        return lcqs;
    }

    public void setLcqs(LazyMultiChoiceQuestionList lcqs) {
        this.lcqs = lcqs;
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
            //this.questions.addAll(questionDAO.findAllMultiChoiceQuestion());
        } else {
            //this.questions.addAll(this.questionDAO.findMultiChoiceQuestionByModule(id));
            this.buildLazy();
        }
        //Collections.sort(questions);
    }
    
    
    public void buildLazy() {
        if (lcqs == null) {
            this.lcqs = new LazyMultiChoiceQuestionList(this.module.getId());
        }
        
    }
    
    public void delete(String id) {
        MultiChoiceQuestion mc=this.questionDAO.findMultiChoiceQuestion(id);
        this.logger.log("删除了多选题："+mc.getCleanName());
        this.questionDAO.deleteMultiChoiceQuestion(id);
        //this.questions.clear();
        //this.questions.addAll(this.questionDAO.findMultiChoiceQuestionByModule(this.module.getId()));
        this.buildLazy();
        //ExamPaperPool.checkExamination();//更新试卷池中的考试
    }
    
    public void batchDelete() {
        List<MultiChoiceQuestion> cqs=this.lcqs.getModels();
        for (GeneralQuestion c : cqs) {
            if (c.isSelected()) {
                this.logger.log("删除了多选题："+c.getCleanName());
                this.questionDAO.deleteMultiChoiceQuestion(c.getId());
            }
        }
        //this.questions.clear();
        //this.questions.addAll(this.questionDAO.findMultiChoiceQuestionByModule(this.module.getId()));
        this.buildLazy();
        //ExamPaperPool.checkExamination();//更新试卷池中的考试
    }
    
    public void deleteAll() {
        this.logger.log("清空了模块"+this.module.getName()+"下的多选题");
        this.questionDAO.deleteMultiChoiceQuestionByModule(this.module.getId());
        //this.questions.clear();
        //this.questions.addAll(this.questionDAO.findMultiChoiceQuestionByModule(this.module.getId()));
        this.buildLazy();
        //ExamPaperPool.checkExamination();//更新试卷池中的考试
    }
    
    
    public void batchMove() {
        List<MultiChoiceQuestion> cqs=this.lcqs.getModels();
        for (GeneralQuestion c : cqs) {
            if (c.isSelected()) {
                this.logger.log("移动多选题："+c.getCleanName()+"到"+target.getName());
                c.setModule(target);
                this.questionDAO.updateMultiChoiceQuestion((MultiChoiceQuestion)c);
            }
        }
        //this.questions.clear();
        //this.questions.addAll(this.questionDAO.findMultiChoiceQuestionByModule(this.module.getId()));
        //ExamPaperPool.checkExamination();//更新试卷池中的考试
    }
    
    
    public String editOrd(String id) {
        List<MultiChoiceQuestion> cqs=this.lcqs.getModels();
        for (MultiChoiceQuestion cq : cqs) {
            if (id.equals(cq.getId())) {
                this.questionDAO.updateMultiChoiceQuestion(cq);
                break;
            }
        }
        return null;
    }
    
    public void addToBasket() {
        BasketSession bs = JsfHelper.getBean("basketSession");
        List<MultiChoiceQuestion> cqs = this.lcqs.getModels();
        for (GeneralQuestion c : cqs) {
            if (c.isSelected()) {
                MultiChoiceQuestion cc = (MultiChoiceQuestion) c;
                if (!bs.getBasket().getMultiChoices().contains(cc)) {
                    bs.getBasket().getMultiChoices().add(cc);
                }
            }
        }
    }
}
