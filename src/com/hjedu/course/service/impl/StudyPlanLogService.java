package com.hjedu.course.service.impl;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.hjedu.course.dao.ILessonLogDAO;
import com.hjedu.course.dao.ILessonTypeLogDAO;
import com.hjedu.course.dao.IStudyPlanChangeLogDAO;
import com.hjedu.course.dao.IStudyPlanDAO;
import com.hjedu.course.dao.IStudyPlanLogDAO;
import com.hjedu.course.entity.CourseOfPlan;
import com.hjedu.course.entity.Lesson;
import com.hjedu.course.entity.LessonLog;
import com.hjedu.course.entity.LessonType;
import com.hjedu.course.entity.LessonTypeLog;
import com.hjedu.course.entity.StudyPlan;
import com.hjedu.course.entity.StudyPlanChangeLog;
import com.hjedu.course.entity.StudyPlanLog;
import com.hjedu.course.service.IStudyPlanLogService;
import com.hjedu.customer.entity.BbsUser;
import com.hjedu.examination.dao.IExamCaseDAO;
import com.hjedu.examination.entity.ExamCase;
import com.hjedu.examination.entity.Examination;
import com.huajie.app.util.StringUtil;

public class StudyPlanLogService implements IStudyPlanLogService{
	private ILessonTypeLogDAO lessonTypeLogDAO;
	private ILessonLogDAO lessonLogDAO;
	private IStudyPlanChangeLogDAO studyPlanChangeLogDAO;
	private IStudyPlanLogDAO studyPlanLogDAO;
	private IStudyPlanDAO studyPlanDAO;
	private IExamCaseDAO examCaseDAO;

	public ILessonTypeLogDAO getLessonTypeLogDAO() {
		return lessonTypeLogDAO;
	}

	public void setLessonTypeLogDAO(ILessonTypeLogDAO lessonTypeLogDAO) {
		this.lessonTypeLogDAO = lessonTypeLogDAO;
	}

	public IStudyPlanChangeLogDAO getStudyPlanChangeLogDAO() {
		return studyPlanChangeLogDAO;
	}

	public void setStudyPlanChangeLogDAO(IStudyPlanChangeLogDAO studyPlanChangeLogDAO) {
		this.studyPlanChangeLogDAO = studyPlanChangeLogDAO;
	}

	public IStudyPlanLogDAO getStudyPlanLogDAO() {
		return studyPlanLogDAO;
	}

	public void setStudyPlanLogDAO(IStudyPlanLogDAO studyPlanLogDAO) {
		this.studyPlanLogDAO = studyPlanLogDAO;
	}

	public ILessonLogDAO getLessonLogDAO() {
		return lessonLogDAO;
	}

	public void setLessonLogDAO(ILessonLogDAO lessonLogDAO) {
		this.lessonLogDAO = lessonLogDAO;
	}

	public IStudyPlanDAO getStudyPlanDAO() {
		return studyPlanDAO;
	}

	public void setStudyPlanDAO(IStudyPlanDAO studyPlanDAO) {
		this.studyPlanDAO = studyPlanDAO;
	}

	public IExamCaseDAO getExamCaseDAO() {
		return examCaseDAO;
	}

	public void setExamCaseDAO(IExamCaseDAO examCaseDAO) {
		this.examCaseDAO = examCaseDAO;
	}
	
	@Override
	public void createFinishLessonLog(LessonLog lessonLog) {
		if(lessonLog!=null){
			BbsUser user = lessonLog.getUser();
			Lesson lesson = lessonLog.getLesson();
			if(user!=null&&lesson!=null){
				LessonType lt = lesson.getLessonType();
				if(lt!=null){
					String userId = user.getId();
					String typeId = lt.getId();
					if(StringUtil.isNotEmpty(userId)&&StringUtil.isNotEmpty(typeId)){
						LessonTypeLog log = lessonTypeLogDAO.findLessonTypeLogByTypeAndUsr(typeId,userId);
						if(log!=null){
							log.setFinishedClassNum(log.getFinishedClassNum()+lesson.getClassNum());
//							boolean ifFinish = log.getFinishedClassNum()>=lt.getOpenExamNum();
							boolean ifFinish = log.getFinishedClassNum()>=lt.getTotalClassNum();
							boolean ifaddLTLog = false;//课程是否首次完成
							//如果课程之前没完成，现在完成了，则改变课程记录完成状态
							if(!log.isFinished()&&ifFinish){
								log.setFinished(true);
								ifaddLTLog = true;
							}
							lessonTypeLogDAO.updateLessonTypeLog(log);
							//判断是否是学习计划内课程的学习资料
							List<StudyPlan> sps = studyPlanDAO.findStudyPlanByUserId(userId);
							for(StudyPlan sp : sps){
								if(sp!=null){
									String courseIds = sp.getCourseStr();
									String spId = sp.getId();
									if(StringUtil.isNotEmpty(courseIds)&&StringUtil.isNotEmpty(spId)){
										if(courseIds.contains(typeId)){
											//增加学习资料完成动态日志
											StudyPlanLog splog = studyPlanLogDAO.findStudyPlanLogBySPAndUsr(spId, userId);
											if(splog!=null){
												createFinishLessonLog(lessonLog, splog);
												//更新学习计划记录完成学时
												if(log.isIfRequired()){
													splog.setFinishedRClassNum(splog.getFinishedRClassNum()+lesson.getClassNum());
												}else{
													splog.setFinishedClassNum(splog.getFinishedClassNum()+lesson.getClassNum());
												}
												//如果之前没有完成此课程，新增课程完成记录
												if(ifaddLTLog){
													createFinishLessonTypeLog(log, splog);
													updateStudyPlanLogFinishedRequiredNum(log,splog);
												}
												updateCompletedProgress(splog);
												System.out.println(user.getName()+"的学习计划："+sp.getName()+"完成度"+splog.getCompletedProgress());
												studyPlanLogDAO.updateStudyPlanLog(splog);
											}
										}
									}
								}
							}
						}
					}
				}
			}
		}
	}
	
	@Override
	public void createFinishExamLog(ExamCase examCase) {
		//判断是否是学习计划内的考试
		BbsUser user = examCase.getUser();
		Examination exam = examCase.getExamination();
		if(user!=null&&exam!=null){
			List<StudyPlan> sps = studyPlanDAO.findStudyPlanByUserId(user.getId());
			for(StudyPlan sp : sps){
				if(sp!=null){
					String examIds = sp.getExamsStr();
					if(StringUtil.isNotEmpty(examIds)){
						if(examIds.contains(exam.getId())){
							StudyPlanLog splog = studyPlanLogDAO.findStudyPlanLogBySPAndUsr(sp.getId(), user.getId());
							//增加完成考试动态日志
							createFinishExamLog(examCase, splog);
							updateStudyPlanLogFinishedExamNum(splog, examCase);
						}
					}
				}
			}
		}
	}
	
	@Override
	public void createStudyPlanLog(StudyPlan studyPlan, BbsUser user) {
		if(studyPlan!=null&&user!=null){
			String planId = studyPlan.getId();
			String userId = user.getId();
			if(StringUtil.isNotEmpty(userId)&&StringUtil.isNotEmpty(planId)){
				StudyPlanLog splog = studyPlanLogDAO.findStudyPlanLogBySPAndUsr(planId, userId);
				if(splog==null){
					//创建学习计划记录
					splog = new StudyPlanLog(user,studyPlan);
					studyPlanLogDAO.addStudyPlanLog(splog);
					//创建学习计划动态记录
					StudyPlanChangeLog clog = new StudyPlanChangeLog(splog,"0");
					studyPlanChangeLogDAO.addStudyPlanChangeLog(clog);
				}else{
					//如果是修改，则重置学习计划记录状态
					splog.setIfFinished(false);
					splog.setFinishedClassNum(0d);
					splog.setFinishedRClassNum(0d);
					splog.setFinishedExamNum(0);
					splog.setFinishedRequiredNum(0);
					splog.setCompletedProgress("0");
					studyPlanChangeLogDAO.deleteLogByStudyPlanLog(splog.getId());
				}
				String courseStr = studyPlan.getCourseStr();
				//将在学习计划之前完成的数据加入
				if(StringUtil.isNotEmpty(courseStr)){
					//1.加入已经完成的学习资料记录
					List<LessonLog> lessonLogs = lessonLogDAO.findFinishedLessonLogsByUsr(userId);
					if(lessonLogs!=null&&lessonLogs.size()>0){
						Map<String,Boolean> map = new HashMap<>();
						List<CourseOfPlan> cs = studyPlan.getCourses();
						if(cs!=null&&cs.size()>0){
							for(CourseOfPlan c : cs){
								if(c!=null){
									LessonType lt = c.getLessonType();
									if(lt!=null){
										map.put(lt.getId(), c.isIfRequired());
									}
								}
							}
							for(LessonLog lessonLog : lessonLogs){
								if(lessonLog!=null){
									Lesson lesson = lessonLog.getLesson();
									if(lesson!=null){
										LessonType lt = lesson.getLessonType();
										if(lt!=null){
											String typeId = lt.getId();
											if(StringUtil.isNotEmpty(typeId)&&courseStr.contains(typeId)){
												createFinishLessonLog(lessonLog, splog);
												if(map!=null){
													boolean bo = map.get(lt.getId());
													//更新学习计划记录完成学时
													if(bo){
														splog.setFinishedRClassNum(splog.getFinishedRClassNum()+lesson.getClassNum());
													}else{
														splog.setFinishedClassNum(splog.getFinishedClassNum()+lesson.getClassNum());
													}
												}
											}
										}
									}
								}
							}
							//2.加入已经完成的课程记录
							List<LessonTypeLog> lessonTypeLogs = lessonTypeLogDAO.findFinishedLessonTypeLogsByUsr(user.getId());
							for(LessonTypeLog lessonTypeLog : lessonTypeLogs){
								LessonType lt = lessonTypeLog.getLessonType();
								if(lt!=null){
									if(courseStr.contains(lessonTypeLog.getLessonType().getId())){
										createFinishLessonTypeLog(lessonTypeLog,splog);
										updateStudyPlanLogFinishedRequiredNum(lessonTypeLog,splog);
									}
								}
							}
						}
					}
				}
				
				//3.加入已经完成的考试记录
				String examsStr = splog.getStudyPlan().getExamsStr();
				if(StringUtil.isNotEmpty(examsStr)){
					List<ExamCase> examCases = examCaseDAO.findFinishedExamCasesByUser(user.getId());
					for(ExamCase examCase : examCases){
						if(examsStr.contains(examCase.getExamination().getId())){
							createFinishExamLog(examCase, splog);
							updateStudyPlanLogFinishedExamNum(splog, examCase);
						}
					}
				}
				updateCompletedProgress(splog);
				studyPlanLogDAO.updateStudyPlanLog(splog);
			}
		}
	}
	
	/**
	 * 新增学习资料完成动态记录
	 * @param lessonLog
	 * @param splog
	 */
	private void createFinishLessonLog(LessonLog lessonLog,StudyPlanLog splog){
		StudyPlanChangeLog lessonlog = new StudyPlanChangeLog(splog,"1");
		lessonlog.setLesson(lessonLog.getLesson());
		lessonlog.setClassNum(lessonLog.getLesson().getClassNum());
		studyPlanChangeLogDAO.addStudyPlanChangeLog(lessonlog);
	}

	/**
	 * 新增课程完成动态记录
	 * @param log
	 * @param splog
	 */
	private void createFinishLessonTypeLog(LessonTypeLog log,StudyPlanLog splog) {
		StudyPlanChangeLog ltlog = new StudyPlanChangeLog(splog,"2");
		ltlog.setLessonType(log.getLessonType());
		ltlog.setCompletionPercent(log.getFinishedClassNum()+"/"+log.getLessonType().getOpenExamNum());
		studyPlanChangeLogDAO.addStudyPlanChangeLog(ltlog);
	}
	
	/**
	 * 新增考试完成动态记录
	 * @param examCase
	 * @param splog
	 */
	private void createFinishExamLog(ExamCase examCase,StudyPlanLog splog){
		StudyPlanChangeLog examlog = new StudyPlanChangeLog(splog,"3");
		examlog.setExam(examCase.getExamination());
		examlog.setExamScore(examCase.getScore());
		examlog.setIfPassed(examCase.isIfPassed());
		studyPlanChangeLogDAO.addStudyPlanChangeLog(examlog);
	}
	
	/**
	 * 新增学习计划完成动态记录
	 * @param splog
	 */
	private void createFinishPlanLog(StudyPlanLog splog){
		//创建学习计划完成动态日志
		StudyPlanChangeLog llog = new StudyPlanChangeLog(splog,"4");
		studyPlanChangeLogDAO.addStudyPlanChangeLog(llog);
		updateStudyPlanLogState(splog);
	}
	
	/**
	 * 根据课程记录更新学习计划记录
	 * @param log
	 * @param splog
	 */
	private void updateStudyPlanLogFinishedRequiredNum(LessonTypeLog log,StudyPlanLog splog){
		//判断该课程是否必修
		if(log.isIfRequired()){//必修，则完成必修课程数目+1
			splog.setFinishedRequiredNum(splog.getFinishedRequiredNum()+1);
			updateCompletedProgress(splog);
		}
	}
	
	/**
	 * 设置学习计划是否完成，完成名次，完成时间
	 * @param splog
	 */
	private void updateStudyPlanLogState(StudyPlanLog splog){
		int i = studyPlanLogDAO.findFinishedStudyPlanLogNum(splog.getStudyPlan().getId(),splog.getId());
		splog.setRanking(i+1);
		splog.setIfFinished(true);
		splog.setFinishedTime(new Date());
	}
	
	private void updateStudyPlanLogFinishedExamNum(StudyPlanLog splog,ExamCase examCase){
		if(splog!=null){
			//判断此考试以前是否通过了，如果没有，则学习计划的完成考试数目+1
			BbsUser user = splog.getUser();
			if(user!=null){
				List<StudyPlanChangeLog> results = studyPlanChangeLogDAO.findStudyPlanChangeLogBySPLAndUsrAndType(splog.getId(), user.getId(),"3");
				int i = 0;
				StringBuffer sbf = new StringBuffer();
				for(StudyPlanChangeLog result : results){
					if(result!=null){
						Examination exam = result.getExam();
						if(exam!=null){
							if(!sbf.toString().contains(exam.getId())){
								sbf.append(exam.getId());
								sbf.append(";");
								if(result.isIfPassed()){
									i=i+1;
								}
							}
						}
					}
				}
				splog.setFinishedExamNum(i);
				updateCompletedProgress(splog);
				studyPlanLogDAO.updateStudyPlanLog(splog);
			}
		}
	}
	
	private void updateCompletedProgress(StudyPlanLog splog){
		String cp = splog.getCompletedProgress();
		if(StringUtil.isEmpty(cp)){
			cp ="0";
		}
		boolean bol = !cp.equals("1");
		double rate = splog.checkStudyPlanIfFinished();
		splog.setCompletedProgress(StringUtil.returnRate(rate, 1, 2));
		if(bol&&rate==1){//如果全部完成，则创建完成学习计划动态记录
			createFinishPlanLog(splog);
		}
	}
}
