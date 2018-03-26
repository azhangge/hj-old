package com.hjedu.platform.controller;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;

import com.hjedu.course.dao.ILessonDAO;
import com.hjedu.course.dao.ILessonTypeDAO;
import com.hjedu.course.entity.Lesson;
import com.hjedu.course.entity.LessonType;
import com.hjedu.customer.dao.IBbsFileDAO;
import com.hjedu.customer.entity.BbsUser;
import com.hjedu.platform.entity.BbsFileModel;
import com.huajie.app.util.StringUtil;
import com.huajie.util.JsfHelper;
import com.huajie.util.SpringHelper;

@ManagedBean
@ViewScoped
public class PlayerList implements Serializable {
		List<Lesson> lessons = new ArrayList<Lesson>();
		List<String> lessonids = new ArrayList<String>();
	 	private ILessonDAO lessonDAO = SpringHelper.getSpringBean("LessonDAO");
	 	private ILessonTypeDAO lessonTypeDAO = SpringHelper.getSpringBean("LessonTypeDAO");
	 	IBbsFileDAO bbsFileDAO = SpringHelper.getSpringBean("BbsFileDAO");
	 	BbsUser usr = null;
	 	String para="";
	 	
	 	public List<Lesson> getLessons() {
	        return lessons;
	    }

	    public void setLessons(List<Lesson> lessons) {
	        this.lessons = lessons;
	    }
 
	    public List<String> getLessonids() {
	    	List<String> lessonids2 = new ArrayList<String>();
	    	for(Lesson ls:lessons){
	    		lessonids2.add(ls.getId());
	    	}
			return lessonids2;
		}

		public void setLessonids(List<String> lessonids) {
			this.lessonids = lessonids;
		}

		public BbsUser getUsr() {
	        return usr;
	    }
	    
	    public void setUsr(BbsUser usr) {
	        this.usr = usr;
	    }
	 	
		public String getPara() {
			return para;
		}

		public void setPara(String para) {
			this.para = para;
		}
	 
    @PostConstruct
    public void init() {
    	ClientSession cs = JsfHelper.getBean("clientSession");
        this.usr = cs.getUsr();
        if (this.usr != null) {
//	    	this.para=JsfHelper.getRequest().getParameter("l");
	    	String id = JsfHelper.getRequest().getParameter("id");
            String ltid = JsfHelper.getRequest().getParameter("lessonTypeId");
            
            if(ltid != null){
            	this.lessons=lessonDAO.findAllLessonByType(ltid);
            }else if(id != null){
            	Lesson lesson=lessonDAO.findLesson(id);
            	LessonType lessonType=null;
            	if(lesson!=null){
            		lessonType=lesson.getLessonType();
            		this.lessons=lessonDAO.findAllLessonByType(lessonType.getId());
            	}
            }
            int zindex=1;    		
        	for(Lesson lesson:this.lessons){        		
        		if (lesson.getChapterType() == 1) {
					lesson.setChapterIndex("章节" + zindex++);
				}    
        		BbsFileModel client = this.bbsFileDAO.findAllClientFileByLessionId(lesson.getId());
        		if (client != null && client.getFileFullPath() != null) {
					lesson.setHasFile(true);
				}else {
					lesson.setHasFile(false);
				}
        	}            
        }
    }
    
    public String changeVision(){
    	HttpServletRequest request = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
    	 String tid = request.getParameter("tid");
    	if(tid!=""){
    		return "LessonDetail2.jspx?id="+tid+"&faces-redirect=true";
    	}
		return null;
    }
	
	
}
