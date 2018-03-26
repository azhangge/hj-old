package com.hjedu.examination.controller;

import java.io.Serializable;
import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.hjedu.examination.dao.IExamModule2DAO;
import com.hjedu.examination.dao.IJudgeQuestionDAO;
import com.hjedu.examination.entity.ExamModuleModel;
import com.hjedu.examination.entity.JudgeQuestion;
import com.hjedu.platform.service.ILogService;
import com.huajie.exam.pool.ExamPaperPool;
import com.huajie.util.CookieUtils;
import com.huajie.util.JsfHelper;
import com.huajie.util.MyLogger;
import com.huajie.util.SpringHelper;

@ManagedBean
@ViewScoped
public class AAJudgeQuestion  implements Serializable{
    ILogService logger = SpringHelper.getSpringBean("LogService");
    IJudgeQuestionDAO questionDAO = SpringHelper.getSpringBean("JudgeQuestionDAO");
    IExamModule2DAO moduleDAO = SpringHelper.getSpringBean("ExamModule2DAO");
    JudgeQuestion cq;
    ExamModuleModel em;
    boolean flag = false;
    
    public JudgeQuestion getCq() {
        return cq;
    }
    
    public void setCq(JudgeQuestion cq) {
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
            this.cq = this.questionDAO.findJudgeQuestion(id);
            this.em = cq.getModule();
        } else {
            this.cq = new JudgeQuestion();
            em = this.moduleDAO.findExamModuleModel(moduleId);
            cq.setModule(em);
        }
    }
    
    
    
    public String done() {
        if (flag) {
            this.logger.log("修改了模块："+cq.getModule().getName()+"下的判断题："+cq.getCleanName());
            this.questionDAO.updateJudgeQuestion(cq);
        } else {
            this.logger.log("添加了模块："+cq.getModule().getName()+"下的判断题："+cq.getCleanName());
            this.cq.setBusinessId(CookieUtils.getBusinessId(JsfHelper.getRequest()));
            this.questionDAO.addJudgeQuestion(cq);
        }
        //return "ListJudgeQuestion?faces-redirect=true";
        HttpServletResponse response = (HttpServletResponse) FacesContext.getCurrentInstance().getExternalContext().getResponse();
        try {
            String url = "ListAllQuestion.jspx?active_id=3&module_id=" + this.cq.getModule().getId();
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
        	this.cq.setBusinessId(CookieUtils.getBusinessId(JsfHelper.getRequest()));
            this.questionDAO.addJudgeQuestion(cq);
            this.cq = new JudgeQuestion();
            cq.setModule(em);
            //ExamPaperPool.checkExamination();//更新试卷池中的考试
        }
    }
    
    
    
}
