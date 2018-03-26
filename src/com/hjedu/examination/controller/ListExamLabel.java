package com.hjedu.examination.controller;

import java.io.Serializable;
import java.util.Collections;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.model.SelectItem;
import javax.servlet.http.HttpServletRequest;

import com.hjedu.course.entity.LessonType;
import com.hjedu.examination.dao.IExamLabelDAO;
import com.hjedu.examination.dao.IExamLabelTypeDAO;
import com.hjedu.examination.entity.ExamLabel;
import com.hjedu.examination.entity.ExamLabelType;
import com.hjedu.platform.service.ILogService;
import com.huajie.util.CookieUtils;
import com.huajie.util.JsfHelper;
import com.huajie.util.SpringHelper;

@ManagedBean
@ViewScoped
public class ListExamLabel implements Serializable {

    ILogService logger = SpringHelper.getSpringBean("LogService");
    IExamLabelDAO examDAO = SpringHelper.getSpringBean("ExamLabelDAO");
    List<ExamLabel> labels;
    IExamLabelTypeDAO labelTypeDAO = SpringHelper.getSpringBean("ExamLabelTypeDAO");
    List<ExamLabelType> labelTypes;
    private SelectItem[] filterOptions;

    public List<ExamLabel> getLabels() {
        return labels;
    }

    public void setLabels(List<ExamLabel> labels) {
        this.labels = labels;
    }

    public List<ExamLabelType> getLabelTypes() {
        return labelTypes;
    }

    public SelectItem[] getFilterOptions() {
        return filterOptions;
    }

    public void setFilterOptions(SelectItem[] filterOptions) {
        this.filterOptions = filterOptions;
    }

    public void setLabelTypes(List<ExamLabelType> labelTypes) {
        this.labelTypes = labelTypes;
    }

    

    @PostConstruct
    public void init() {
        HttpServletRequest request = JsfHelper.getRequest();
        String businessId = CookieUtils.getBusinessId(request);
        this.labels = this.examDAO.findAllExamLabel();
        Collections.sort(labels);
        this.labelTypes = this.labelTypeDAO.findAllExamLabelTypeByBusinessId(businessId);
        Collections.sort(labelTypes);
        this.createFilterOptions();
    }

    public void delete(String id) {
        ExamLabel ee = this.examDAO.findExamLabel(id);
        this.logger.log("删除了考试标签：" + ee.getName());
        this.examDAO.deleteExamLabel(id);
        this.labels = this.examDAO.findAllExamLabel();
        Collections.sort(labels);
        Collections.reverse(labels);
    }
    
    
    public String editOrd(String id) {
        for (ExamLabel cq : this.labels) {
            if (id.equals(cq.getId())) {
                this.examDAO.updateExamLabel(cq);
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
            for (ExamLabelType lt : this.labelTypes) {
                options[i] = new SelectItem(lt.getId(), lt.getName());
                i++;
            }
        }
        this.filterOptions = options;
    }
}
