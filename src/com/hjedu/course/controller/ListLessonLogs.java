package com.hjedu.course.controller;

import java.io.Serializable;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.servlet.http.HttpServletRequest;

import com.hjedu.course.dao.ILessonLogDAO;
import com.hjedu.course.entity.LessonLog;
import com.hjedu.platform.service.ILogService;
import com.huajie.util.CookieUtils;
import com.huajie.util.JsfHelper;
import com.huajie.util.SpringHelper;

@ManagedBean
@ViewScoped
public class ListLessonLogs implements Serializable {

    ILogService logger = SpringHelper.getSpringBean("LogService");
    ILessonLogDAO lessonLogDAO = SpringHelper.getSpringBean("LessonLogDAO");
    List<LessonLog> lessonLogs;

    public List<LessonLog> getLessonLogs() {
        return lessonLogs;
    }

    public void setLessonLogs(List<LessonLog> lessonLogs) {
        this.lessonLogs = lessonLogs;
    }
    
    

    @PostConstruct
    public void init() {
        HttpServletRequest request = JsfHelper.getRequest();
        this.lessonLogs = this.lessonLogDAO.findAllLessonLog(CookieUtils.getBusinessId(JsfHelper.getRequest()));
    }

    public void delete(String id) {
        LessonLog ll = this.lessonLogDAO.findLessonLog(id);
        this.logger.log("删除了一条课程购买记录，课程及用户是：" + ll.getLesson().getName() + "," + ll.getUser().getName());
        this.lessonLogDAO.deleteLessonLog(id);
        this.lessonLogs = this.lessonLogDAO.findAllLessonLog(CookieUtils.getBusinessId(JsfHelper.getRequest()));
    }

    public void delAll() {
        this.logger.log("清空了课程购买记录");
        this.lessonLogDAO.deleteAll();
        this.lessonLogs = this.lessonLogDAO.findAllLessonLog(CookieUtils.getBusinessId(JsfHelper.getRequest()));
    }

    public void batchDel() {
        for (LessonLog c : this.lessonLogs) {
            if (c.isSelected()) {
                this.logger.log("删除了一条课程购买记录，课程及用户是：" + c.getLesson().getName() + "," + c.getUser().getName());
                this.lessonLogDAO.deleteLessonLog(c.getId());
            }
        }
        this.lessonLogs = this.lessonLogDAO.findAllLessonLog(CookieUtils.getBusinessId(JsfHelper.getRequest()));
    }

}
