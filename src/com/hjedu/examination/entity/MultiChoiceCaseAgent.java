package com.hjedu.examination.entity;

import java.io.Serializable;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletResponse;

import com.hjedu.examination.controller.CaseEditMultiChoice;
import com.hjedu.examination.dao.IExamMultiChoiceDAO;
import com.hjedu.examination.dao.IMultiChoiceQuestionDAO;
import com.hjedu.platform.service.ILogService;
import com.huajie.util.HTMLCleaner;
import com.huajie.util.JsfHelper;
import com.huajie.util.MyLogger;
import com.huajie.util.SpringHelper;

public class MultiChoiceCaseAgent implements Serializable {

    ILogService logger = SpringHelper.getSpringBean("LogService");
    IMultiChoiceQuestionDAO questionDAO = SpringHelper.getSpringBean("MultiChoiceQuestionDAO");
    IExamMultiChoiceDAO choiceDAO = SpringHelper.getSpringBean("ExamMultiChoiceDAO");
    MultiChoiceQuestion cq;
    List<ExamMultiChoice> choices = new LinkedList();
    ExamMultiChoice newChoice = new ExamMultiChoice();
    boolean flag = false;

    public MultiChoiceQuestion getCq() {
        return cq;
    }

    public void setCq(MultiChoiceQuestion cq) {
        this.cq = cq;
    }

    public List<ExamMultiChoice> getChoices() {
        return choices;
    }

    public void setChoices(List<ExamMultiChoice> choices) {
        this.choices = choices;
    }

    public boolean isFlag() {
        return flag;
    }

    public void setFlag(boolean flag) {
        this.flag = flag;
    }

    public ExamMultiChoice getNewChoice() {
        return newChoice;
    }

    public void setNewChoice(ExamMultiChoice newChoice) {
        this.newChoice = newChoice;
    }

    public MultiChoiceCaseAgent() {
        this.cq = new MultiChoiceQuestion();
        this.loadChoice();
        this.loadAnswer();
        System.out.println("ChoiceCaseAgent init...");
    }

    public void initFromCase(MultiChoiceQuestion ccqq, CaseQuestion caseQ) {
        if (ccqq != null) {
            this.flag = true;
            this.setCq(ccqq);
            System.out.println(ccqq.getName());
            //this.em = em;
            //cq.setModule(em);
            //cq.setCaseQuestion(caseQ);
        } else {
            this.flag = false;
            this.cq = new MultiChoiceQuestion();
            cq.setCaseQuestion(caseQ);
        }
        this.loadChoice();
        this.loadAnswer();
    }

    private void loadAnswer() {
        if (!flag) {
            this.cq.setAnswer("");
        }
        String anw = this.cq.getAnswer();
        for (ExamMultiChoice e : this.getChoices()) {
            if (anw.contains(e.getLabel().toUpperCase())) {
                e.setSelected(true);
            }
        }
    }

    

    public void loadChoice() {
        if (flag) {
            this.choices = this.cq.getChoices();
        } else {
            this.choices=new LinkedList();
            for (int i = 0; i < 4; i++) {
                ExamMultiChoice e = new ExamMultiChoice();
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
        this.newChoice = new ExamMultiChoice();
    }

    public void deleteChoice(String id) {
        ExamMultiChoice ec = null;
        for (ExamMultiChoice e : this.choices) {
            if (e.getId().equals(id)) {
                ec = e;
                break;
            }
        }
        if (ec != null) {
            this.choices.remove(ec);
            //this.getCq().getChoices().remove(ec);
            //MyLogger.echo("选项数：" + choices.size()+":"+this.getCq().getChoices().size());

            this.choiceDAO.deleteExamMultiChoice(ec.getId());

        }
    }

    private void fitAnswer() {
        StringBuilder sb = new StringBuilder();
        for (ExamMultiChoice e : this.getChoices()) {
            if (e.isSelected()) {
                sb.append(e.getLabel());
            }
        }
        char[] cs = sb.toString().toUpperCase().toCharArray();
        List ls = new LinkedList();
        for (char c : cs) {
            ls.add(c);
        }
        Collections.sort(ls);
        Object[] css = ls.toArray();
        StringBuilder sb2 = new StringBuilder();
        for (Object oc : css) {
            sb2.append(oc.toString());
        }
        String s = sb2.toString();
        cq.setAnswer(s);
    }

    public String caseMBDone() {
        CaseEditMultiChoice acq = JsfHelper.getBean("caseEditMultiChoice");
        this.fitAnswer();
        //System.out.println(cq.getName());
        if (flag) {
            this.cq.setChoices(choices);
            this.questionDAO.updateMultiChoiceQuestion(cq);
            //MyLogger.echo("选项数：" + choices.size());
            this.cq = new MultiChoiceQuestion();
        } else {
            this.cq.setChoices(this.choices);
            acq.getCq().getMultiChoices().add(cq);
            this.questionDAO.updateMultiChoiceQuestion(cq);
            this.cq = new MultiChoiceQuestion();
        }
        return null;
    }

    public String done() {
        this.fitAnswer();
        cq.setCleanName(HTMLCleaner.delHTMLTag(cq.getName()));
        if (flag) {
            this.cq.setChoices(choices);
            //MyLogger.echo("选项数：" + choices.size());
            this.questionDAO.updateMultiChoiceQuestion(cq);
            //this.logger.log("修改了模块：" + cq.getModule().getName() + "下的单选题：" + cq.getCleanName());
        } else {
            this.cq.setChoices(choices);
            this.questionDAO.addMultiChoiceQuestion(cq);
            //this.logger.log("添加了模块：" + cq.getModule().getName() + "下的单选题：" + cq.getCleanName());
        }
        //return "ListMultiChoiceQuestion?faces-redirect=true";
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
            this.questionDAO.addMultiChoiceQuestion(cq);
            this.cq = new MultiChoiceQuestion();
            this.loadChoice();
            this.loadAnswer();
        }

    }
}
