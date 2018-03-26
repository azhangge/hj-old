package com.hjedu.course.dao;

import java.util.List;
import java.util.Map;

import com.hjedu.course.entity.LessonType;
import com.hjedu.course.entity.StudyPlan;
import com.hjedu.examination.entity.Examination;

public interface IStudyPlanDAO {
	public abstract void addStudyPlan(StudyPlan StudyPlan);

    public abstract void updateStudyPlan(StudyPlan StudyPlan);

    public abstract void deleteStudyPlan(String id);
    
    public abstract void deleteStudyPlanByUrn(String urn);

    public abstract StudyPlan findStudyPlan(String id);
    
    public abstract StudyPlan findStudyPlanByUrn(String paramString);
    
    public abstract List<StudyPlan> findStudyPlansLikeUrn(String urn);
    
    public List<StudyPlan> findAllStudyPlanOrderByDept() ;

    public abstract List<StudyPlan> findAllStudyPlan();

	List<LessonType> findCoursesByStudyPlan(StudyPlan StudyPlan);

	List<Examination> findExamsByStudyPlan(StudyPlan StudyPlan);

	List<StudyPlan> findStudyPlanByUserId(String uid);

	List<StudyPlan> findStudyPlansByOrder(String str, int firstSize, int pageSize, boolean ifDesc, String business);

	List<StudyPlan> findStudyPlanByUserId(String uid, int firstSize, int pageSize);
	
	/**
	 * 
	 * @param str 排序字段名称
	 * @param firstSize 起始行数
	 * @param pageSize 每页行数
	 * @param ifDesc 是否倒序排列
	 * @param idlist 课程id集合
	 * @param ifIn 是否在idlist集合中
	 * @return
	 */
	List<StudyPlan> findStudyPlansByOrder(String str, int firstSize, int pageSize, boolean ifDesc, List<String> idlist,
			boolean ifIn);

	long getStudyPlansNumByOrder(List<String> idlist, boolean ifIn, String business);

	List<StudyPlan> findStudyPlansByFilter(Map<String, Object> fms, String business);

	List<StudyPlan> findOrderedStudyPlans(int offSet, int num, String field, String type, String business);

}