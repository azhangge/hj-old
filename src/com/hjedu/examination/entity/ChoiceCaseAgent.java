package com.hjedu.examination.entity;

import java.io.Serializable;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletResponse;

import com.hjedu.examination.controller.CaseEditChoice;
import com.hjedu.examination.dao.IChoiceQuestionDAO;
import com.hjedu.examination.dao.IExamChoiceDAO;
import com.hjedu.platform.service.ILogService;
import com.huajie.util.HTMLCleaner;
import com.huajie.util.JsfHelper;
import com.huajie.util.MyLogger;
import com.huajie.util.SpringHelper;

public class ChoiceCaseAgent implements Serializable {

    ILogService logger = SpringHelper.getSpringBean("LogService");
    IChoiceQuestionDAO questionDAO = SpringHelper.getSpringBean("ChoiceQuestionDAO");
    IExamChoiceDAO choiceDAO = SpringHelper.getSpringBean("ExamChoiceDAO");
    ChoiceQuestion cq;
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

    public ChoiceCaseAgent() {
        this.cq = new ChoiceQuestion();
        this.loadChoice();
        this.loadAnswer();
        System.out.println("ChoiceCaseAgent init...");
    }

    public void initFromCase(ChoiceQuestion ccqq, CaseQuestion caseQ) {
        if (ccqq != null) {
            this.flag = true;
            this.setCq(ccqq);
            System.out.println(ccqq.getName());
            //this.em = em;
            //cq.setModule(em);
            //cq.setCaseQuestion(caseQ);
        } else {
            this.flag = false;
            this.cq = new ChoiceQuestion();
            cq.setCaseQuestion(caseQ);
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
            /*此处必须重新创建一个List，否则在AJAX连续添加单选过程中，
             可能上一题的选项的对象和本题的选项对象是同一对象，本题选项改后，
             上一题和本题选项相同。
             */
            this.choices = new LinkedList();
            for (int i = 0; i < 4; i++) {
                ExamChoice e = new ExamChoice();
                char t = 'A';
                t = (char) ((int) t + i);
                e.setLabel(String.valueOf(t));
                e.setQuestion(cq);
                this.choices.add(e);
            }
        }
        newChoice = new ExamChoice();
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
        CaseEditChoice acq = JsfHelper.getBean("caseEditChoice");
        this.fitAnswer();
        cq.setCleanName(HTMLCleaner.delHTMLTag(cq.getName()));
        //System.out.println(cq.getName());
        if (flag) {
            this.cq.setChoices(choices);
            this.questionDAO.updateChoiceQuestion(cq);
            //MyLogger.echo("选项数：" + choices.size());
            this.cq = new ChoiceQuestion();
        } else {
            this.cq.setChoices(this.choices);
            acq.getCq().getChoices().add(cq);
            this.questionDAO.updateChoiceQuestion(cq);
            this.cq = new ChoiceQuestion();
        }
        return null;
    }

    public String done() {
        this.fitAnswer();
        if (flag) {
            this.cq.setChoices(choices);
            //MyLogger.echo("选项数：" + choices.size());
            this.questionDAO.updateChoiceQuestion(cq);
            //this.logger.log("修改了模块：" + cq.getModule().getName() + "下的单选题：" + cq.getCleanName());
        } else {
            this.cq.setChoices(choices);
            this.questionDAO.addChoiceQuestion(cq);
            //this.logger.log("添加了模块：" + cq.getModule().getName() + "下的单选题：" + cq.getCleanName());
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
        return null;
    }

    public void addAndContinue() {
        if (!flag) {
            this.cq.setChoices(choices);
            this.questionDAO.addChoiceQuestion(cq);
            this.cq = new ChoiceQuestion();
            this.loadChoice();
            this.loadAnswer();
        }

    }
}
