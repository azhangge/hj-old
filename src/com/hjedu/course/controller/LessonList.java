package com.hjedu.course.controller;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

import com.hjedu.course.dao.ILessonDAO;
import com.hjedu.course.dao.ILessonLogDAO;
import com.hjedu.course.dao.ILessonTypeDAO;
import com.hjedu.course.dao.ILessonTypeLogDAO;
import com.hjedu.course.entity.Lesson;
import com.hjedu.course.entity.LessonLog;
import com.hjedu.course.entity.LessonType;
import com.hjedu.course.entity.LessonTypeLog;
import com.hjedu.customer.dao.IBbsFileDAO;
import com.hjedu.customer.dao.IBbsUserDAO;
import com.hjedu.customer.entity.BbsUser;
import com.hjedu.customer.entity.Teacher;
import com.hjedu.platform.controller.ClientSession;
import com.hjedu.platform.entity.BbsFileModel;
import com.huajie.app.util.FileUtil;
import com.huajie.app.util.StringUtil;
import com.huajie.util.CookieUtils;
import com.huajie.util.JsfHelper;
import com.huajie.util.SpringHelper;

@ManagedBean
@ViewScoped
public class LessonList implements Serializable {
	private static final long serialVersionUID = 1L;
	List<Lesson> lessons = new ArrayList<>();
    LessonType lessonType;
    ILessonDAO lessonDAO = SpringHelper.getSpringBean("LessonDAO");
    ILessonTypeLogDAO lessonTypeLogDAO = SpringHelper.getSpringBean("LessonTypeLogDAO");
    IBbsFileDAO bbsFileDAO = SpringHelper.getSpringBean("BbsFileDAO");
    List<BbsUser> users;
    
    List<Lesson> accessibleLessons;
	private LessonTypeLog lessonTypeLog;
	private String message;
	private List<LessonType> courses;

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public List<BbsUser> getUsers() {
		return users;
	}

	public void setUsers(List<BbsUser> users) {
		this.users = users;
	}

	public LessonTypeLog getLessonTypeLog() {
		return lessonTypeLog;
	}

	public void setLessonTypeLog(LessonTypeLog lessonTypeLog) {
		this.lessonTypeLog = lessonTypeLog;
	}

	public List<Lesson> getLessons() {
        return lessons;
    }

    public void setLessons(List<Lesson> lessons) {
        this.lessons = lessons;
    }

    public List<Lesson> getAccessibleLessons() {
        this.filterAccessibleExams();
        return accessibleLessons;
    }

    public void setAccessibleLessons(List<Lesson> accessibleLessons) {
        this.accessibleLessons = accessibleLessons;
    }

    public LessonType getLessonType() {
        return lessonType;
    }

    public void setLessonType(LessonType lessonType) {
        this.lessonType = lessonType;
    }
    
    public List<LessonType> getCourses() {
		return courses;
	}

	public void setCourses(List<LessonType> courses) {
		this.courses = courses;
	}

	@PostConstruct
    public void init() {
        String tid = JsfHelper.getRequest().getParameter("tid");
        if (tid != null) {
        	ILessonTypeDAO lessonTypeDAO = SpringHelper.getSpringBean("LessonTypeDAO");
        	String businessId = CookieUtils.getBusinessId(JsfHelper.getRequest());
        	this.lessonType = lessonTypeDAO.findLessonType(tid);
        	if(lessonType!=null){
//        		Teacher t = lessonType.getTeacher();
//        		if(t!=null){
//        			this.courses = lessonTypeDAO.findLessonTypesByTeacherId(t.getId());
//        		}
        		String str = lessonType.getCourseTypeStr();
        		if(StringUtil.isNotEmpty(str)){
        			String[] ids = str.split(";");
        			if(ids!=null){
        				String id = ids[0];
        				if(StringUtil.isNotEmpty(id)){
        					this.courses = lessonTypeDAO.findLessonTypesByTagName(id, 0, 3, businessId);
        				}
        			}
        		}
        	}
        	IBbsUserDAO userDAO = SpringHelper.getSpringBean("BbsUserDAO");
        	this.users = userDAO.findBbsUserByLessonTypeId(tid);
        	
        	this.lessons = this.lessonDAO.findAllShowedLessonByType(tid);
        	int zindex=1;
    		int jindex=1;
        	for(Lesson lesson:this.lessons){        		
        		if (lesson.getChapterType() == 1) {
					lesson.setChapterIndex("章节" + zindex++);
				}else if (lesson.getChapterType() == 2) {
					lesson.setChapterIndex("课时" + jindex++);
				}
        		BbsFileModel client = this.bbsFileDAO.findAllClientFileByLessionId(lesson.getId());
        		if (client != null && client.getFileFullPath() != null) {
					lesson.setHasFile(true);
				}else {
					lesson.setHasFile(false);
				}
        	}
        	
	        ClientSession cs = JsfHelper.getBean("clientSession");
	        if(cs!=null&&cs.getUsr()!=null){
	        	BbsUser user = cs.getUsr();
	        	this.lessonTypeLog = lessonTypeLogDAO.findLessonTypeLogByTypeAndUsr(tid,user.getId());
	        	ILessonLogDAO lessonLogDAO = SpringHelper.getSpringBean("LessonLogDAO");
	        	List<LessonLog> logs = lessonLogDAO.findLessonLogByTypeAndUsr(user.getId(), tid);
	        	Map<String,LessonLog> map = new HashMap<>();
	        	for(LessonLog log : logs){
	        		map.put(log.getLesson().getId(), log);
	        	}
	        	for(Lesson l : lessons){
	        		l.setLog(map.get(l.getId()));
	        	}
	        	if(logs!=null&&logs.size()>0){
	        		long begin = logs.get(0).getGenTime().getTime();
	        		long end = new Date().getTime();
	        		String time = null;
	        		//小时
	        		long l = (end-begin)/(1000*60*60);
	        		if(l>=(24*30)){
	        			time = l/(24*30)+"个月前";
	        		}else if(l>=24&&l<24*30){
	        			time = l/24+"天前";
	        		}else if(l>=1&&l<24){
	        			time = l+"小时前";
	        		}else if(l<1){
	        			time = "刚刚";
	        		}
	        		this.message = time+"学习至："+logs.get(0).getLesson().getName();
	        	}
	        }
        }
    }
    
    
    
    /**
     * 
     * @return 
     */
    public List<Lesson> filterAccessibleExams() {
        ClientSession cs = JsfHelper.getBean("clientSession");
        BbsUser bu = cs.getUsr();
        List<Lesson> exs = new ArrayList<>();
        if (bu != null) {
            if (this.lessons != null) {
                for (Lesson ex : this.lessons) {
                    if (bu.testIfIn(ex.getGroupStr())) {
                        exs.add(ex);
                    }
                }
            }
        }
        //this.checkRetry(exs);
        accessibleLessons = exs;
        return accessibleLessons;
    }
    
    public void collectCourse(){
    	ClientSession cs = JsfHelper.getBean("clientSession");
    	if(cs!=null){
    		BbsUser bu = cs.getUsr();
    		if(bu!=null){
    			String str = bu.getCollectionCourses();
    			String id = this.lessonType.getId();
    			if(StringUtil.isEmpty(str)){
    				str = id + ";";
    			}else{
    				if(str.contains(id)){
    					str = FileUtil.removePartOfStr(str, id);
    				}else{
    					str += id + ";";
    				}
				}
    			bu.setCollectionCourses(str);
    			IBbsUserDAO userDAO = SpringHelper.getSpringBean("BbsUserDAO");
    			userDAO.updateBbsUser(bu);
    		}
    	}
    }
    
}
