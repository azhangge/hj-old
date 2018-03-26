package com.hjedu.course.dao;

import java.util.List;

import com.hjedu.examination.entity.CourseType;

public interface ICourseTypeDAO {

    public abstract void addCourseType(CourseType ct);

    public abstract CourseType findCourseType(String ctid);

    public abstract void updateCourseType(CourseType ct);

    public abstract List<CourseType> findAllCourseTypeWithoutRoot(String rootId);

    public abstract List<CourseType> findAllSonCourseType(String fatherID, String businessId);

    public abstract void deleteCourseType(String ctid);

    public void deleteCourseTypeAndAllDescendants(String id, String businessId);

    public List<CourseType> loadAllDescendants(String fid, String businessId);

    public void loadAllDescendants(String fid, List<CourseType> sons, String businessId);

    public CourseType findCourseTypeByNameAndBusinessId(String name, String businessId);

    public void deleteAllCourseTypeWithoutRoot(String rootId);

    List<CourseType> findNavigationCourseTypeByFatherId(String fatherId);

	List<CourseType> findFrontShowCourseTypeByFatherId(String fatherId);

	List<CourseType> findFirstCourseType(String rootId);

	List<CourseType> findSomeCourseType(List<String> idList);
}
