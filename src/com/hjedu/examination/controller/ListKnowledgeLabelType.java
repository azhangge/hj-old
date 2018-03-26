package com.hjedu.examination.controller;


import com.google.gson.annotations.Expose;
import com.hjedu.examination.dao.IKnowledgeLabelTypeDAO;
import com.hjedu.examination.entity.KnowledgeLabelType;
import com.hjedu.platform.service.ILogService;
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
public class ListKnowledgeLabelType implements Serializable {

    ILogService logger = SpringHelper.getSpringBean("LogService");
    IKnowledgeLabelTypeDAO labelTypeDAO = SpringHelper.getSpringBean("KnowledgeLabelTypeDAO");
    @Expose
    List<KnowledgeLabelType> labelTypes;
    
    KnowledgeLabelType pt = new KnowledgeLabelType();
    Map typeMap = new HashMap();
    

    public List<KnowledgeLabelType> getLabelTypes() {
        return labelTypes;
    }

    public void setLabelTypes(List<KnowledgeLabelType> labelTypes) {
        this.labelTypes = labelTypes;
    }

    public KnowledgeLabelType getPt() {
        return pt;
    }

    public void setPt(KnowledgeLabelType pt) {
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
        this.labelTypes = this.labelTypeDAO.findAllKnowledgeLabelType();
        Collections.sort(labelTypes);
    }

    public void delete(String id) {
        KnowledgeLabelType er=this.labelTypeDAO.findKnowledgeLabelType(id);
        this.logger.log("删除了知识点标签类别："+er.getName());
        this.labelTypeDAO.deleteKnowledgeLabelType(id);
        this.labelTypes = this.labelTypeDAO.findAllKnowledgeLabelType();
    }
    
    
    
    public String addType() {
        logger.log("添加了知识点标签类别：" + pt.getName() + ",ID:" + pt.getId());
        labelTypeDAO.addKnowledgeLabelType(pt);
        this.pt = new KnowledgeLabelType();
        this.synType();
        return null;
    }

    public String batchUpdateType() {
        for (KnowledgeLabelType p : this.labelTypes) {
            labelTypeDAO.updateKnowledgeLabelType(p);
        }
        logger.log("批量修改了知识点标签类别。");
        this.synType();
        return null;
    }

    public void synType() {
        this.labelTypes = labelTypeDAO.findAllKnowledgeLabelType();
        this.typeMap.clear();
        for (KnowledgeLabelType p : labelTypes) {
            typeMap.put(p.getId(), p.getName());
        }
    }
    
    public String editOrd(String id) {
        for (KnowledgeLabelType cq : this.labelTypes) {
            if (id.equals(cq.getId())) {
                this.labelTypeDAO.updateKnowledgeLabelType(cq);
                break;
            }
        }
        return null;
    }
    
}
