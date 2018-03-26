package com.hjedu.examination.controller;

import java.io.Serializable;
import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.hjedu.examination.dao.IExamModule2DAO;
import com.hjedu.examination.dao.IFileQuestionDAO;
import com.hjedu.examination.entity.ExamModuleModel;
import com.hjedu.examination.entity.FileQuestion;
import com.hjedu.platform.service.ILogService;
import com.huajie.exam.pool.ExamPaperPool;
import com.huajie.util.JsfHelper;
import com.huajie.util.MyLogger;
import com.huajie.util.SpringHelper;

@ManagedBean
@ViewScoped
public class AAFileQuestion  implements Serializable{
    ILogService logger = SpringHelper.getSpringBean("LogService");
    IFileQuestionDAO questionDAO = SpringHelper.getSpringBean("FileQuestionDAO");
    IExamModule2DAO moduleDAO = SpringHelper.getSpringBean("ExamModule2DAO");
    FileQuestion cq;
    ExamModuleModel em;
    boolean flag = false;
    
    public FileQuestion getCq() {
        return cq;
    }
    
    public void setCq(FileQuestion cq) {
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
            this.cq = this.questionDAO.findFileQuestion(id);
            this.em = cq.getModule();
        } else {
            this.cq = new FileQuestion();
            em = this.moduleDAO.findExamModuleModel(moduleId);
            cq.setModule(em);
        }
    }
    
    
    
    public String done() {
        if (flag) {
            this.logger.log("修改了模块："+cq.getModule().getName()+"下的文件题："+cq.getCleanName());
            this.questionDAO.updateFileQuestion(cq);
        } else {
            this.logger.log("添加了模块："+cq.getModule().getName()+"下的文件题："+cq.getCleanName());
            this.questionDAO.addFileQuestion(cq);
        }
        //return "ListFillQuestion?faces-redirect=true";
        HttpServletResponse response = (HttpServletResponse) FacesContext.getCurrentInstance().getExternalContext().getResponse();
        try {
            String url = "ListAllQuestion.jspx?active_id=5&module_id=" + this.cq.getModule().getId();
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
            this.questionDAO.addFileQuestion(cq);
            this.cq = new FileQuestion();
            cq.setModule(em);
            //ExamPaperPool.checkExamination();//更新试卷池中的考试
        }

    }
    
    
}
