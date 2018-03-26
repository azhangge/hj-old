package com.hjedu.examination.controller;


import java.io.Serializable;
import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.servlet.http.HttpServletRequest;

import com.hjedu.examination.dao.IKnowledgeLabelTypeDAO;
import com.hjedu.examination.entity.KnowledgeLabelType;
import com.hjedu.platform.service.ILogService;
import com.huajie.util.JsfHelper;
import com.huajie.util.SpringHelper;

@ManagedBean
@ViewScoped
public class AAKnowledgeLabelType implements Serializable {

    ILogService logger = SpringHelper.getSpringBean("LogService");
    IKnowledgeLabelTypeDAO labelTypeDAO = SpringHelper.getSpringBean("KnowledgeLabelTypeDAO");
    KnowledgeLabelType labelType = null;
    boolean flag = false;
    
    public boolean isFlag() {
        return flag;
    }
    
    public void setFlag(boolean flag) {
        this.flag = flag;
    }

    public KnowledgeLabelType getLabelType() {
        return labelType;
    }

    public void setLabelType(KnowledgeLabelType labelType) {
        this.labelType = labelType;
    }
    
    
    
    @PostConstruct
    public void init() {
        HttpServletRequest request = JsfHelper.getRequest();
        String id = request.getParameter("id");
        if (id != null) {
            this.flag = true;
            this.labelType = this.labelTypeDAO.findKnowledgeLabelType(id);
        } else {
            this.labelType = new KnowledgeLabelType();
        }
    }
    
    public String done() {
        if (flag) {
            this.logger.log("修改了知识点标签类别：" + labelType.getName());
            this.labelTypeDAO.updateKnowledgeLabelType(labelType);
        } else {
            this.logger.log("添加了知识点标签类别：" + labelType.getName());
            this.labelTypeDAO.addKnowledgeLabelType(labelType);
        }
        return "ListKnowledgeLabelType?faces-redirect=true";
    }
}
