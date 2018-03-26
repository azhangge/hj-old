package com.hjedu.examination.entity;

import java.io.Serializable;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletResponse;

import com.hjedu.examination.controller.CaseEditEssay;
import com.hjedu.examination.dao.IEssayQuestionDAO;
import com.hjedu.examination.dao.IExamModule2DAO;
import com.hjedu.platform.service.ILogService;
import com.huajie.util.HTMLCleaner;
import com.huajie.util.JsfHelper;
import com.huajie.util.MyLogger;
import com.huajie.util.SpringHelper;


public class EssayCaseAgent  implements Serializable{
    ILogService logger = SpringHelper.getSpringBean("LogService");
    IEssayQuestionDAO questionDAO = SpringHelper.getSpringBean("EssayQuestionDAO");
    IExamModule2DAO moduleDAO = SpringHelper.getSpringBean("ExamModule2DAO");
    EssayQuestion cq;
    boolean flag = false;
    
    public EssayQuestion getCq() {
        return cq;
    }
    
    public void setCq(EssayQuestion cq) {
        this.cq = cq;
    }

    
    public boolean isFlag() {
        return flag;
    }
    
    public void setFlag(boolean flag) {
        this.flag = flag;
    }
   

    public EssayCaseAgent() {
            this.cq = new EssayQuestion();
    }
    
     public void initFromCase(EssayQuestion ccqq, CaseQuestion caseQ) {
        if (ccqq != null) {
            this.flag = true;
            this.setCq(ccqq);
            System.out.println(ccqq.getName());
            //this.em = em;
            //cq.setModule(em);
            //cq.setCaseQuestion(caseQ);
        } else {
            this.flag=false;
            this.cq = new EssayQuestion();
            cq.setCaseQuestion(caseQ);
        }
    }
    
    
    public String caseMBDone() {
        CaseEditEssay acq = JsfHelper.getBean("caseEditEssay");
        if (flag) {
            this.questionDAO.updateEssayQuestion(cq);
            this.cq = new EssayQuestion();
        } else {
            acq.getCq().getEssaies().add(cq);
            this.questionDAO.updateEssayQuestion(cq);
            this.cq = new EssayQuestion();
        }
        return null;
    }
    
    public String done() {
        cq.setCleanName(HTMLCleaner.delHTMLTag(cq.getName()));
        if (flag) {
            this.questionDAO.updateEssayQuestion(cq);
            //this.logger.log("修改了模块："+cq.getModule().getName()+"下的问答题："+cq.getCleanName());
        } else {
            this.questionDAO.addEssayQuestion(cq);
            //this.logger.log("添加了模块："+cq.getModule().getName()+"下的问答题："+cq.getCleanName());
        }
        //return "ListFillQuestion?faces-redirect=true";
        HttpServletResponse response = (HttpServletResponse) FacesContext.getCurrentInstance().getExternalContext().getResponse();
        try {
            String url = "ListAllQuestion.jspx?active_id=4&module_id=" + this.cq.getModule().getId();
            MyLogger.echo(url);
            response.sendRedirect(url);
            FacesContext.getCurrentInstance().responseComplete();
        } catch (Exception e) {
        }
        return null;
    }
    
    public void addAndContinue() {
        if (!flag) {
            this.questionDAO.addEssayQuestion(cq);
            this.cq = new EssayQuestion();
        }

    }
    
    
}
