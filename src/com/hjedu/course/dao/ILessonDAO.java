package com.hjedu.course.dao;

import java.util.List;
import java.util.Map;

import com.hjedu.course.entity.Lesson;
import com.hjedu.course.vo.LessonVO;
import com.hjedu.customer.entity.AdminInfo;

public interface ILessonDAO {
	
	public abstract List<Lesson> findChapterLesson(String businessId, int chapterType);//章节分类查询
	
	public List<Lesson> findChapterLessonss(String businessId, String typeId, int chapterType);

	public abstract List<Lesson> findChilds(String businessId, String lessonId);
	
    public abstract void addLesson(Lesson m);

    public abstract void updateLesson(Lesson m);

    public abstract void deleteLesson(String id);

    public abstract Lesson findLesson(String id);

    public abstract List<Lesson> findAllLesson();
    
    public List<Lesson> findLessonByAdmin(AdminInfo admin);
    
    public List<Lesson> findAllShowedLesson();
    
    public List<Lesson> findAllShowedLessonByType(String typeId);
    
    public List<Lesson> findAllUntypedLesson();
    
    public List<Lesson> findMostLessonByType(String typeId);
    
    public long getLessonNumByType(String id);
    
    public long getShowedLessonNumByType(String id);

    public List<Lesson> findAllLessonByType(String typeId);

	List<Lesson> findAllLessonByTypes(List<String> ids);

	List<Lesson> findLessonByIdList(List<String> ids);

	List<Lesson> findLessonByType(String typeId);

	int deleteLessonsByLessonTypeId(List<String> idlist);

	List<LessonVO> findLessonVO(int firstSize, int pageSize);

	public abstract int getLessonsNumByOrder(Object object, boolean b);

	long getNumByCondition(Map<String, Object> fms, String businessId);
	
	/**
	 * 
	 * @param filterMap 模糊匹配字段和值map
	 * @param firstSize 
	 * @param pageSize
	 * @param field 排序字段
	 * @param type 排序方式（0正序，1：逆序）
	 * @return
	 */
	List<LessonVO> findLessonVOsByCondition(Map<String, Object> filterMap, int firstSize, int pageSize, String field,
			int type, String businessId);

	public abstract void updateLessonOrd(String id, int ord);
    

}
