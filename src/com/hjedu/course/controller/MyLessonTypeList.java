package com.hjedu.course.controller;

import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;

import com.hjedu.course.dao.ILessonTypeLogDAO;
import com.hjedu.course.entity.LessonType;
import com.hjedu.course.entity.LessonTypeLog;
import com.hjedu.customer.entity.BbsUser;
import com.hjedu.platform.controller.ClientSession;
import com.huajie.util.JsfHelper;
import com.huajie.util.SpringHelper;

@ManagedBean
@RequestScoped
public class MyLessonTypeList implements Serializable {
	private static final long serialVersionUID = 1L;
	ILessonTypeLogDAO lessonTypeLogDAO = SpringHelper.getSpringBean("LessonTypeLogDAO");
    List<LessonTypeLog> ltLogs;
    int courseNum;
    double totalFinishNum;

	public int getCourseNum() {
		return courseNum;
	}

	public void setCourseNum(int courseNum) {
		this.courseNum = courseNum;
	}

	public List<LessonTypeLog> getLtLogs() {
		return ltLogs;
	}

	public void setLtLogs(List<LessonTypeLog> ltLogs) {
		this.ltLogs = ltLogs;
	}

	@PostConstruct
    public void init() {
    	ClientSession cs = JsfHelper.getBean("clientSession");
    	BbsUser user = cs.getUsr();
    	if(user==null||user.getLessonTypeStr()==null){
    		return;
    	}
    	ltLogs = lessonTypeLogDAO.findLessonTypeLogByUsr(user.getId());
    	this.courseNum = ltLogs.size();
    	if(ltLogs!=null&&ltLogs.size()>0){
    		for(LessonTypeLog ltl : ltLogs){
    			this.totalFinishNum += ltl.getFinishedClassNum();
    		}
    	}
    }

	public double getTotalFinishNum() {
		return totalFinishNum;
	}

	public void setTotalFinishNum(double totalFinishNum) {
		this.totalFinishNum = totalFinishNum;
	}
}
