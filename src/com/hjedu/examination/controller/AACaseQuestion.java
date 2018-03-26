package com.hjedu.examination.controller;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.hjedu.examination.dao.ICaseQuestionDAO;
import com.hjedu.examination.dao.IChoiceQuestionDAO;
import com.hjedu.examination.dao.IEssayQuestionDAO;
import com.hjedu.examination.dao.IExamModule2DAO;
import com.hjedu.examination.entity.CaseQuestion;
import com.hjedu.examination.entity.ChoiceQuestion;
import com.hjedu.examination.entity.EssayQuestion;
import com.hjedu.examination.entity.ExamModuleModel;
import com.hjedu.examination.util.ExamCaseServiceUtils;
import com.hjedu.platform.service.ILogService;
import com.huajie.exam.pool.ExamPaperPool;
import com.huajie.util.JsfHelper;
import com.huajie.util.MyLogger;
import com.huajie.util.SpringHelper;

@ManagedBean
@ViewScoped
public class AACaseQuestion implements Serializable {

    ILogService logger = SpringHelper.getSpringBean("LogService");
    ICaseQuestionDAO questionDAO = SpringHelper.getSpringBean("CaseQuestionDAO");
    IChoiceQuestionDAO choiceDAO = SpringHelper.getSpringBean("ChoiceQuestionDAO");
    IEssayQuestionDAO essayDAO = SpringHelper.getSpringBean("EssayQuestionDAO");
    IExamModule2DAO moduleDAO = SpringHelper.getSpringBean("ExamModule2DAO");
    CaseQuestion cq;
    ExamModuleModel em;
    boolean flag = false;
    //ChoiceQuestion newChoice;
    //EssayQuestion newEssay;
    //ChoiceCaseAgent choiceAgent = new ChoiceCaseAgent();
    //EssayCaseAgent essayAgent = new EssayCaseAgent();

    public CaseQuestion getCq() {
        return cq;
    }

    public void setCq(CaseQuestion cq) {
        this.cq = cq;
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

    @PostConstruct
    public void init() {
        HttpServletRequest request = JsfHelper.getRequest();
        String id = request.getParameter("id");
        String moduleId = request.getParameter("module_id");
        if (id != null) {
            this.flag = true;
            this.cq = this.questionDAO.findCaseQuestion(id);
            this.em = cq.getModule();
        } else {
            this.cq = new CaseQuestion();
            em = this.moduleDAO.findExamModuleModel(moduleId);
            cq.setModule(em);
            //新增加的综合题的单选题是空的
            if (cq.getChoices() == null) {
                cq.setChoices(new ArrayList<ChoiceQuestion>());
            }
            //新增加的综合题的问答题是空的
            if (cq.getEssaies() == null) {
                cq.setEssaies(new ArrayList<EssayQuestion>());
            }
        }
        ExamCaseServiceUtils.orderCaseQuestion(cq);
    }

    public String delChoice(String id) {
        List<ChoiceQuestion> cs = this.cq.getChoices();
        ChoiceQuestion ccqq = null;
        for (ChoiceQuestion c : cs) {
            if (c.getId().equals(id)) {
                ccqq = c;
                break;
            }
        }
        if (ccqq != null) {
            cs.remove(ccqq);
            this.choiceDAO.deleteChoiceQuestion(ccqq.getId());
        }
        return null;
    }


   

    public String delEssay(String id) {
        List<EssayQuestion> cs = this.cq.getEssaies();
        EssayQuestion ccqq = null;
        for (EssayQuestion c : cs) {
            if (c.getId().equals(id)) {
                ccqq = c;
                break;
            }
        }
        if (ccqq != null) {
            cs.remove(ccqq);
            this.essayDAO.deleteEssayQuestion(ccqq.getId());
        }
        return null;
    }


    public String done() {
//        List<EssayQuestion> cqsTemp = cq.getEssaies();
//        for (EssayQuestion cqst : cqsTemp) {
//            System.out.println(cqst.getName());
//        }
        if (flag) {
            this.questionDAO.updateCaseQuestion(cq);
            this.logger.log("修改了模块：" + cq.getModule().getName() + "下的综合题：" + cq.getCleanName());
        } else {
            this.questionDAO.addCaseQuestion(cq);
            this.logger.log("添加了模块：" + cq.getModule().getName() + "下的综合题：" + cq.getCleanName());
        }
        //return "ListFillQuestion?faces-redirect=true";
        HttpServletResponse response = (HttpServletResponse) FacesContext.getCurrentInstance().getExternalContext().getResponse();
        try {
            String url = "ListAllQuestion.jspx?active_id=6&module_id=" + this.cq.getModule().getId();
            MyLogger.echo(url);
            response.sendRedirect(url);
            FacesContext.getCurrentInstance().responseComplete();
        } catch (Exception e) {
        }
        //ExamPaperPool.checkExamination();//更新试卷池中的考试
        return null;
    }
    
    public String saveAndEditChoice() {
        if (flag) {
            this.questionDAO.updateCaseQuestion(cq);
        } else {
            this.questionDAO.addCaseQuestion(cq);
        }
        HttpServletResponse response = (HttpServletResponse) FacesContext.getCurrentInstance().getExternalContext().getResponse();
        try {
            String url = "CaseEditChoice.jspx?id=" + this.cq.getId();
            MyLogger.echo(url);
            response.sendRedirect(url);
            FacesContext.getCurrentInstance().responseComplete();
        } catch (Exception e) {
        }
        return null;
    }
    
    public String saveAndEditMultiChoice() {
        if (flag) {
            this.questionDAO.updateCaseQuestion(cq);
        } else {
            this.questionDAO.addCaseQuestion(cq);
        }
        HttpServletResponse response = (HttpServletResponse) FacesContext.getCurrentInstance().getExternalContext().getResponse();
        try {
            String url = "CaseEditMultiChoice.jspx?id=" + this.cq.getId();
            MyLogger.echo(url);
            response.sendRedirect(url);
            FacesContext.getCurrentInstance().responseComplete();
        } catch (Exception e) {
        }
        return null;
    }
    
    public String saveAndEditFill() {
        if (flag) {
            this.questionDAO.updateCaseQuestion(cq);
        } else {
            this.questionDAO.addCaseQuestion(cq);
        }
        HttpServletResponse response = (HttpServletResponse) FacesContext.getCurrentInstance().getExternalContext().getResponse();
        try {
            String url = "CaseEditFill.jspx?id=" + this.cq.getId();
            MyLogger.echo(url);
            response.sendRedirect(url);
            FacesContext.getCurrentInstance().responseComplete();
        } catch (Exception e) {
        }
        return null;
    }
    
    public String saveAndEditJudge() {
        if (flag) {
            this.questionDAO.updateCaseQuestion(cq);
        } else {
            this.questionDAO.addCaseQuestion(cq);
        }
        HttpServletResponse response = (HttpServletResponse) FacesContext.getCurrentInstance().getExternalContext().getResponse();
        try {
            String url = "CaseEditJudge.jspx?id=" + this.cq.getId();
            MyLogger.echo(url);
            response.sendRedirect(url);
            FacesContext.getCurrentInstance().responseComplete();
        } catch (Exception e) {
        }
        return null;
    }
    
     public String saveAndEditEssay(){
        if (flag) {
            this.questionDAO.updateCaseQuestion(cq);
        } else {
            this.questionDAO.addCaseQuestion(cq);
        }
        HttpServletResponse response = (HttpServletResponse) FacesContext.getCurrentInstance().getExternalContext().getResponse();
        try {
            String url = "CaseEditEssay.jspx?id=" + this.cq.getId();
            MyLogger.echo(url);
            response.sendRedirect(url);
            FacesContext.getCurrentInstance().responseComplete();
        } catch (Exception e) {
        }
        return null;
    }

    public void addAndContinue() {
        if (!flag) {
            this.questionDAO.addCaseQuestion(cq);
            this.cq = new CaseQuestion();
            cq.setModule(em);
            //ExamPaperPool.checkExamination();//更新试卷池中的考试
        }

    }
}
