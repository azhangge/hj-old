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

import com.hjedu.examination.dao.IExamModule2DAO;
import com.hjedu.examination.dao.IExamMultiChoiceDAO;
import com.hjedu.examination.dao.IMultiChoiceQuestionDAO;
import com.hjedu.examination.entity.ExamModuleModel;
import com.hjedu.examination.entity.ExamMultiChoice;
import com.hjedu.examination.entity.MultiChoiceQuestion;
import com.hjedu.platform.service.ILogService;
import com.huajie.exam.pool.ExamPaperPool;
import com.huajie.util.CookieUtils;
import com.huajie.util.JsfHelper;
import com.huajie.util.MyLogger;
import com.huajie.util.SpringHelper;

@ManagedBean
@ViewScoped
public class AAMultiChoiceQuestion implements Serializable {

    ILogService logger = SpringHelper.getSpringBean("LogService");
    IMultiChoiceQuestionDAO questionDAO = SpringHelper.getSpringBean("MultiChoiceQuestionDAO");
    IExamModule2DAO moduleDAO = SpringHelper.getSpringBean("ExamModule2DAO");
    IExamMultiChoiceDAO choiceDAO = SpringHelper.getSpringBean("ExamMultiChoiceDAO");
    MultiChoiceQuestion cq;
    ExamModuleModel em;
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

    public ExamMultiChoice getNewChoice() {
        return newChoice;
    }

    public void setNewChoice(ExamMultiChoice newChoice) {
        this.newChoice = newChoice;
    }

    @PostConstruct
    public void init() {
        HttpServletRequest request = JsfHelper.getRequest();
        String id = request.getParameter("id");
        String moduleId = request.getParameter("module_id");
        if (id != null) {
            this.flag = true;
            this.cq = this.questionDAO.findMultiChoiceQuestion(id);
            this.em = cq.getModule();
        } else {
            this.cq = new MultiChoiceQuestion();
            em = this.moduleDAO.findExamModuleModel(moduleId);
            cq.setModule(em);
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
            this.choices.clear();
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

    public String done() {
        this.fitAnswer();
        if (flag) {
            this.cq.setChoices(choices);
            //MyLogger.echo("选项数：" + choices.size());
            this.logger.log("修改了模块："+cq.getModule().getName()+"下的多选题："+cq.getCleanName());
            this.questionDAO.updateMultiChoiceQuestion(cq);
        } else {
            this.cq.setChoices(choices);
            this.cq.setBusinessId(CookieUtils.getBusinessId(JsfHelper.getRequest()));
            this.logger.log("添加了模块："+cq.getModule().getName()+"下的多选题："+cq.getCleanName());
            this.questionDAO.addMultiChoiceQuestion(cq);
        }
        //return "ListChoiceQuestion?faces-redirect=true";
        HttpServletResponse response = (HttpServletResponse) FacesContext.getCurrentInstance().getExternalContext().getResponse();
        try {
            String url = "ListAllQuestion.jspx?active_id=1&module_id=" + this.cq.getModule().getId();
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
            this.questionDAO.addMultiChoiceQuestion(cq);
            this.cq = new MultiChoiceQuestion();
            cq.setModule(em);
            this.loadChoice();
            this.loadAnswer();
            //ExamPaperPool.checkExamination();//更新试卷池中的考试
        }

    }
}
