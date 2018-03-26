package com.hjedu.course.controller;


import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

import org.apache.log4j.Logger;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.model.UploadedFile;

import com.hjedu.course.dao.HJCourseDAO;
import com.hjedu.course.entity.HJCourse;
import com.hjedu.customer.entity.Teacher;
import com.hjedu.platform.controller.AdminNewFile;
import com.hjedu.platform.controller.ClientSession;
import com.huajie.app.util.StringUtil;
import com.huajie.util.Cat;
import com.huajie.util.JsfHelper;
import com.huajie.util.SpringHelper;

@ManagedBean
@ViewScoped
public class TeacherCourseList implements Serializable {
	private static final long serialVersionUID = 1L;
	private Logger log4j = Logger.getLogger(TeacherCourseList.class);
	private List<HJCourse> courses;
    
	public Logger getLog4j() {
		return log4j;
	}

	public void setLog4j(Logger log4j) {
		this.log4j = log4j;
	}

	public List<HJCourse> getCourses() {
		return courses;
	}

	public void setCourses(List<HJCourse> courses) {
		this.courses = courses;
	}

	@SuppressWarnings("unchecked")
	@PostConstruct
    public void init() {
		this.courses = new LinkedList<>();
		HJCourseDAO dao = SpringHelper.getSpringBean("HJCourseDAO");
		ClientSession cs = JsfHelper.getBean("clientSession");
		if(cs!=null&&cs.getUsr()!=null){
			Teacher t = cs.getUsr().getTeacher();
			if(null!=t){
				Object[] ob = {t.getId()};
				Map<String, Object> map = dao.getScrollData(HJCourse.class, 0, 100, " o.teacher.id =?1", ob, null);
				courses = (List<HJCourse>) map.get("list");
			}
		}
	}
	
	public String addCourse(){
		return null;
	}
	
	public String updateCourse(){
		return null;
	}
	
	public String delete(String id) {
		HJCourseDAO dao = SpringHelper.getSpringBean("HJCourseDAO");
		dao.delete(id);
        this.init();
        return null;
    }
	
	public String submit(){
		return "/talk/CreateCourse.jspx";
	}
	
	public void up_action(FileUploadEvent event){
		String picUrl = savePic(event);
		if(StringUtil.isNotEmpty(picUrl)){
		}
	}
	
	private String savePic(FileUploadEvent event) {
		String picUrl = "";
		try {
			UploadedFile item = event.getFile();
			String n = item.getFileName();
			int l2 = n.lastIndexOf(".");
			String ext = "." + n.substring(l2 + 1).toLowerCase();
			if (!ext.equals(".jpg") && !ext.equals(".jpeg") && !ext.equals(".png") && !ext.equals(".gif")) {
				JsfHelper.error("不符合的图片类型！");
				return picUrl;
			}
			if(item.getSize()>5*1024*1024){
				JsfHelper.error("图片大小不能超过5M");
				return picUrl;
			}
			String imgId = Cat.getUniqueId();
			AdminNewFile.saveFile(item, ext, imgId);
			picUrl = "servlet/ShowImage?id=" + imgId;
		} catch (Exception ee) {
			ee.printStackTrace();
		}
		return picUrl;
	}
}
