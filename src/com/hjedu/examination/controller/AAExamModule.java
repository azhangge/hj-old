package com.hjedu.examination.controller;

import java.io.Serializable;
import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.servlet.http.HttpServletRequest;

import com.hjedu.examination.dao.IExamModule2DAO;
import com.hjedu.examination.entity.ExamModuleModel;
import com.hjedu.platform.service.ILogService;
import com.huajie.util.CookieUtils;
import com.huajie.util.JsfHelper;
import com.huajie.util.SpringHelper;

@ManagedBean
@ViewScoped
public class AAExamModule implements Serializable {

    ILogService logger = SpringHelper.getSpringBean("LogService");
    IExamModule2DAO moduleDAO = SpringHelper.getSpringBean("ExamModule2DAO");
    ExamModuleModel module = null;
    boolean flag = false;
    String businessId;

    public boolean isFlag() {
        return flag;
    }

    public void setFlag(boolean flag) {
        this.flag = flag;
    }

    public ExamModuleModel getModule() {
        return module;
    }

    public void setModule(ExamModuleModel module) {
        this.module = module;
    }

    @PostConstruct
    public void init() {
        HttpServletRequest request = JsfHelper.getRequest();
        this.businessId = CookieUtils.getBusinessId(request);
        String id = request.getParameter("id");
        if (id != null) {
            this.flag = true;
            this.module = this.moduleDAO.findExamModuleModel(id);
        } else {
            this.module = new ExamModuleModel();
            this.module.setFatherId(this.businessId);
        }
    }

    public String done() {
        if (flag) {
            this.logger.log("修改了试题模块：" + module.getName());
            this.moduleDAO.updateExamModuleModel(module);
        } else {
            this.logger.log("添加了试题模块：" + module.getName());
            this.moduleDAO.addExamModuleModel(module);
        }
        return "ListExamModule?faces-redirect=true";
    }
}
