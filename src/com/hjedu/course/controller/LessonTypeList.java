package com.hjedu.course.controller;

import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.util.List;
import java.util.Vector;
import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.bean.ViewScoped;
import javax.servlet.http.HttpServletRequest;

import com.hjedu.course.dao.ICourseTypeDAO;
import com.hjedu.course.dao.ILessonTypeDAO;
import com.hjedu.course.entity.LessonType;
import com.hjedu.customer.entity.BbsUser;
import com.hjedu.examination.entity.CourseType;
import com.hjedu.platform.controller.ClientSession;
import com.huajie.util.CookieUtils;
import com.huajie.util.JsfHelper;
import com.huajie.util.SpringHelper;

@ManagedBean
//@RequestScoped
@ViewScoped
public class LessonTypeList implements Serializable {

    ILessonTypeDAO lessonTypeDAO = SpringHelper.getSpringBean("LessonTypeDAO");
    List<LessonType> lessonTypes;
    List<CourseType> courseTypes;
    LessonType selectedLessonType;
    private List<String> selectedOptions;
    String searchName = "课程搜索";
    String info = "";
    String lid;
    
    public List<CourseType> getCourseTypes() {
		return courseTypes;
	}

	public void setCourseTypes(List<CourseType> courseTypes) {
		this.courseTypes = courseTypes;
	}

	public String getInfo() {
		return info;
	}

	public void setInfo(String info) {
		this.info = info;
	}

    public String getSearchName() {
		return searchName;
	}

	public void setSearchName(String searchName) {
		this.searchName = searchName;
	}

	public List<String> getSelectedOptions() {
		return selectedOptions;
	}

	public void setSelectedOptions(List<String> selectedOptions) {
		this.selectedOptions = selectedOptions;
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

    public String getLid() {
		return lid;
	}

	public void setLid(String lid) {
		this.lid = lid;
	}

	@PostConstruct
    public void init() {
    	String lid = JsfHelper.getRequest().getParameter("lid");
    	String name = JsfHelper.getRequest().getParameter("name");
    	String businessId = CookieUtils.getBusinessId(JsfHelper.getRequest());
		try {
			name = name==null ? "" : new String(name.getBytes("iso-8859-1"),"utf-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		
    	ClientSession cs = JsfHelper.getBean("clientSession");
    	if(cs!=null&&cs.getUsr()!=null){
    		BbsUser user = cs.getUsr();
        	if (lid != null) {
        		this.lid = lid;
                this.lessonTypes = this.lessonTypeDAO.findAllLessonTypeByTagId2(lid,user,businessId);
            } else if(name != null&&!name.equals("")){
            	this.searchName = name;
            	this.lessonTypes = this.lessonTypeDAO.findLessonTypesByName2(name,user,businessId);
//            	this.info = "共找到"+lessonTypes.size()+"门\""+name+"\"相关课程";
            	this.info = this.lessonTypes.size()+"";
            } else {
                this.lessonTypes = this.lessonTypeDAO.findAllEnableLessonType(businessId);
            }
    	}else{
        	if (lid != null) {
        		this.lid = lid;
                this.lessonTypes = this.lessonTypeDAO.findAllLessonTypeByTagId(lid,businessId);
            } else if(name != null&&!name.equals("")){
            	this.searchName = name;
            	this.lessonTypes = this.lessonTypeDAO.findLessonTypesByName(name,businessId);
//            	this.info = "共找到"+lessonTypes.size()+"门\""+name+"\"相关课程";
            	this.info = this.lessonTypes.size()+"";
            } else {
                this.lessonTypes = this.lessonTypeDAO.findAllEnableLessonType(businessId);
            }
    	}
		

//    	ILessonClassifysDAO lessonClassifysDAO = SpringHelper.getSpringBean("LessonClassifysDAO");
//        this.labelTypes = lessonClassifysDAO.findAllLessonClassifys();
    	ICourseTypeDAO courseTypeDAO = SpringHelper.getSpringBean("CourseTypeDAO");
    	this.courseTypes = courseTypeDAO.findFirstCourseType(businessId);
//    	this.courseTypes = courseTypeDAO.findAllCourseType();
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
