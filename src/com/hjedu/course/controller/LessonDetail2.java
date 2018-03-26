package com.hjedu.course.controller;

import java.io.Serializable;
import java.util.List;
import java.util.UUID;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;

import com.google.gson.annotations.Expose;
import com.hjedu.course.dao.ILessonDAO;
import com.hjedu.course.dao.ILessonLogDAO;
import com.hjedu.course.dao.ILessonTypeDAO;
import com.hjedu.course.dao.ILessonTypeLogDAO;
import com.hjedu.course.entity.Lesson;
import com.hjedu.course.entity.LessonLog;
import com.hjedu.course.entity.LessonType;
import com.hjedu.course.entity.LessonTypeLog;
import com.hjedu.customer.dao.IBbsUserDAO;
import com.hjedu.customer.entity.BbsUser;
import com.hjedu.platform.controller.ClientSession;
import com.hjedu.platform.entity.BbsFileModel;
import com.huajie.app.util.StringUtil;
import com.huajie.util.CookieUtils;
import com.huajie.util.JsfHelper;
import com.huajie.util.SpringHelper;

@ManagedBean
@ViewScoped
public class LessonDetail2 implements Serializable {
    
    BbsUser usr = null;
    Lesson lesson;
    @Expose
    LessonLog lessonLog;
    
    private IBbsUserDAO userDAO = SpringHelper.getSpringBean("BbsUserDAO");
    private ILessonDAO lessonDAO = SpringHelper.getSpringBean("LessonDAO");
    private ILessonLogDAO lessonLogDAO = SpringHelper.getSpringBean("LessonLogDAO");
    ILessonTypeLogDAO lessonTypeLogDAO = SpringHelper.getSpringBean("LessonTypeLogDAO");
    private List<LessonType> courses;
    private String businessId;
    
    public List<LessonType> getCourses() {
		return courses;
	}

	public void setCourses(List<LessonType> courses) {
		this.courses = courses;
	}
    private String htmlUrl;
    
    public String getHtmlUrl() {
		return htmlUrl;
	}

	public void setHtmlUrl(String htmlUrl) {
		this.htmlUrl = htmlUrl;
	}

	public BbsUser getUsr() {
        return usr;
    }
    
    public void setUsr(BbsUser usr) {
        this.usr = usr;
    }
    
    public LessonLog getLessonLog() {
        return lessonLog;
    }
    
    public void setLessonLog(LessonLog lessonLog) {
        this.lessonLog = lessonLog;
    }
    
    public Lesson getLesson() {
        return lesson;
    }
    
    public void setLesson(Lesson lesson) {
        this.lesson = lesson;
    }
    
    @PostConstruct
    public void init() {
        HttpServletRequest request = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
        ClientSession cs = JsfHelper.getBean("clientSession");
        this.businessId = CookieUtils.getBusinessId(JsfHelper.getRequest());
        this.usr = cs.getUsr();
        if (this.usr != null) {
        	
        	
        	
            String temp = request.getParameter("id");
            String ltid = request.getParameter("lessonTypeId");
            if(ltid != null){//传入的是课程id，判断是开始学习还是继续学习
            	List<LessonLog> logs = lessonLogDAO.findLessonLogByTypeAndUsr(usr.getId(), ltid);
            	if(logs!=null&&logs.size()>0){//有学习记录，是继续学习
            		this.lessonLog = logs.get(0);
            		this.lesson = lessonLog.getLesson();
            	}else{//无学习记录，是开始学习
        			List<Lesson> les = lessonDAO.findAllLessonByType(ltid);
        			if(les!=null&&les.size()>0){
        				this.lesson = les.get(0);
        				LessonLog log = new LessonLog();
                        log.setUser(usr);
                        log.setLesson(lesson);
                        log.setScorePaid(lesson.getScorePaid());
                        log.setBusinessId(this.businessId);
                        this.lessonLogDAO.addLessonLog(log);
                        this.lessonLog = log;
        			}
        		}
            }else if (temp != null) {
//                this.initx(temp, usr, request);
            	this.lesson = this.lessonDAO.findLesson(temp);
            	List<LessonLog> logs = this.lessonLogDAO.findLessonLogByLessonAndUsr(lesson.getId(), usr.getId());
            	if (logs.isEmpty()) {
                    LessonLog log = new LessonLog();
                    log.setUser(usr);
                    log.setLesson(lesson);
                    log.setScorePaid(lesson.getScorePaid());
                    log.setBusinessId(this.businessId);
                    this.lessonLogDAO.addLessonLog(log);
                    this.lessonLog = log;
                } else {
                    this.lessonLog = logs.get(0);
                }
            }
            String typeId = null;
            if(ltid!=null){
            	typeId = ltid;
            }else if(this.lesson!=null&&this.lesson.getLessonType()!=null){
            	typeId = this.lesson.getLessonType().getId();
            }
            if(this.lesson!=null&&this.lesson.getLessonType()!=null){
            	String str = this.lesson.getLessonType().getCourseTypeStr();
        		if(StringUtil.isNotEmpty(str)){
        			String[] ids = str.split(";");
        			if(ids!=null){
        				String id = ids[0];
        				if(StringUtil.isNotEmpty(id)){
        					ILessonTypeDAO lessonTypeDAO = SpringHelper.getSpringBean("LessonTypeDAO");
        					this.courses = lessonTypeDAO.findLessonTypesByTagName(id, 0, 3, businessId);
        				}
        			}
        		}
            }
            if(typeId!=null){
            	ILessonTypeLogDAO lessonTypeLogDAO = SpringHelper.getSpringBean("LessonTypeLogDAO");
            	LessonTypeLog ltl = lessonTypeLogDAO.findLessonTypeLogByTypeAndUsr(typeId,usr.getId());
            	if(ltl!=null){
            		ltl.setLearnNum((ltl.getLearnNum()==null?0:ltl.getLearnNum())+1);
            		lessonTypeLogDAO.updateLessonTypeLog(ltl);
            	} else {
            		ltl = new LessonTypeLog(lesson.getLessonType(), usr);
            		ltl.setFinishedClassNum(0d);
            		lessonTypeLogDAO.addLessonTypeLog(ltl);
            	}
            }
        }
        
        if(this.lesson!=null){
        	List<BbsFileModel> bfmlist=this.lesson.getFiles();
            if(StringUtil.isEmpty(this.lesson.getSourceUrl())&&this.lesson.getFiles()!=null&&this.lesson.getFiles().size()>0){
            	BbsFileModel bfm = bfmlist.get(0);
            	String ext=bfm.getFileExt();
            	if(ext.equals(".doc") || ext.equals(".docx") || ext.equals(".ppt") || ext.equals(".pptx") || ext.equals(".pdf")){
            		this.setHtmlUrl(bfm.getId()+".html");
            	}
            }
        }
        
    }
    
    
    public void initx(String lid, BbsUser bu, HttpServletRequest request) {
        this.usr = bu;
        if (this.usr != null) {
            if (lid != null) {
                this.lesson = this.lessonDAO.findLesson(lid);
                lesson.setReadCount(lesson.getReadCount() + 1);
                this.lessonDAO.updateLesson(lesson);
            }
            //如果发现还没有购买此课程，或者此课程需要重复购买，就消耗积分
            List<LessonLog> logs = this.lessonLogDAO.findLessonLogByLessonAndUsr(lesson.getId(), usr.getId());
            if (logs.isEmpty()) {
                //this.usr.setScore(this.usr.getScore() - lesson.getScorePaid());
//                IBbsScoreLogService bsl = SpringHelper.getSpringBean("BbsScoreLogService");
//                bsl.log("课程学习消耗积分", (int) (-1 * lesson.getScorePaid()));
                //this.userDAO.updateBbsUser(this.usr);
                //加入购买记录
                LessonLog log = new LessonLog();
                log.setUser(usr);
                log.setLesson(lesson);
                log.setScorePaid(lesson.getScorePaid());
                log.setBusinessId(this.businessId);
                this.lessonLogDAO.addLessonLog(log);
                this.lessonLog = log;
            } else {
                this.lessonLog = logs.get(0);
            }
        }
        
    }
    
}
