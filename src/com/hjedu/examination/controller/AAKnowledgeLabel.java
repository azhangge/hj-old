package com.hjedu.examination.controller;

import java.io.Serializable;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.servlet.http.HttpServletRequest;

import com.hjedu.customer.controller.AdminSessionBean;
import com.hjedu.customer.dao.IAdminDAO;
import com.hjedu.examination.dao.IKnowledgeLabelDAO;
import com.hjedu.examination.dao.IKnowledgeLabelTypeDAO;
import com.hjedu.examination.entity.KnowledgeLabel;
import com.hjedu.examination.entity.KnowledgeLabelType;
import com.hjedu.platform.service.ILogService;
import com.huajie.util.JsfHelper;
import com.huajie.util.SpringHelper;

@ManagedBean
@ViewScoped
public class AAKnowledgeLabel implements Serializable {

    ILogService logger = SpringHelper.getSpringBean("LogService");
    IKnowledgeLabelDAO examDAO = SpringHelper.getSpringBean("KnowledgeLabelDAO");
    IKnowledgeLabelTypeDAO lessonTypeDAO = SpringHelper.getSpringBean("KnowledgeLabelTypeDAO");
    IAdminDAO adminDAO = SpringHelper.getSpringBean("AdminDAO");
    KnowledgeLabel label;

    String typeId = "0";
    List<KnowledgeLabelType> types;
    boolean flag = false;


    public boolean isFlag() {
        return flag;
    }

    public void setFlag(boolean flag) {
        this.flag = flag;
    }

    public String getTypeId() {
        return typeId;
    }

    public void setTypeId(String typeId) {
        this.typeId = typeId;
    }

    public KnowledgeLabel getLabel() {
        return label;
    }

    public void setLabel(KnowledgeLabel label) {
        this.label = label;
    }

    public List<KnowledgeLabelType> getTypes() {
        return types;
    }

    public void setTypes(List<KnowledgeLabelType> types) {
        this.types = types;
    }

    @PostConstruct
    public void init() {
        HttpServletRequest request = JsfHelper.getRequest();
        String id = request.getParameter("id");
        if (id != null) {
            this.flag = true;
            this.label = this.examDAO.findKnowledgeLabel(id);
        } else {
            this.label = new KnowledgeLabel();
            //this.buildModuleKnowledgeLabels(exam);
        }

        //加载课程类别
        this.types = this.lessonTypeDAO.findAllKnowledgeLabelType();
        //设置当前课程的类别
        if (this.label.getLabelType() != null) {
            this.typeId = this.label.getLabelType().getId();
        }
    }

   


    public String done() {
        //设置课程的类型
        KnowledgeLabelType lt = this.lessonTypeDAO.findKnowledgeLabelType(typeId);
        if (lt != null) {
            this.label.setLabelType(lt);
        }
        if (flag) {
            this.logger.log("修改了知识点标签：" + label.getName());
            this.examDAO.updateKnowledgeLabel(label);
        } else {
            this.logger.log("添加了知识点标签：" + label.getName());
            this.examDAO.addKnowledgeLabel(label);
        }
        AdminSessionBean asb = JsfHelper.getBean("adminSessionBean");
        asb.refreshAdmin();
        return "ListKnowledgeLabel?faces-redirect=true";
    }
}
