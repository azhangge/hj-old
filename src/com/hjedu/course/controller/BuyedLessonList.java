package com.hjedu.course.controller;

import com.google.gson.annotations.Expose;
import com.hjedu.course.dao.ILessonLogDAO;
import com.hjedu.course.entity.LessonLog;
import com.hjedu.customer.entity.BbsUser;
import com.hjedu.platform.controller.ClientSession;
import com.huajie.util.CookieUtils;
import com.huajie.util.JsfHelper;
import com.huajie.util.SpringHelper;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

@ManagedBean
@ViewScoped
public class BuyedLessonList implements Serializable {
    @Expose
    List<LessonLog> lessonTrades = new ArrayList();
    ILessonLogDAO lessonLogDAO = SpringHelper.getSpringBean("LessonLogDAO");
    
    public List<LessonLog> getLessonTrades() {
        return lessonTrades;
    }
    
    public void setLessonTrades(List<LessonLog> lessonTrades) {
        this.lessonTrades = lessonTrades;
    }
    
    @PostConstruct
    public void init() {
        ClientSession cs = JsfHelper.getBean("clientSession");
        BbsUser user = cs.getUsr();
        this.synDB(user);
    }
    
    public void synDB(BbsUser user) {
    	String bussinessId = CookieUtils.getBusinessId(JsfHelper.getRequest());
        if (user != null) {
            List<LessonLog> lls = this.lessonLogDAO.findLessonLogByUsr(user.getId());
            for (LessonLog l : lls) {
                if (!l.getLesson().isRepeatBuy()) {
                    this.lessonTrades.add(l);
                }
            }
            
        }
    }
    
    public String deleteTrade(String id) {
    	String bussinessId = CookieUtils.getBusinessId(JsfHelper.getRequest());
        ClientSession cs = JsfHelper.getBean("clientSession");
        BbsUser user = cs.getUsr();
        if (user != null) {
            this.lessonLogDAO.deleteLessonLog(id);
            this.lessonTrades = this.lessonLogDAO.findLessonLogByUsr(user.getId());
        }
        return null;
    }
    
}
