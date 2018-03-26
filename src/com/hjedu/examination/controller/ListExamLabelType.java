package com.hjedu.examination.controller;


import com.google.gson.annotations.Expose;
import com.hjedu.examination.dao.IExamLabelTypeDAO;
import com.hjedu.examination.entity.ExamLabelType;
import com.hjedu.platform.service.ILogService;
import com.huajie.util.CookieUtils;
import com.huajie.util.JsfHelper;
import com.huajie.util.SpringHelper;

import java.io.Serializable;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

@ManagedBean
@ViewScoped
public class ListExamLabelType implements Serializable {

    ILogService logger = SpringHelper.getSpringBean("LogService");
    IExamLabelTypeDAO labelTypeDAO = SpringHelper.getSpringBean("ExamLabelTypeDAO");
    @Expose
    List<ExamLabelType> labelTypes;
    
    ExamLabelType pt = new ExamLabelType();
    Map typeMap = new HashMap();
    

    public List<ExamLabelType> getLabelTypes() {
        return labelTypes;
    }

    public void setLabelTypes(List<ExamLabelType> labelTypes) {
        this.labelTypes = labelTypes;
    }

    public ExamLabelType getPt() {
        return pt;
    }

    public void setPt(ExamLabelType pt) {
        this.pt = pt;
    }

    public Map getTypeMap() {
        return typeMap;
    }

    public void setTypeMap(Map typeMap) {
        this.typeMap = typeMap;
    }

    

    @PostConstruct
    public void init() {
    	String businessId = CookieUtils.getBusinessId(JsfHelper.getRequest());
        this.labelTypes = this.labelTypeDAO.findAllExamLabelTypeByBusinessId(businessId);
        Collections.sort(labelTypes);
    }

    public void delete(String id) {
    	String businessId = CookieUtils.getBusinessId(JsfHelper.getRequest());
        ExamLabelType er=this.labelTypeDAO.findExamLabelType(id);
        this.logger.log("删除了考试标签类别："+er.getName());
        this.labelTypeDAO.deleteExamLabelType(id);
        this.labelTypes = this.labelTypeDAO.findAllExamLabelTypeByBusinessId(businessId);
    }
    
    
    
    public String addType() {
        logger.log("添加了考试标签类别：" + pt.getName() + ",ID:" + pt.getId());
        labelTypeDAO.addExamLabelType(pt);
        this.pt = new ExamLabelType();
        this.synType();
        return null;
    }

    public String batchUpdateType() {
        for (ExamLabelType p : this.labelTypes) {
            labelTypeDAO.updateExamLabelType(p);
        }
        logger.log("批量修改了考试标签类别。");
        this.synType();
        return null;
    }

    public void synType() {
    	String businessId = CookieUtils.getBusinessId(JsfHelper.getRequest());
        this.labelTypes = labelTypeDAO.findAllExamLabelTypeByBusinessId(businessId);
        this.typeMap.clear();
        for (ExamLabelType p : labelTypes) {
            typeMap.put(p.getId(), p.getName());
        }
    }
    
    public String editOrd(String id) {
        for (ExamLabelType cq : this.labelTypes) {
            if (id.equals(cq.getId())) {
                this.labelTypeDAO.updateExamLabelType(cq);
                break;
            }
        }
        return null;
    }
    
}
