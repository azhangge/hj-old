package com.hjedu.platform.controller;

import java.io.IOException;
import java.io.Serializable;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Vector;
import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.hjedu.course.dao.ICourseTypeDAO;
import com.hjedu.course.dao.ILessonTypeDAO;
import com.hjedu.course.entity.LessonType;
import com.hjedu.customer.entity.AdminInfo;
import com.hjedu.customer.entity.BbsUser;
import com.hjedu.examination.entity.CourseType;
import com.hjedu.examination.entity.ExamCase;
import com.hjedu.platform.dao.ISystemConfigDAO;
import com.hjedu.platform.entity.SystemConfig;
import com.huajie.app.util.StringUtil;
import com.huajie.util.CookieUtils;
import com.huajie.util.ExternalUserUtil;
import com.huajie.util.JsfHelper;
import com.huajie.util.JsonUtil;
import com.huajie.util.MyLogger;
import com.huajie.util.SpringHelper;

@ManagedBean
@RequestScoped
public class JTGeneral implements Serializable {

    ILessonTypeDAO lessonTypeDAO = SpringHelper.getSpringBean("LessonTypeDAO");
    ICourseTypeDAO lessonClassifysDAO = SpringHelper.getSpringBean("CourseTypeDAO");
    ICourseTypeDAO CourseTypeDAO = SpringHelper.getSpringBean("CourseTypeDAO");
    List<LessonType> lessonTypes;
    List<LessonType> lessonTypes2;
    LessonType selectedLessonType;
    List<CourseType> labelTypes;
    private List<String> selectedOptions;
    List<ExamCase> examcases;
    List<CourseType> courseTypes;
    List<CourseType> FSCourseTypes;
    private int picChangeTime;

    public int getPicChangeTime() {
		return picChangeTime;
	}

	public void setPicChangeTime(int picChangeTime) {
		this.picChangeTime = picChangeTime;
	}

	public List<CourseType> getFSCourseTypes() {
		return FSCourseTypes;
	}

	public void setFSCourseTypes(List<CourseType> fSCourseTypes) {
		FSCourseTypes = fSCourseTypes;
	}

	public List<CourseType> getCourseTypes() {
		return courseTypes;
	}

	public void setCourseTypes(List<CourseType> courseTypes) {
		this.courseTypes = courseTypes;
	}

	public List<ExamCase> getExamcases() {
		return examcases;
	}

	public void setExamcases(List<ExamCase> examcases) {
		this.examcases = examcases;
	}

	public List<LessonType> getLessonTypes2() {
		return lessonTypes2;
	}

	public void setLessonTypes2(List<LessonType> lessonTypes2) {
		this.lessonTypes2 = lessonTypes2;
	}

	public List<String> getSelectedOptions() {
		return selectedOptions;
	}

	public void setSelectedOptions(List<String> selectedOptions) {
		this.selectedOptions = selectedOptions;
	}

	public List<CourseType> getLabelTypes() {
		return labelTypes;
	}

	public void setLabelTypes(List<CourseType> labelTypes) {
		this.labelTypes = labelTypes;
	}

	public LessonType getSelectedLessonType() {
		return selectedLessonType;
	}

	public void setSelectedLessonType(LessonType selectedLessonType) {
		this.selectedLessonType = selectedLessonType;
	}

	public List<LessonType> getLessonTypes() {
        return lessonTypes;
    }

    public void setLessonTypes(List<LessonType> lessonTypes) {
        this.lessonTypes = lessonTypes;
    }

    
    
    @PostConstruct
    public void init() {
    	//获取企业ID
    	String businessId = CookieUtils.getBusinessId(JsfHelper.getRequest());
    	
    	//轮播图时间间隔
    	ISystemConfigDAO systemConfigDAO = SpringHelper.getSpringBean("SystemConfigDAO");
    	int i = systemConfigDAO.getSystemConfigByBusinessId(businessId).getPicChangeTime();
    	this.picChangeTime = i==0?3*1000:i*1000;
    	
    	//
    	ClientSession cs = JsfHelper.getBean("clientSession");
    	if(cs!=null&&cs.getUsr()!=null){
    		BbsUser user = cs.getUsr();
    		//最新课程
        	this.lessonTypes2 = this.lessonTypeDAO.findLessonTypesByOrder3("availableBegain", businessId, "true", 0, 10000, true, null, false, null,user);
        	//热门课程
        	this.lessonTypes = this.lessonTypeDAO.findLessonTypesByOrder3("userNum",businessId, "true", 0, 10000, true, null, false, null,user);
    	}else{
    		//最新课程
        	this.lessonTypes2 = this.lessonTypeDAO.findLessonTypesByOrder("availableBegain",businessId, "true", 0, 10000, true, null, false, null);
        	//热门课程
        	this.lessonTypes = this.lessonTypeDAO.findLessonTypesByOrder("userNum", businessId, "true", 0, 10000, true, null, false, null);
    	}
    	
    	this.courseTypes = CourseTypeDAO.findNavigationCourseTypeByFatherId(businessId);
        this.FSCourseTypes = CourseTypeDAO.findFrontShowCourseTypeByFatherId(businessId);
    }
    
    public List<LessonType> getLessonTypesByNameAndNum(String name,String beginnum,String endnum){
    	String businessId = CookieUtils.getBusinessId(JsfHelper.getRequest());
    	CourseType lc = lessonClassifysDAO.findCourseTypeByNameAndBusinessId(name, businessId);
    	List<LessonType> ll = null;
    	if(lc!=null){
    		ll = this.lessonTypeDAO.findLessonTypesByTagName(lc.getId(),Integer.parseInt(beginnum),Integer.parseInt(endnum),businessId);
    	}else{
    		return null;
    	}
    	return ll;
    }
    
    public String getCourseTypeIdByNameAndNum(String name){
    	String businessId = CookieUtils.getBusinessId(JsfHelper.getRequest());
    	CourseType lc = lessonClassifysDAO.findCourseTypeByNameAndBusinessId(name,businessId);
    	return lc.getId();
    }
    
    public List<LessonType> getLessonTypesByIdAndNum(String id,String endnum){
    	if(StringUtil.isEmpty(id)){
    		return null;
    	}
    	List<LessonType> ll = new LinkedList<>();
    	for(LessonType lt : this.lessonTypes){
    		if(lt.getLabelStr()!=null&&lt.getLabelStr().contains(id)){
    			ll.add(lt);
    		}
    		if(ll.size()==Integer.parseInt(endnum)){
    			break;
    		}
    	}
    	return ll;
    }
    
    public List<LessonType> getLessonTypesByNum(String beginnum,String endnum){
    	String businessId = CookieUtils.getBusinessId(JsfHelper.getRequest());
    	List<LessonType> ll = this.lessonTypeDAO.findLessonTypesByNumAndBusinessId(Integer.parseInt(beginnum),Integer.parseInt(endnum),businessId);
    	return ll;
    }
    
    public void changeTypes(){
    	String businessId = CookieUtils.getBusinessId(JsfHelper.getRequest());
    	this.lessonTypes = this.lessonTypeDAO.findAllEnableLessonType(businessId);
    	List<LessonType> ll = new Vector<LessonType>();
    	ll.addAll(this.lessonTypes);
    	if(this.selectedOptions!=null&&this.selectedOptions.size()>0){
			for (LessonType t : lessonTypes) {
				for (String type : this.selectedOptions) {
					if (t.getLabelStr()==null||!t.getLabelStr().contains(type)) {
						ll.remove(t);
					}
				}
			}
    	}
    	lessonTypes = ll;
    }
}
