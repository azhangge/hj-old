package com.hjedu.examination.controller;

import java.io.Serializable;
import java.util.Collections;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.servlet.http.HttpServletRequest;

import com.hjedu.examination.dao.IExamModule2DAO;
import com.hjedu.examination.dao.IFileQuestionDAO;
import com.hjedu.examination.entity.ChoiceQuestion;
import com.hjedu.examination.entity.ExamModuleModel;
import com.hjedu.examination.entity.FileQuestion;
import com.hjedu.examination.entity.GeneralQuestion;
import com.hjedu.platform.controller.BasketSession;
import com.hjedu.platform.service.ILogService;
import com.huajie.exam.pool.ExamPaperPool;
import com.huajie.util.CookieUtils;
import com.huajie.util.JsfHelper;
import com.huajie.util.SpringHelper;

@ManagedBean
@ViewScoped
public class ListFileQuestion  implements Serializable{
    ILogService logger = SpringHelper.getSpringBean("LogService");
    IFileQuestionDAO questionDAO = SpringHelper.getSpringBean("FileQuestionDAO");
    IExamModule2DAO moduleDAO = SpringHelper.getSpringBean("ExamModule2DAO");
    List<FileQuestion> questions;
    ExamModuleModel module;
    List<ExamModuleModel> targets;
    ExamModuleModel target;
    
    public List<FileQuestion> getQuestions() {
        return questions;
    }
    
    public void setQuestions(List<FileQuestion> questions) {
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
            this.questions = this.questionDAO.findAllFileQuestion();
        } else {
            this.questions = this.questionDAO.findFileQuestionByModule(id);
        }
        Collections.sort(questions);
    }
    
    public void delete(String id) {
        FileQuestion cq=this.questionDAO.findFileQuestion(id);
        this.logger.log("删除了文件题："+cq.getCleanName());
        this.questionDAO.deleteFileQuestion(id);
        this.questions = this.questionDAO.findFileQuestionByModule(this.module.getId());
        //ExamPaperPool.checkExamination();//更新试卷池中的考试
    }
    
    public void batchDelete() {
        for (FileQuestion c : this.questions) {
            if (c.isSelected()) {
                this.logger.log("删除了文件题："+c.getCleanName());
                this.questionDAO.deleteFileQuestion(c.getId());
            }
        }
        this.questions = this.questionDAO.findFileQuestionByModule(this.module.getId());
        //ExamPaperPool.checkExamination();//更新试卷池中的考试
    }
    
    public void deleteAll() {
        this.logger.log("删除了模块"+module.getName()+"下的所有文件题");
        this.questionDAO.deleteFileQuestionByModule(this.module.getId());
        this.questions = this.questionDAO.findFileQuestionByModule(this.module.getId());
        //ExamPaperPool.checkExamination();//更新试卷池中的考试
    }
    
    
    public void batchMove() {
        for (FileQuestion c : this.questions) {
            if (c.isSelected()) {
                this.logger.log("移动文件题："+c.getCleanName()+"到模块："+target.getName());
                c.setModule(target);
                this.questionDAO.updateFileQuestion(c);
            }
        }
        this.questions = this.questionDAO.findFileQuestionByModule(this.module.getId());
        //ExamPaperPool.checkExamination();//更新试卷池中的考试
    }
    
    public String editOrd(String id) {
        for (FileQuestion cq : this.questions) {
            if (id.equals(cq.getId())) {
                this.questionDAO.updateFileQuestion(cq);
                break;
            }
        }
        return null;
    }
    
    public void addToBasket() {
        BasketSession bs = JsfHelper.getBean("basketSession");
        for (FileQuestion c : questions) {
            if (c.isSelected()) {
                if (!bs.getBasket().getFiles().contains(c)) {
                    bs.getBasket().getFiles().add(c);
                }
            }
        }
    }
    
}
