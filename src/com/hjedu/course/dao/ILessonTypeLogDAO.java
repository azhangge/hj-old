package com.hjedu.course.dao;

import java.util.Date;
import java.util.List;
import java.util.Map;

import com.hjedu.common.dao.BaseDAO;
import com.hjedu.course.entity.LessonTypeLog;
import com.hjedu.course.vo.LessonTypeLogVO;

public interface ILessonTypeLogDAO extends BaseDAO<LessonTypeLog> {

	void addLessonTypeLog(LessonTypeLog partnerModel);

	void deleteLessonTypeLog(String id);

	void deleteAll();
	
	void updateLessonTypeLog(LessonTypeLog comModel);
	
	LessonTypeLog findLessonTypeLog(String id);
	
	List<LessonTypeLog> findAllLessonTypeLog(String businessId);
	
	void deleteLogByLessonType(String lid);
	
	void deleteLogByUser(String lid);
	
	long getTotalLessonTypeLogBbsScore(String uid, Date btime, Date etime);

	List<LessonTypeLog> findLessonTypeLogByUsr(String uid);
	
	List<LessonTypeLog> findLessonTypeLogByUsrAndMaxResults(String uid, int maxResults);

	LessonTypeLog findLessonTypeLogByTypeAndUsr(String typeId, String userId);

	List<LessonTypeLog> findLessonTypeLogByLessonType(String tid);

	List<LessonTypeLog> findFinishedLessonTypeLogsByUsr(String userId);

	Map<String, Object> findLessonTypeLogsByUsr(String userId, int firstSize, int pageSize);

	void deleteLogByLessonTypeAndUser(String lid, String userId);
	
	public long getNumByCondition(Map<String, Object> fms,String businessId);
	
	public List<LessonTypeLogVO> findLessonTypeLogVOsByCondition(Map<String, Object> filterMap, int firstSize, int pageSize, String field,
			int type, String businessId);

}
