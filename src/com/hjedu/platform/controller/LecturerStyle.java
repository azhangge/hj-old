package com.hjedu.platform.controller;

import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

import com.hjedu.course.dao.ILessonDAO;
import com.hjedu.course.dao.ILessonTypeDAO;
import com.hjedu.course.entity.Lesson;
import com.hjedu.course.entity.LessonType;
import com.hjedu.customer.dao.ITeacherDAO;
import com.hjedu.customer.entity.BbsUser;
import com.hjedu.customer.entity.Teacher;
import com.huajie.app.util.StringUtil;
import com.huajie.util.CookieUtils;
import com.huajie.util.JsfHelper;
import com.huajie.util.SpringHelper;

@ManagedBean
@ViewScoped
public class LecturerStyle implements Serializable {
	private static final long serialVersionUID = 1L;
	ITeacherDAO teacherDAO = SpringHelper.getSpringBean("TeacherDAO");
	ILessonTypeDAO lessonTypeDAO = SpringHelper.getSpringBean("LessonTypeDAO");
    private Teacher teacher;
    private List<LessonType> courses;
    
    String bussinessId = CookieUtils.getBusinessId(JsfHelper.getRequest());
    
    public Teacher getTeacher() {
		return teacher;
	}


	public void setTeacher(Teacher teacher) {
		this.teacher = teacher;
	}


	public List<LessonType> getCourses() {
		return courses;
	}


	public void setCourses(List<LessonType> courses) {
		this.courses = courses;
	}


	@PostConstruct
    public void init() {
        String name = JsfHelper.getRequest().getParameter("name");
        String id = JsfHelper.getRequest().getParameter("id");
        String businessId = CookieUtils.getBusinessId(JsfHelper.getRequest());
        try {
			name = name==null ? "" : new String(name.getBytes("iso-8859-1"),"utf-8");
			id = id==null ? "" : new String(id.getBytes("iso-8859-1"),"utf-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
        if (!StringUtil.isEmpty(name)) {
        	this.teacher = this.teacherDAO.findTeacherByUrn(name,bussinessId);
        	if(teacher!=null){
        		this.courses = this.lessonTypeDAO.findLessonTypesByName(teacher.getName(),businessId);
        	}
        }else if(!StringUtil.isEmpty(id)){
        	this.teacher = this.teacherDAO.findTeacher(id);
        	if(teacher!=null){
        		this.courses = this.lessonTypeDAO.findLessonTypesByTeacherId(teacher.getId());
        	}
        }
    }
    
}
