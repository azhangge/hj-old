package com.hjedu.course.dao;

import java.util.List;

import com.hjedu.course.entity.StudyPlanLog;

public interface IStudyPlanLogDAO {

	void addStudyPlanLog(StudyPlanLog studyPlanLog);

	void deleteStudyPlanLog(String id);

	void deleteAll();
	
	void updateStudyPlanLog(StudyPlanLog studyPlanLog);
	
	StudyPlanLog findStudyPlanLog(String id);
	
	List<StudyPlanLog> findAllStudyPlanLog();
	
	List<StudyPlanLog> findStudyPlanLogByUserId(String uid);

	List<StudyPlanLog> findStudyPlanLogBySPId(String studyPlanId);

	StudyPlanLog findStudyPlanLogBySPAndUsr(String studyPlanId, String tid);

	int findFinishedStudyPlanLogNum(String studyPlanId, String logId);

	void deleteStudyPlanLogByStudyPlanId(String id);
	
}
