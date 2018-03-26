package com.hjedu.course.controller;

import java.io.Serializable;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.model.SelectItem;

import com.hjedu.course.dao.ILessonDAO;
import com.hjedu.course.dao.ILessonTypeDAO;
import com.hjedu.course.entity.Lesson;
import com.hjedu.course.entity.LessonType;
import com.hjedu.customer.controller.AdminSessionBean;
import com.hjedu.customer.entity.AdminInfo;
import com.hjedu.platform.service.ILogService;
import com.huajie.util.CookieUtils;
import com.huajie.util.JsfHelper;
import com.huajie.util.SpringHelper;

@ManagedBean
@ViewScoped
public class ListLesson implements Serializable {

    ILogService logger = SpringHelper.getSpringBean("LogService");
    ILessonDAO examDAO = SpringHelper.getSpringBean("LessonDAO");
    ILessonTypeDAO lessonTypeDAO = SpringHelper.getSpringBean("LessonTypeDAO");
    List<LessonType> lessonTypes;
    List<Lesson> exams;
    private SelectItem[] filterOptions;

    public List<Lesson> getExams() {
        return exams;
    }

    public void setExams(List<Lesson> exams) {
        this.exams = exams;
    }

    public List<LessonType> getLessonTypes() {
        return lessonTypes;
    }

    public void setLessonTypes(List<LessonType> lessonTypes) {
        this.lessonTypes = lessonTypes;
    }

    public SelectItem[] getFilterOptions() {
        return filterOptions;
    }

    public void setFilterOptions(SelectItem[] filterOptions) {
        this.filterOptions = filterOptions;
    }

    @PostConstruct
    public void init() {
    	loadData();
        this.createFilterOptions();
    }
    
    private void loadData(){
    	AdminSessionBean asb = JsfHelper.getBean("adminSessionBean");
        if (asb != null) {
            AdminInfo ai = asb.getAdmin();
            if (ai != null&&ai.getGrp().equals("company")) {
                this.exams = this.examDAO.findLessonByAdmin(ai);
                List<Lesson> lessons=this.examDAO.findAllUntypedLesson();
                exams.addAll(lessons);
                this.lessonTypes = this.lessonTypeDAO.findLessonTypeByAdmin(ai,CookieUtils.getBusinessId(JsfHelper.getRequest()));
            } else {
                this.exams = this.examDAO.findAllLesson();
                this.lessonTypes = this.lessonTypeDAO.findAllLessonType(CookieUtils.getBusinessId(JsfHelper.getRequest()));
            }
        }
    }

    public void delete(String id) {
        this.examDAO.deleteLesson(id);
        this.loadData();
    }

    public String editOrd(String id) {
        for (Lesson cq : this.exams) {
            if (id.equals(cq.getId())) {
                this.examDAO.updateLesson(cq);
                break;
            }
        }
        return null;
    }

    private void createFilterOptions() {
        SelectItem[] options = new SelectItem[this.lessonTypes.size() + 1];
        options[0] = new SelectItem("", "请选择");
        if (!this.lessonTypes.isEmpty()) {
            int i = 1;
            for (LessonType lt : this.lessonTypes) {
                options[i] = new SelectItem(lt.getId(), lt.getName());
                i++;
            }
        }
        this.filterOptions = options;
    }
}
