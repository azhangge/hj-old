package com.hjedu.examination.controller;

import java.io.Serializable;
import java.util.Collections;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.model.SelectItem;
import javax.servlet.http.HttpServletRequest;

import com.hjedu.examination.dao.IKnowledgeLabelDAO;
import com.hjedu.examination.dao.IKnowledgeLabelTypeDAO;
import com.hjedu.examination.entity.KnowledgeLabel;
import com.hjedu.examination.entity.KnowledgeLabelType;
import com.hjedu.platform.service.ILogService;
import com.huajie.util.JsfHelper;
import com.huajie.util.SpringHelper;

@ManagedBean
@ViewScoped
public class ListKnowledgeLabel implements Serializable {

    ILogService logger = SpringHelper.getSpringBean("LogService");
    IKnowledgeLabelDAO examDAO = SpringHelper.getSpringBean("KnowledgeLabelDAO");
    List<KnowledgeLabel> labels;
    IKnowledgeLabelTypeDAO labelTypeDAO = SpringHelper.getSpringBean("KnowledgeLabelTypeDAO");
    List<KnowledgeLabelType> labelTypes;
    private SelectItem[] filterOptions;

    public List<KnowledgeLabel> getLabels() {
        return labels;
    }

    public void setLabels(List<KnowledgeLabel> labels) {
        this.labels = labels;
    }

    public List<KnowledgeLabelType> getLabelTypes() {
        return labelTypes;
    }

    public SelectItem[] getFilterOptions() {
        return filterOptions;
    }

    public void setFilterOptions(SelectItem[] filterOptions) {
        this.filterOptions = filterOptions;
    }

    public void setLabelTypes(List<KnowledgeLabelType> labelTypes) {
        this.labelTypes = labelTypes;
    }

    

    @PostConstruct
    public void init() {
        HttpServletRequest request = JsfHelper.getRequest();
        this.labels = this.examDAO.findAllKnowledgeLabel();
        Collections.sort(labels);
        this.labelTypes = this.labelTypeDAO.findAllKnowledgeLabelType();
        Collections.sort(labelTypes);
        this.createFilterOptions();
    }

    public void delete(String id) {
        KnowledgeLabel ee = this.examDAO.findKnowledgeLabel(id);
        this.logger.log("删除了知识点标签：" + ee.getName());
        this.examDAO.deleteKnowledgeLabel(id);
        this.labels = this.examDAO.findAllKnowledgeLabel();
        Collections.sort(labels);
        Collections.reverse(labels);
    }
    
    
    public String editOrd(String id) {
        for (KnowledgeLabel cq : this.labels) {
            if (id.equals(cq.getId())) {
                this.examDAO.updateKnowledgeLabel(cq);
                break;
            }
        }
        return null;
    }
    
    private void createFilterOptions() {
        SelectItem[] options = new SelectItem[this.labelTypes.size()+1];
        options[0] = new SelectItem("", "请选择");
        if (!this.labelTypes.isEmpty()) {
            int i = 1;
            for (KnowledgeLabelType lt : this.labelTypes) {
                options[i] = new SelectItem(lt.getId(), lt.getName());
                i++;
            }
        }
        this.filterOptions = options;
    }
}
