package com.hjedu.course.service;

import com.hjedu.course.entity.LessonType;
import com.hjedu.course.entity.StudyPlan;
import com.hjedu.customer.entity.BbsUser;

public interface IBuyCourseService {
	/**
	 * 行购买课程
	 * @param ltp 课程
	 * @param bu 用户
	 * @return false 积分不够，true 购买成功
	 */
	boolean buyCourse(LessonType ltp,BbsUser user);
	
	void createScoreChangeLog(long score, String logString, BbsUser user);

	void updateLessonTypesOfUser(String LessonTypeId, BbsUser user);

	void createLessonTypeLog(LessonType ltp, BbsUser user);

	void createLessonLog(String ltId, BbsUser user);
	
	/**
	 * 行购买课程
	 * @param ltp 课程
	 * @param bu 用户
	 * @param StudyPlan 归属学习计划
	 * @return false 积分不够，true 购买成功
	 */
	boolean buyCourse(LessonType ltp, BbsUser user, StudyPlan studyPlan, boolean ifRequired);

	boolean buyCourse2(LessonType ltp, BbsUser user);
}
