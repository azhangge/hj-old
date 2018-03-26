package com.hjedu.course.controller;

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
public class UserCourses implements Serializable {
	private static final long serialVersionUID = 1L;
	private static final Logger log4j = Logger.getLogger(UserCourses.class);
	
	private LazyLessonType activeCourses;
	private LazyLessonType collectCourses;
	private LazyLessonType boughtCourses;
	private LazyLessonType secretCourses;
	
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
	
	public LazyLessonType getSecretCourses() {
		return secretCourses;
	}

	public void setSecretCourses(LazyLessonType secretCourses) {
		this.secretCourses = secretCourses;
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
    	loadCollectCourses(user);
    	loadBoughtCourses(user);
    	loadActiveCourses(user);
    	loadSecretCourses(user);
	}
	
	private void loadCollectCourses(BbsUser user){
		String acs = user.getCollectionCourses();
    	if(StringUtil.isNotEmpty(acs)){
    		String acs2 = getInSqlString(acs, ";");
    		collectCourses = new LazyLessonType(" l.id in "+acs2+" and l.enabled=true");
    	}else{
    		collectCourses = null;
    	}
	}
	
	private void loadBoughtCourses(BbsUser user){
		String acs = user.getLessonTypeStr();
    	if(StringUtil.isNotEmpty(acs)){
    		String acs2 = getInSqlString(acs, ";");
    		boughtCourses = new LazyLessonType(" l.id in "+acs2+" and l.totalScorePaid>0 and l.enabled=true and l.secreted<>1");
    	}else{
    		boughtCourses = null;
    	}
	}
	
	private void loadActiveCourses(BbsUser user){
		String acs = user.getLessonTypeStr();
    	if(StringUtil.isNotEmpty(acs)){
    		String acs2 = getInSqlString(acs, ";");
    		activeCourses = new LazyLessonType(" l.id in "+acs2+" and l.totalScorePaid=0 and l.enabled=true and l.secreted<>1");
    	}else{
    		activeCourses = null;
    	}
	}
	
	private void loadSecretCourses(BbsUser user){
		String acs = user.getLessonTypeStr();
    	if(StringUtil.isNotEmpty(acs)){
    		String acs2 = getInSqlString(acs, ";");
    		secretCourses = new LazyLessonType(" l.id in "+acs2+" and l.secreted=1 and l.enabled=true ");
    	}else{
    		secretCourses = null;
    	}
	}
	
	private String getInSqlString(String acs,String split){
		String acs2 = "('";
		String[] acsarray = acs.split(split);
		for(String a : acsarray){
			acs2 = acs2+a+"','";
		}
		acs2 = acs2.substring(0, acs2.length()-2)+")";
		return acs2;
	}
	
	public String cancle(int type){
		ClientSession cs = JsfHelper.getBean("clientSession");
    	BbsUser user = cs.getUsr();
    	if(user==null||user.getLessonTypeStr()==null){
    		return "";
    	}
    	boolean ifselect = false;
		if(type==1&&collectCourses!=null){
			@SuppressWarnings("unchecked")
			List<LessonType> list = (List<LessonType>) collectCourses.getWrappedData();
			if(list!=null){
				String ids = user.getCollectionCourses();
				for(LessonType lt : list){
					if(lt.isSelected()){
						ids = FileUtil.removePartOfStr(ids,lt.getId());
						user.setCollectionCourses(ids);
						ifselect = true;
					}
				}
				IBbsUserDAO bbsUserDAO = SpringHelper.getSpringBean("BbsUserDAO");
				bbsUserDAO.updateBbsUser(user);
				loadCollectCourses(user);
			}
		}else if(type==2&&activeCourses!=null){
			@SuppressWarnings("unchecked")
			List<LessonType> list = (List<LessonType>) activeCourses.getWrappedData();
			if(list!=null){
				String ids = user.getLessonTypeStr();
				for(LessonType lt : list){
					if(lt.isSelected()){
						ids = FileUtil.removePartOfStr(ids,lt.getId());
						user.setLessonTypeStr(ids);
						ILessonTypeLogDAO ltLogDAO = SpringHelper.getSpringBean("LessonTypeLogDAO");
						ltLogDAO.deleteLogByLessonTypeAndUser(lt.getId(), user.getId());
						ILessonLogDAO lLogDAO = SpringHelper.getSpringBean("LessonLogDAO");
						lLogDAO.deleteLogByUserAndType(user.getId(), lt.getId());
						ifselect = true;
					}
				}
				IBbsUserDAO bbsUserDAO = SpringHelper.getSpringBean("BbsUserDAO");
				bbsUserDAO.updateBbsUser(user);
				loadActiveCourses(user);
			}
		}
		if(!ifselect){
			JsfHelper.error("请选择课程！");
			return "";
		}
		return "";
	}
}
