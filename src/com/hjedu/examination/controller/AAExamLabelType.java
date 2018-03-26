package com.hjedu.examination.controller;


import java.io.Serializable;
import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.servlet.http.HttpServletRequest;

import com.hjedu.examination.dao.IExamLabelTypeDAO;
import com.hjedu.examination.entity.ExamLabelType;
import com.hjedu.platform.service.ILogService;
import com.huajie.util.JsfHelper;
import com.huajie.util.SpringHelper;

@ManagedBean
@ViewScoped
public class AAExamLabelType implements Serializable {

    ILogService logger = SpringHelper.getSpringBean("LogService");
    IExamLabelTypeDAO labelTypeDAO = SpringHelper.getSpringBean("ExamLabelTypeDAO");
    ExamLabelType labelType = null;
    boolean flag = false;
    
    public boolean isFlag() {
        return flag;
    }
    
    public void setFlag(boolean flag) {
        this.flag = flag;
    }

    public ExamLabelType getLabelType() {
        return labelType;
    }

    public void setLabelType(ExamLabelType labelType) {
        this.labelType = labelType;
    }
    
    
    
    @PostConstruct
    public void init() {
        HttpServletRequest request = JsfHelper.getRequest();
        String id = request.getParameter("id");
        if (id != null) {
            this.flag = true;
            this.labelType = this.labelTypeDAO.findExamLabelType(id);
        } else {
            this.labelType = new ExamLabelType();
        }
    }
    
    public String done() {
        if (flag) {
            this.logger.log("修改了标签类别：" + labelType.getName());
            this.labelTypeDAO.updateExamLabelType(labelType);
        } else {
            this.logger.log("添加了标签类别：" + labelType.getName());
            this.labelTypeDAO.addExamLabelType(labelType);
        }
        return "ListExamLabelType?faces-redirect=true";
    }
}
