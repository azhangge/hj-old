package com.hjedu.examination.controller;

import java.io.Serializable;
import java.util.Collections;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.servlet.http.HttpServletRequest;

import com.hjedu.examination.dao.IEssayQuestionDAO;
import com.hjedu.examination.dao.IExamModule2DAO;
import com.hjedu.examination.entity.ChoiceQuestion;
import com.hjedu.examination.entity.EssayQuestion;
import com.hjedu.examination.entity.ExamModuleModel;
import com.hjedu.examination.entity.GeneralQuestion;
import com.hjedu.examination.entity.MultiChoiceQuestion;
import com.hjedu.examination.entity.lazy.LazyChoiceQuestionList;
import com.hjedu.examination.entity.lazy.LazyEssayQuestionList;
import com.hjedu.platform.controller.BasketSession;
import com.hjedu.platform.service.ILogService;
import com.huajie.exam.pool.ExamPaperPool;
import com.huajie.util.CookieUtils;
import com.huajie.util.JsfHelper;
import com.huajie.util.SpringHelper;

@ManagedBean
@ViewScoped
public class ListEssayQuestion  implements Serializable{
    ILogService logger = SpringHelper.getSpringBean("LogService");
    IEssayQuestionDAO questionDAO = SpringHelper.getSpringBean("EssayQuestionDAO");
    IExamModule2DAO moduleDAO = SpringHelper.getSpringBean("ExamModule2DAO");
    //List<EssayQuestion> questions;
    LazyEssayQuestionList lcqs;
    ExamModuleModel module;
    List<ExamModuleModel> targets;
    ExamModuleModel target;
    
//    public List<EssayQuestion> getQuestions() {
//        return questions;
//    }
//    
//    public void setQuestions(List<EssayQuestion> questions) {
//        this.questions = questions;
//    }
    public LazyEssayQuestionList getLcqs() {
        return lcqs;
    }

    public void setLcqs(LazyEssayQuestionList lcqs) {
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
            //this.questions = this.questionDAO.findAllEssayQuestion();
        } else {
            //this.questions = this.questionDAO.findEssayQuestionByModule(id);
            this.buildLazy();
        }
        //Collections.sort(questions);
    }
    
    
    public void buildLazy() {
        if (lcqs == null) {
            this.lcqs = new LazyEssayQuestionList(this.module.getId());
        }
        
    }
    
    public void delete(String id) {
        EssayQuestion cq=this.questionDAO.findEssayQuestion(id);
        this.questionDAO.deleteEssayQuestion(id);
        this.logger.log("删除了问答题："+cq.getCleanName());
        //this.questions = this.questionDAO.findEssayQuestionByModule(this.module.getId());
        this.buildLazy();
        //ExamPaperPool.checkExamination();//更新试卷池中的考试
    }
    
    public void batchDelete() {
        List<EssayQuestion> cqs=this.lcqs.getModels();
        for (EssayQuestion c : cqs) {
            if (c.isSelected()) {
                this.logger.log("删除了问答题："+c.getCleanName());
                this.questionDAO.deleteEssayQuestion(c.getId());
            }
        }
        //this.questions = this.questionDAO.findEssayQuestionByModule(this.module.getId());
        this.buildLazy();
        //ExamPaperPool.checkExamination();//更新试卷池中的考试
    }
    
    public void deleteAll() {
        this.logger.log("删除了模块"+module.getName()+"下的所有问答题");
        this.questionDAO.deleteEssayQuestionByModule(this.module.getId());
        //this.questions = this.questionDAO.findEssayQuestionByModule(this.module.getId());
        this.buildLazy();
        //ExamPaperPool.checkExamination();//更新试卷池中的考试
    }
    
    
    public void batchMove() {
        List<EssayQuestion> cqs=this.lcqs.getModels();
        for (EssayQuestion c : cqs) {
            if (c.isSelected()) {
                this.logger.log("移动问答题："+c.getCleanName()+"到模块："+target.getName());
                c.setModule(target);
                this.questionDAO.updateEssayQuestion(c);
            }
        }
        //this.questions = this.questionDAO.findEssayQuestionByModule(this.module.getId());
        this.buildLazy();
        //ExamPaperPool.checkExamination();//更新试卷池中的考试
    }
    
    public String editOrd(String id) {
        List<EssayQuestion> cqs=this.lcqs.getModels();
        for (EssayQuestion cq : cqs) {
            if (id.equals(cq.getId())) {
                this.questionDAO.updateEssayQuestion(cq);
                break;
            }
        }
        return null;
    }
    
    public void addToBasket() {
        BasketSession bs = JsfHelper.getBean("basketSession");
        List<EssayQuestion> cqs = this.lcqs.getModels();
        for (EssayQuestion c : cqs) {
            if (c.isSelected()) {
                if (!bs.getBasket().getEssaies().contains(c)) {
                    bs.getBasket().getEssaies().add(c);
                }
            }
        }
    }
}
