package com.hjedu.course.service.impl;

import java.io.Serializable;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import org.primefaces.json.JSONObject;

import com.hjedu.course.dao.ILessonDAO;
import com.hjedu.course.dao.ILessonLogDAO;
import com.hjedu.course.dao.ILessonTypeDAO;
import com.hjedu.course.dao.ILessonTypeLogDAO;
import com.hjedu.course.entity.Lesson;
import com.hjedu.course.entity.LessonLog;
import com.hjedu.course.entity.LessonType;
import com.hjedu.course.entity.LessonTypeLog;
import com.hjedu.course.entity.StudyPlan;
import com.hjedu.course.service.IBuyCourseService;
import com.hjedu.customer.dao.IBbsUserDAO;
import com.hjedu.customer.entity.BbsUser;
import com.hjedu.platform.controller.ClientSession;
import com.hjedu.platform.dao.IBbsScoreLogDAO;
import com.hjedu.platform.dao.ISystemConfigDAO;
import com.hjedu.platform.entity.BbsScoreLog;
import com.huajie.app.util.StringUtil;
import com.huajie.corss.model.SubUser;
import com.huajie.corss.util.Conn;
import com.huajie.corss.util.ObjectConvent;
import com.huajie.util.JsfHelper;
import com.huajie.util.SpringHelper;

public class BuyCourseService implements IBuyCourseService, Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private IBbsUserDAO userDAO;
	private ILessonTypeDAO lessonTypeDAO;
	private ILessonTypeLogDAO lessonTypeLogDAO;
	private ILessonDAO lessonDAO;
	private ILessonLogDAO lessonLogDAO;
	private IBbsScoreLogDAO bbsScoreLogDAO;
    ISystemConfigDAO systemConfigDAO;
    
    public ISystemConfigDAO getSystemConfigDAO() {
		return systemConfigDAO;
	}

	public void setSystemConfigDAO(ISystemConfigDAO systemConfigDAO) {
		this.systemConfigDAO = systemConfigDAO;
	}
	
	public IBbsUserDAO getUserDAO() {
		return userDAO;
	}

	public void setUserDAO(IBbsUserDAO userDAO) {
		this.userDAO = userDAO;
	}

	public ILessonTypeDAO getLessonTypeDAO() {
		return lessonTypeDAO;
	}

	public void setLessonTypeDAO(ILessonTypeDAO lessonTypeDAO) {
		this.lessonTypeDAO = lessonTypeDAO;
	}

	public ILessonTypeLogDAO getLessonTypeLogDAO() {
		return lessonTypeLogDAO;
	}

	public void setLessonTypeLogDAO(ILessonTypeLogDAO lessonTypeLogDAO) {
		this.lessonTypeLogDAO = lessonTypeLogDAO;
	}

	public ILessonDAO getLessonDAO() {
		return lessonDAO;
	}

	public void setLessonDAO(ILessonDAO lessonDAO) {
		this.lessonDAO = lessonDAO;
	}

	public ILessonLogDAO getLessonLogDAO() {
		return lessonLogDAO;
	}

	public void setLessonLogDAO(ILessonLogDAO lessonLogDAO) {
		this.lessonLogDAO = lessonLogDAO;
	}

	public IBbsScoreLogDAO getBbsScoreLogDAO() {
		return bbsScoreLogDAO;
	}

	public void setBbsScoreLogDAO(IBbsScoreLogDAO bbsScoreLogDAO) {
		this.bbsScoreLogDAO = bbsScoreLogDAO;
	}
	
	public boolean buyCourse(LessonType ltp, BbsUser user) {
		long userscore=0;
		userscore = user.getScore();
		user.setScore(userscore);
		
		if(ltp.getTotalScorePaid()>user.getScore()){
			return false;
		}
		String lessonTypeStr = user.getLessonTypeStr();
		if(lessonTypeStr==null){
			lessonTypeStr = "";
		}
		if(!lessonTypeStr.contains(ltp.getId())){
			//修改用户课程字段
			updateLessonTypesOfUser(ltp.getId(),user);
			
			//修改用户积分
			user.setScore(user.getScore()-ltp.getTotalScorePaid());
			this.userDAO.updateBbsUser(user);
			
			//创建课程记录
			createLessonTypeLog(ltp,user);
			
			//创建课程下资料学习记录
//			createLessonLog(ltp.getId(), user);
			
			//创建积分变化记录
			createScoreChangeLog(-ltp.getTotalScorePaid(), "用户："+user.getName()+"购买了课程："+ltp.getName()+",消耗积分："+ltp.getTotalScorePaid(), user);
			
			//更新缓存中用户数据
			ClientSession ab = JsfHelper.getBean("clientSession");
			if(ab!=null&&ab.getUsr()!=null){
				ab.getUsr().setScore(user.getScore());
				ab.getUsr().setLessonTypeStr(user.getLessonTypeStr());
			}
		}
		return false;
	}
	
	@Override
	public boolean buyCourse2(LessonType ltp, BbsUser user) {
		if(ltp.getTotalScorePaid()>user.getScore()){
			return false;
		}
		String lessonTypeStr = user.getLessonTypeStr();
		if(lessonTypeStr==null){
			lessonTypeStr = "";
		}
		if(!lessonTypeStr.contains(ltp.getId())){
			//修改用户课程字段
			updateLessonTypesOfUser(ltp.getId(),user);
			
			//修改用户积分
			user.setScore(user.getScore()-ltp.getTotalScorePaid());
			this.userDAO.updateBbsUser(user);
			
			//创建课程记录
			createLessonTypeLog(ltp,user);
			
			//创建课程下资料学习记录
//			createLessonLog(ltp.getId(), user);
			
			//创建积分变化记录
			createScoreChangeLog(-ltp.getTotalScorePaid(), "用户："+user.getName()+"购买了课程："+ltp.getName()+",消耗积分："+ltp.getTotalScorePaid(), user);
			
			//更新缓存中用户数据
			ClientSession ab = JsfHelper.getBean("clientSession");
			if(ab!=null&&ab.getUsr()!=null){
				ab.getUsr().setScore(user.getScore());
				ab.getUsr().setLessonTypeStr(user.getLessonTypeStr());
			}
		}
		return true;
	}
	
	/**
	 * 创建所属课程下资料学习记录
	 * @param ltId 课程id
	 * @param user 用户
	 */
	@Override
	public void createLessonLog(String ltId,BbsUser user){
		List<Lesson> lessons=this.lessonDAO.findAllLessonByType(ltId);
        if(lessons!=null&&lessons.size()>0){
        	List<LessonLog> llogs =this.lessonLogDAO.findLessonLogByTypeAndUsr(user.getId(), ltId);
        	StringBuffer strb = new StringBuffer();
        	if(llogs!=null&&llogs.size()>0){
        		for(LessonLog llo : llogs){
        			strb.append(llo.getLesson().getId());
        			strb.append(";");
        		}
        	}
        	for(Lesson ls : lessons){
            	//如果发现还没有购买此课程，或者此课程需要重复购买，就消耗积分
            	 if (!strb.toString().contains(ls.getId())) {
                     LessonLog log = new LessonLog();
                     log.setUser(user);
                     log.setLesson(ls);
                     log.setScorePaid(ls.getScorePaid());
                     log.setBusinessId(user.getBusinessId());
                     this.lessonLogDAO.addLessonLog(log);
                 } 
            }
        }
	}
	
	/**
	 * 创建课程记录（因为有免费课程，可能出现已经有课程记录，需要先查询）
	 * @param ltp 课程
	 * @param user 用户
	 */
	@Override
	public void createLessonTypeLog(LessonType ltp, BbsUser user){
		LessonTypeLog ltl = lessonTypeLogDAO.findLessonTypeLogByTypeAndUsr(ltp.getId(), user.getId());
		if(ltl==null){
			LessonTypeLog lessonTypeLog = new LessonTypeLog(ltp,user);
			lessonTypeLog.setBusinessId(user.getBusinessId());
			this.lessonTypeLogDAO.addLessonTypeLog(lessonTypeLog);
		}
	}
	
	/**
	 * 创建积分变化记录
	 * @param score 积分，正值为加积分，负值为减积分
	 * @param logString 记录日志
	 * @param user 用户
	 */
	@Override
	public void createScoreChangeLog(long score,String logString,BbsUser user){
		BbsScoreLog log = new BbsScoreLog();
        log.setDescription(logString);
        log.setUser(user);
        log.setScore(score);
        log.setScoreBalance(user.getScore());
        log.setGenTime(new Date());
        bbsScoreLogDAO.addBbsScoreLog(log);
	}
	
	/**
	 * 更新用户课程字段
	 * @param LessonTypeId 购买的课程id
	 * @param user 用户
	 */
	@Override
    public void updateLessonTypesOfUser(String LessonTypeId,BbsUser user){
    	String lts = user.getLessonTypeStr();
        if(StringUtil.isEmpty(lts)){
        	lts = LessonTypeId+";";
        }else{
        	if(!lts.contains(LessonTypeId)){
            	lts = lts+LessonTypeId+";";
            }
        }
        user.setLessonTypeStr(lts);
    }

	@Override
	public boolean buyCourse(LessonType ltp, BbsUser user, StudyPlan studyPlan,boolean ifRequired) {
		if(user.getLessonTypeStr()==null || !user.getLessonTypeStr().contains(ltp.getId())){
			//修改用户课程字段
			updateLessonTypesOfUser(ltp.getId(),user);
			this.userDAO.updateBbsUser(user);
			
			//创建积分变化记录
			createScoreChangeLog(0, "用户："+user.getName()+"购买了课程："+ltp.getName()+",消耗积分："+ltp.getTotalScorePaid(), user);
			
			//更新缓存中用户数据
			ClientSession ab = JsfHelper.getBean("clientSession");
			if(ab!=null&&ab.getUsr()!=null){
				ab.getUsr().setLessonTypeStr(user.getLessonTypeStr());
			}
		}
		//创建课程记录
		createLessonTypeLog(ltp,user,studyPlan,ifRequired);
		return true;
	}

	private void createLessonTypeLog(LessonType ltp, BbsUser user, StudyPlan studyPlan,boolean ifRequired) {
		LessonTypeLog ltl = lessonTypeLogDAO.findLessonTypeLogByTypeAndUsr(ltp.getId(), user.getId());
		List<StudyPlan> sps = new LinkedList<>();
		sps.add(studyPlan);
		if(ltl==null){
			LessonTypeLog lessonTypeLog = new LessonTypeLog(ltp,user,sps,ifRequired);
			lessonTypeLog.setBusinessId(user.getBusinessId());
			this.lessonTypeLogDAO.addLessonTypeLog(lessonTypeLog);
		}else{
			ltl.setStudyPlan(sps);
			ltl.setIfRequired(ifRequired);
			this.lessonTypeLogDAO.updateLessonTypeLog(ltl);
		}
	}
}
