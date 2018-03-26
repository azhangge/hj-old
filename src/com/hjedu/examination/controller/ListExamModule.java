package com.hjedu.examination.controller;

import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

import com.hjedu.examination.dao.IExamModule2DAO;
import com.hjedu.examination.entity.ExamModuleModel;
import com.hjedu.platform.service.ILogService;
import com.huajie.util.CookieUtils;
import com.huajie.util.JsfHelper;
import com.huajie.util.SpringHelper;

@ManagedBean
@ViewScoped
public class ListExamModule implements Serializable {

    ILogService logger = SpringHelper.getSpringBean("LogService");
    IExamModule2DAO moduleDAO = SpringHelper.getSpringBean("ExamModule2DAO");
    List<ExamModuleModel> modules = new LinkedList();
    String businessId;
    
    public List<ExamModuleModel> getModules() {
        return modules;
    }
    
    public void setModules(List<ExamModuleModel> modules) {
        this.modules = modules;
    }
    
    @PostConstruct
    public void init() {
    	this.businessId = CookieUtils.getBusinessId(JsfHelper.getRequest());
        this.modules = this.moduleDAO.findRootExamModuleModelByRootId(this.businessId);
    }
    
    public void delete(String id) {
        ExamModuleModel em = this.moduleDAO.findExamModuleModel(id);
        this.logger.log("删除了试题模块：" + em.getName());
        this.moduleDAO.deleteExamModuleModel(id);
        this.modules = this.moduleDAO.findRootExamModuleModelByRootId(this.businessId);
        //ExamPaperPool.checkExamination();//更新试卷池中的考试
    }
}
