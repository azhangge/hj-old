package com.hjedu.examination.controller;

import java.io.Serializable;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

import com.hjedu.examination.dao.IExamCaseLogDAO;
import com.hjedu.examination.entity.ExamCaseLog;
import com.hjedu.examination.entity.ExamModuleModel;
import com.hjedu.platform.service.ILogService;
import com.huajie.util.SpringHelper;

@ManagedBean
@ViewScoped
public class ListExamCaseLog implements Serializable {

    ILogService logger = SpringHelper.getSpringBean("LogService");
    IExamCaseLogDAO logDAO = SpringHelper.getSpringBean("ExamCaseLogDAO");
    List<ExamCaseLog> logs;
    ExamModuleModel module;

    public List<ExamCaseLog> getLogs() {
        return logs;
    }

    public void setLogs(List<ExamCaseLog> logs) {
        this.logs = logs;
    }

    public ExamModuleModel getModule() {
        return module;
    }

    public void setModule(ExamModuleModel module) {
        this.module = module;
    }

    @PostConstruct
    public void init() {
        //HttpServletRequest request = JsfHelper.getRequest();
        this.logs = this.logDAO.findAllExamCaseLog();
    }

    public String delete(String id) {
        ExamCaseLog ll=this.logDAO.findExamCaseLog(id);
        this.logger.log("删除了考生："+ll.getUser().getUsername()+"的"+ll.getExamination().getName()+"抽题记录");
        this.logDAO.deleteExamCaseLog(id);
        
        this.logs = this.logDAO.findAllExamCaseLog();
        return null;
    }

    public String deleteAll() {
        this.logger.log("清空了所有抽题记录");
        this.logDAO.deleteAllExamCaseLog();
        this.logs = this.logDAO.findAllExamCaseLog();
        return null;
    }
}
