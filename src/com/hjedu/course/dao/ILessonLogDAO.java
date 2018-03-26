package com.hjedu.course.dao;

import java.util.Date;
import java.util.List;
import java.util.Map;

import com.hjedu.common.dao.BaseDAO;
import com.hjedu.course.entity.Lesson;
import com.hjedu.course.entity.LessonLog;
import com.hjedu.course.entity.LessonType;
import com.hjedu.customer.entity.AdminInfo;

public interface ILessonLogDAO{

    public void updateLessonLog(LessonLog comModel);

    public void addLessonLog(LessonLog partnerModel);

    public void deleteAll();

    public void deleteLogByLesson(String lid);

    public void deleteLogByUser(String lid);

    public void deleteLessonLog(String id);

    public List<LessonLog> findAllLessonLog(String businessId);

    public List<LessonLog> findLessonLogByAdmin(AdminInfo admin,String businessId);

    public List<LessonLog> findLessonLogByUsr(String id);

    public List<LessonLog> findLessonLogByTypeAndUsr(final String uid, String tid);

    public List<LessonLog> findLessonLogByLesson(final String uid);

    public List<LessonLog> findLessonLogByLessonAndUsr(final String lid, final String uid);

    public long getFinishedLogNumByTypeAndUser(String tid, String uid);

    public LessonLog findLessonLog(String id);

    public long getTotalLessonLogBbsScore(String uid, Date btime, Date etime);

	public List<LessonLog> findFinishedLessonLogsByUsr(String id);

	void deleteLogByUserAndType(String userId, String typeId);

	void deleteLogByType(String typeId);
	
	long getNumByCondition(Map<String, Object> filterMap);
	
	List<LessonLog> findLessonsByCondition(String whereSql, Map<String, Object> filterMap, int firstSize, int pageSize,
			String field, int type);
}
