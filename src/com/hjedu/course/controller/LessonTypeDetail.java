package com.hjedu.course.controller;

import java.io.Serializable;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;

import com.google.gson.annotations.Expose;
import com.hjedu.course.dao.ILessonDAO;
import com.hjedu.course.dao.ILessonLogDAO;
import com.hjedu.course.dao.ILessonTypeDAO;
import com.hjedu.course.entity.Lesson;
import com.hjedu.course.entity.LessonLog;
import com.hjedu.course.entity.LessonType;
import com.hjedu.course.service.IBuyCourseService;
import com.hjedu.customer.dao.IBbsUserDAO;
import com.hjedu.customer.entity.BbsUser;
import com.hjedu.platform.controller.ClientSession;
import com.hjedu.platform.service.IBbsScoreLogService;
import com.huajie.app.util.StringUtil;
import com.huajie.util.CookieUtils;
import com.huajie.util.JsfHelper;
import com.huajie.util.SpringHelper;

@ManagedBean
@ViewScoped
public class LessonTypeDetail implements Serializable {
    
    BbsUser usr = null;
    LessonType lessonType;
    
    private IBbsUserDAO userDAO = SpringHelper.getSpringBean("BbsUserDAO");
    private ILessonDAO lessonDAO = SpringHelper.getSpringBean("LessonDAO");
    private ILessonLogDAO lessonLogDAO = SpringHelper.getSpringBean("LessonLogDAO");
    private ILessonTypeDAO lessonTypeDAO = SpringHelper.getSpringBean("LessonTypeDAO");
    
    public BbsUser getUsr() {
        return usr;
    }
    
    public void setUsr(BbsUser usr) {
        this.usr = usr;
    }

    public LessonType getLessonType() {
		return lessonType;
	}

	public void setLessonType(LessonType lessonType) {
		this.lessonType = lessonType;
	}

	@PostConstruct
    public void init() {
		
	}
	
	public void buy(){
		HttpServletRequest request = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
        ClientSession cs = JsfHelper.getBean("clientSession");
        this.usr = cs.getUsr();
        if (this.usr != null) {
            String temp = request.getParameter("tid");
            if (temp != null) {
            	this.lessonType=this.lessonTypeDAO.findLessonType(temp);
            	//需要获取用户最新积分，缓存中的可能不是最新数据
            	this.usr = this.userDAO.findBbsUser(usr.getId());
            	IBuyCourseService BuyCourseService  = SpringHelper.getSpringBean("BuyCourseService");
            	BuyCourseService.buyCourse2(this.lessonType, this.usr);
//                return this.initx(temp, usr, request);
            }
        }
	}
    
    public String initx(String tid, BbsUser bu, HttpServletRequest request) {
        this.usr = bu;
        if (this.usr != null) {
            if (tid != null) {
            	this.lessonType=this.lessonTypeDAO.findLessonType(tid);
            }
            String lts = bu.getLessonTypeStr();
            if(StringUtil.isEmpty(lts)){
            	lts = tid+";";
            }else if(!lts.contains(tid)){
            	lts = lts+tid+";";
            }
            bu.setLessonTypeStr(lts);
            userDAO.updateBbsUser(bu);
            List<Lesson> lessons=this.lessonDAO.findAllLessonByType(tid);
            if(lessons.size()>0){
            	for(Lesson ls:lessons){
                	//如果发现还没有购买此课程，或者此课程需要重复购买，就消耗积分
                	List<LessonLog> logs =this.lessonLogDAO.findLessonLogByLessonAndUsr(ls.getId(), usr.getId());
                	 if (logs.isEmpty()) {
                         //this.usr.setScore(this.usr.getScore() - lesson.getScorePaid());
                         IBbsScoreLogService bsl = SpringHelper.getSpringBean("BbsScoreLogService");
                         bsl.log("课程学习消耗积分", (int) (-1 * ls.getScorePaid()));
                         //this.userDAO.updateBbsUser(this.usr);
                         //加入购买记录
                         LessonLog log = new LessonLog();
                         log.setUser(usr);
                         log.setLesson(ls);
                         log.setScorePaid(ls.getScorePaid());
                         log.setBusinessId(CookieUtils.getBusinessId(JsfHelper.getRequest()));
                         this.lessonLogDAO.addLessonLog(log);
                     } 
                }
            }
        }
        return "LessonList.jspx?tid="+tid+"&faces-redirect=true";
    }
    
}
