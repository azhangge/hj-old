package com.hjedu.examination.entity;

import java.io.Serializable;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletResponse;

import com.hjedu.examination.controller.CaseEditFill;
import com.hjedu.examination.dao.IExamModule2DAO;
import com.hjedu.examination.dao.IFillQuestionDAO;
import com.hjedu.platform.service.ILogService;
import com.huajie.util.HTMLCleaner;
import com.huajie.util.JsfHelper;
import com.huajie.util.MyLogger;
import com.huajie.util.SpringHelper;


public class FillCaseAgent  implements Serializable{
    ILogService logger = SpringHelper.getSpringBean("LogService");
    IFillQuestionDAO questionDAO = SpringHelper.getSpringBean("FillQuestionDAO");
    IExamModule2DAO moduleDAO = SpringHelper.getSpringBean("ExamModule2DAO");
    FillQuestion cq;
    boolean flag = false;
    
    public FillQuestion getCq() {
        return cq;
    }
    
    public void setCq(FillQuestion cq) {
        this.cq = cq;
    }

    
    public boolean isFlag() {
        return flag;
    }
    
    public void setFlag(boolean flag) {
        this.flag = flag;
    }
   

    public FillCaseAgent() {
            this.cq = new FillQuestion();
    }
    
     public void initFromCase(FillQuestion ccqq, CaseQuestion caseQ) {
        if (ccqq != null) {
            this.flag = true;
            this.setCq(ccqq);
            System.out.println(ccqq.getName());
            //this.em = em;
            //cq.setModule(em);
            //cq.setCaseQuestion(caseQ);
        } else {
            this.flag=false;
            this.cq = new FillQuestion();
            cq.setCaseQuestion(caseQ);
        }
    }
    
    
    public String caseMBDone() {
        CaseEditFill acq = JsfHelper.getBean("caseEditFill");
        if (flag) {
            this.questionDAO.updateFillQuestion(cq);
            this.cq = new FillQuestion();
        } else {
            acq.getCq().getFills().add(cq);
            this.questionDAO.updateFillQuestion(cq);
            this.cq = new FillQuestion();
        }
        return null;
    }
    
    public String done() {
        cq.setCleanName(HTMLCleaner.delHTMLTag(cq.getName()));
        if (flag) {
            this.questionDAO.updateFillQuestion(cq);
            //this.logger.log("修改了模块："+cq.getModule().getName()+"下的问答题："+cq.getCleanName());
        } else {
            this.questionDAO.addFillQuestion(cq);
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
            this.questionDAO.addFillQuestion(cq);
            this.cq = new FillQuestion();
        }

    }
    
    
}
