package com.hjedu.examination.controller;

import java.io.Serializable;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.hjedu.examination.dao.IChoiceQuestionDAO;
import com.hjedu.examination.dao.IExamChoiceDAO;
import com.hjedu.examination.dao.IExamModule2DAO;
import com.hjedu.examination.entity.CaseQuestion;
import com.hjedu.examination.entity.ChoiceQuestion;
import com.hjedu.examination.entity.ExamChoice;
import com.hjedu.examination.entity.ExamModuleModel;
import com.hjedu.platform.service.ILogService;
import com.huajie.exam.pool.ExamPaperPool;
import com.huajie.util.CookieUtils;
import com.huajie.util.JsfHelper;
import com.huajie.util.MyLogger;
import com.huajie.util.SpringHelper;

@ManagedBean
@ViewScoped
public class AAChoiceQuestion implements Serializable {

    ILogService logger = SpringHelper.getSpringBean("LogService");
    IChoiceQuestionDAO questionDAO = SpringHelper.getSpringBean("ChoiceQuestionDAO");
    IExamModule2DAO moduleDAO = SpringHelper.getSpringBean("ExamModule2DAO");
    IExamChoiceDAO choiceDAO = SpringHelper.getSpringBean("ExamChoiceDAO");
    ChoiceQuestion cq;
    ExamModuleModel em;
    List<ExamChoice> choices = new LinkedList();
    ExamChoice newChoice = new ExamChoice();
    boolean flag = false;

    public ChoiceQuestion getCq() {
        return cq;
    }

    public void setCq(ChoiceQuestion cq) {
        this.cq = cq;
    }

    public List<ExamChoice> getChoices() {
        return choices;
    }

    public void setChoices(List<ExamChoice> choices) {
        this.choices = choices;
    }

    public ExamModuleModel getEm() {
        return em;
    }

    public void setEm(ExamModuleModel em) {
        this.em = em;
    }

    public boolean isFlag() {
        return flag;
    }

    public void setFlag(boolean flag) {
        this.flag = flag;
    }

    public ExamChoice getNewChoice() {
        return newChoice;
    }

    public void setNewChoice(ExamChoice newChoice) {
        this.newChoice = newChoice;
    }

    @PostConstruct
    public void init() {
        HttpServletRequest request = JsfHelper.getRequest();
        String id = request.getParameter("id");
        String moduleId = request.getParameter("module_id");
        if (id != null) {
            this.flag = true;
            this.cq = this.questionDAO.findChoiceQuestion(id);
            this.em = cq.getModule();
        } else {
            this.cq = new ChoiceQuestion();
            em = this.moduleDAO.findExamModuleModel(moduleId);
            cq.setModule(em);
        }
        this.loadChoice();
        this.loadAnswer();
    }

    public void initFromCase(ChoiceQuestion ccqq, CaseQuestion caseQ, ExamModuleModel em) {
        if (ccqq != null) {
            this.flag = true;
            this.setCq(ccqq);
            System.out.println(ccqq.getName());
            //this.em = em;
            //cq.setModule(em);
            //cq.setCaseQuestion(caseQ);
        } else {
            this.cq = new ChoiceQuestion();
            cq.setModule(em);
            cq.setCaseQuestion(caseQ);
            this.em = em;
        }
        this.loadChoice();
        this.loadAnswer();
    }

    private void loadAnswer() {
        if (!flag) {
            this.cq.setAnswer("A");
        }
        String anw = this.cq.getAnswer();
        for (ExamChoice e : this.getChoices()) {
            if (anw.equals(e.getLabel())) {
                e.setSelected(true);
            }
        }
    }

    public void changeAnswer(String id) {
        for (ExamChoice e : this.choices) {
            e.setSelected(false);
        }
        for (ExamChoice e : this.choices) {
            if (e.getId().equals(id)) {
                e.setSelected(true);
                break;
            }
        }
    }

    public void loadChoice() {
        if (flag) {
            this.choices = this.cq.getChoices();
        } else {
            this.choices.clear();
            for (int i = 0; i < 4; i++) {
                ExamChoice e = new ExamChoice();
                char t = 'A';
                t = (char) ((int) t + i);
                e.setLabel(String.valueOf(t));
                e.setQuestion(cq);
                this.choices.add(e);
            }
        }
        Collections.sort(choices);
    }

    public void addNewChoice() {
        this.newChoice.setQuestion(cq);
        this.choices.add(newChoice);
        this.newChoice = new ExamChoice();
    }

    public void deleteChoice(String id) {
        ExamChoice ec = null;
        for (ExamChoice e : this.choices) {
            if (e.getId().equals(id)) {
                ec = e;
                break;
            }
        }
        if (ec != null) {
            this.choices.remove(ec);
            //this.getCq().getChoices().remove(ec);
            //MyLogger.echo("选项数：" + choices.size()+":"+this.getCq().getChoices().size());

            this.choiceDAO.deleteExamChoice(ec.getId());

        }
    }

    private void fitAnswer() {
        for (ExamChoice e : this.getChoices()) {
            if (e.isSelected()) {
                cq.setAnswer(e.getLabel());
                break;
            }
        }
    }

    public String caseMBDone() {
        AACaseQuestion acq=JsfHelper.getBean("aACaseQuestion");
        this.fitAnswer();
        if (flag) {
            this.cq.setChoices(choices);
            //MyLogger.echo("选项数：" + choices.size());
            
        } else {
            this.cq.setChoices(choices);
            acq.getCq().getChoices().add(cq);
        }
        return null;
    }

    public String done() {
        this.fitAnswer();
        if (flag) {
            this.cq.setChoices(choices);
            //MyLogger.echo("选项数：" + choices.size());
            this.questionDAO.updateChoiceQuestion(cq);
            this.logger.log("修改了模块：" + cq.getModule().getName() + "下的单选题：" + cq.getCleanName());
        } else {
            this.cq.setChoices(choices);
            this.cq.setBusinessId(CookieUtils.getBusinessId(JsfHelper.getRequest()));
            this.questionDAO.addChoiceQuestion(cq);
            this.logger.log("添加了模块：" + cq.getModule().getName() + "下的单选题：" + cq.getCleanName());
        }
        //return "ListChoiceQuestion?faces-redirect=true";
        HttpServletResponse response = (HttpServletResponse) FacesContext.getCurrentInstance().getExternalContext().getResponse();
        try {
            String url = "ListAllQuestion.jspx?active_id=0&module_id=" + this.cq.getModule().getId();
            MyLogger.echo(url);
            response.sendRedirect(url);
            FacesContext.getCurrentInstance().responseComplete();
        } catch (Exception e) {
        }
        //ExamPaperPool.checkExamination();//更新试卷池中的考试
        return null;
    }

    public void addAndContinue() {
        if (!flag) {
            this.fitAnswer();
            this.cq.setChoices(choices);
            this.cq.setBusinessId(CookieUtils.getBusinessId(JsfHelper.getRequest()));
            this.questionDAO.addChoiceQuestion(cq);
            this.cq = new ChoiceQuestion();
            cq.setModule(em);
            this.loadChoice();
            this.loadAnswer();
            //ExamPaperPool.checkExamination();//更新试卷池中的考试
        }

    }
}
