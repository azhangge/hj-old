package com.hjedu.examination.controller;

import java.io.Serializable;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.servlet.http.HttpServletRequest;

import com.hjedu.examination.dao.IChoiceQuestionDAO;
import com.hjedu.examination.dao.IExamModule2DAO;
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
public class ListChoiceQuestion2 implements Serializable {

    ILogService logger = SpringHelper.getSpringBean("LogService");
    IChoiceQuestionDAO questionDAO = SpringHelper.getSpringBean("ChoiceQuestionDAO");
    IExamModule2DAO moduleDAO = SpringHelper.getSpringBean("ExamModule2DAO");
    List<ChoiceQuestion> questions = new LinkedList();
    ExamModuleModel module;
    List<ExamModuleModel> targets;
    ExamModuleModel target;

    public List<ChoiceQuestion> getQuestions() {
        return questions;
    }

    public void setQuestions(List<ChoiceQuestion> questions) {
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
            this.questions.addAll(this.questionDAO.findAllChoiceQuestion());
        } else {
            this.questions.addAll(this.questionDAO.findChoiceQuestionByModule(id));
        }
        Collections.sort(questions);
    }



    public void delete(String id) {
        ChoiceQuestion cq = this.questionDAO.findChoiceQuestion(id);
        this.logger.log("删除了单选题：" + cq.getCleanName());
        this.questionDAO.deleteChoiceQuestion(id);
        this.questions.clear();
        this.questions.addAll(this.questionDAO.findChoiceQuestionByModule(this.module.getId()));
        //ExamPaperPool.checkExamination();//更新试卷池中的考试
    }

    public void batchDelete() {
        List<ChoiceQuestion> cqs = this.questions;
        for (GeneralQuestion c : cqs) {
            if (c.isSelected()) {
                this.logger.log("删除了单选题：" + c.getCleanName());
                this.questionDAO.deleteChoiceQuestion(c.getId());
            }
        }
        this.questions.clear();
        this.questions.addAll(this.questionDAO.findChoiceQuestionByModule(this.module.getId()));
        //ExamPaperPool.checkExamination();//更新试卷池中的考试
    }

    public void deleteAll() {
        this.logger.log("删除了模块" + module.getName() + "下的所有单选题");
        this.questionDAO.deleteChoiceQuestionByModule(this.module.getId());
        this.questions.addAll(this.questionDAO.findChoiceQuestionByModule(this.module.getId()));
        //ExamPaperPool.checkExamination();//更新试卷池中的考试
    }

    public void batchMove() {
        List<ChoiceQuestion> cqs = this.questions;
        for (GeneralQuestion c : cqs) {
            if (c.isSelected()) {
                this.logger.log("移动单选题：" + c.getCleanName() + "到模块：" + target.getName());
                c.setModule(target);
                this.questionDAO.updateChoiceQuestion((ChoiceQuestion) c);
            }
        }
        this.questions.clear();
        this.questions.addAll(this.questionDAO.findChoiceQuestionByModule(this.module.getId()));
        //ExamPaperPool.checkExamination();//更新试卷池中的考试
    }

    public String editOrd(String id) {
        List<ChoiceQuestion> cqs = this.questions;
        for (ChoiceQuestion cq : cqs) {
            if (id.equals(cq.getId())) {
                this.questionDAO.updateChoiceQuestion(cq);
                break;
            }
        }
        return null;
    }

    public void addToBasket() {
        BasketSession bs = JsfHelper.getBean("basketSession");
        List<ChoiceQuestion> cqs = this.questions;
        for (GeneralQuestion c : cqs) {
            if (c.isSelected()) {
                ChoiceQuestion cc = (ChoiceQuestion) c;
                if (!bs.getBasket().getChoices().contains(cc)) {
                    bs.getBasket().getChoices().add(cc);
                }
            }
        }
    }

}
