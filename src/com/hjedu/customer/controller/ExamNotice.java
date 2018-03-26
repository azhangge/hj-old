package com.hjedu.customer.controller;

import java.io.Serializable;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

import org.apache.log4j.Logger;

import com.hjedu.course.dao.ILessonLogDAO;
import com.hjedu.course.dao.ILessonTypeLogDAO;
import com.hjedu.course.entity.LessonType;
import com.hjedu.course.lazy.LazyLessonType;
import com.hjedu.customer.dao.IBbsUserDAO;
import com.hjedu.customer.entity.BbsUser;
import com.hjedu.platform.controller.ClientSession;
import com.huajie.app.util.FileUtil;
import com.huajie.app.util.StringUtil;
import com.huajie.util.JsfHelper;
import com.huajie.util.SpringHelper;

@ManagedBean
@ViewScoped
public class ExamNotice implements Serializable {
	private static final long serialVersionUID = 1L;
	private static final Logger log4j = Logger.getLogger(ExamNotice.class);
	
	private LazyLessonType activeCourses;
	private LazyLessonType collectCourses;
	private LazyLessonType boughtCourses;
	
	public LazyLessonType getActiveCourses() {
		return activeCourses;
	}

	public void setActiveCourses(LazyLessonType activeCourses) {
		this.activeCourses = activeCourses;
	}

	public LazyLessonType getCollectCourses() {
		return collectCourses;
	}

	public void setCollectCourses(LazyLessonType collectCourses) {
		this.collectCourses = collectCourses;
	}

	public LazyLessonType getBoughtCourses() {
		return boughtCourses;
	}

	public void setBoughtCourses(LazyLessonType boughtCourses) {
		this.boughtCourses = boughtCourses;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public static Logger getLog4j() {
		return log4j;
	}

	@PostConstruct
    public void init() {
		ClientSession cs = JsfHelper.getBean("clientSession");
    	BbsUser user = cs.getUsr();
    	if(user==null||user.getLessonTypeStr()==null){
    		return;
    	}
    	String acs = user.getLessonTypeStr();
    	if(StringUtil.isNotEmpty(acs)){
    		String acs2 = "('";
    		String[] acsarray = acs.split(";");
    		for(String a : acsarray){
    			acs2 = acs2+a+"','";
    		}
    		acs2 = acs2.substring(0, acs2.length()-2)+")";
    		activeCourses = new LazyLessonType(" l.id in "+acs2+" and l.totalScorePaid=0 ");
    		boughtCourses = new LazyLessonType(" l.id in "+acs2+" and l.totalScorePaid!=0 ");
    	}else{
    		activeCourses = null;
    		boughtCourses = null;
    	}
    	String ccs = user.getCollectionCourses();
    	if(StringUtil.isNotEmpty(ccs)){
    		String ccs2 = "('";
    		String[] ccsarray = ccs.split(";");
    		for(String a : ccsarray){
    			ccs2 = ccs2+a+"','";
    		}
    		ccs2 = ccs2.substring(0, ccs2.length()-2)+")";
    		collectCourses = new LazyLessonType(" l.id in "+ccs2+" ");
    	}else{
    		collectCourses = null;
    	}
	}
	
	public String cancle(int type){
		ClientSession cs = JsfHelper.getBean("clientSession");
    	BbsUser user = cs.getUsr();
    	if(user==null||user.getLessonTypeStr()==null){
    		return "";
    	}
		if(type==1){
			@SuppressWarnings("unchecked")
			List<LessonType> list = (List<LessonType>) collectCourses.getWrappedData();
			String ids = user.getCollectionCourses();
			for(LessonType lt : list){
				if(lt.isSelected()){
					ids = FileUtil.removePartOfStr(ids,lt.getId());
					user.setCollectionCourses(ids);
				}
			}
			IBbsUserDAO bbsUserDAO = SpringHelper.getSpringBean("BbsUserDAO");
			bbsUserDAO.updateBbsUser(user);
		}else if(type==2){
			List<LessonType> list = (List<LessonType>) activeCourses.getWrappedData();
			String ids = user.getLessonTypeStr();
			for(LessonType lt : list){
				if(lt.isSelected()){
					ids = FileUtil.removePartOfStr(ids,lt.getId());
					user.setLessonTypeStr(ids);
					ILessonTypeLogDAO ltLogDAO = SpringHelper.getSpringBean("LessonTypeLogDAO");
					ltLogDAO.deleteLogByLessonTypeAndUser(lt.getId(), user.getId());
					ILessonLogDAO lLogDAO = SpringHelper.getSpringBean("LessonLogDAO");
					lLogDAO.deleteLogByUserAndType(user.getId(), lt.getId());
				}
			}
			IBbsUserDAO bbsUserDAO = SpringHelper.getSpringBean("BbsUserDAO");
			bbsUserDAO.updateBbsUser(user);
		}
		init();
		return "";
	}
}
