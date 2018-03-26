package com.hjedu.course.dao;

import java.util.List;

import com.hjedu.course.entity.StudyPlanChangeLog;

public interface IStudyPlanChangeLogDAO {

	void addStudyPlanChangeLog(StudyPlanChangeLog partnerModel);

	void deleteStudyPlanChangeLog(String id);

	void deleteAll();
	
	void updateStudyPlanChangeLog(StudyPlanChangeLog comModel);
	
	StudyPlanChangeLog findStudyPlanChangeLog(String id);
	
	List<StudyPlanChangeLog> findAllStudyPlanChangeLog();
	
	void deleteLogByStudyPlanLog(String studyPlanLogId);
	
	void deleteLogByUser(String uid);

	List<StudyPlanChangeLog> findStudyPlanChangeLogByUsr(String uid);
	
	List<StudyPlanChangeLog> findStudyPlanChangeLogBySPLAndUsr(String studyPlanLogId, String uid);
	
	List<StudyPlanChangeLog> findStudyPlanChangeLogByStudyPlanLog(String studyPlanLogId);

	List<StudyPlanChangeLog> findStudyPlanChangeLogByPlanAndUsr(String studyPlanId, String uid);

	List<StudyPlanChangeLog> findStudyPlanChangeLogByExamAndUsr(String examId, String userId);

	List<StudyPlanChangeLog> findStudyPlanChangeLogBySPLAndUsrAndType(String studyPlanLogId, String userId,
			String type);
	
}
