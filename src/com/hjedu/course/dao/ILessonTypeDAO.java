package com.hjedu.course.dao;

import java.util.List;
import java.util.Map;

import com.hjedu.common.dao.BaseDAO;
import com.hjedu.course.entity.LessonType;
import com.hjedu.customer.entity.AdminInfo;
import com.hjedu.customer.entity.BbsUser;


public interface ILessonTypeDAO extends BaseDAO<LessonType>{
    
  public abstract void addLessonType(LessonType paramLessonType);

  public abstract LessonType findLessonType(String paramString);

  public abstract void updateLessonType(LessonType paramLessonType);

  public abstract List<LessonType> findAllLessonType(String businessId);
  
  public List<LessonType> findLessonTypeByAdmin(AdminInfo admin,String businessId);
  
  public List<LessonType> findLessonTypeByAdminAndTime(AdminInfo admin,String businessId);
  
  public List<LessonType> findLessonTypeByExam(String eid);

  public abstract void deleteLessonType(String paramString);

  List<LessonType> findAllEnableLessonType(String businessId);
  
  List<LessonType> findAllEnableLessonType2(String businessId);
  
  public abstract List<LessonType> findAllLessonTypeByTagId(String lid,String businessId);
  
  public abstract List<LessonType> findAllLessonTypeByTagId2(String lid,BbsUser user,String businessId);
  
  List<LessonType> findLessonTypesByName(String name,String businessId);
  
  List<LessonType> findLessonTypesByName2(String name,BbsUser user,String businessId);
  
  List<LessonType> findAllSecretedLessonType();

  public abstract List<LessonType> findLessonTypesByNumAndBusinessId(int parseInt, int parseInt2, String businessId);

  public abstract List<LessonType> findLessonTypesByQueryMap(Map<String, String> map);

List<LessonType> findLessonTypesByTagName(String id, int beginnum, int endnum,String businessId);

public abstract List<LessonType> findfreeLessonType();

List<LessonType> findLessonTypesByTeacher(String name);

List<LessonType> findLessonTypeByIdList(List<String> ids);

List<LessonType> findSecretLessonTypeByIdList(List<String> ids);

public abstract List<LessonType> findLessonTypesByTeacherId(String id);

/**
 * 获取是否（由ifIn确定）包含在idlist中的有练习的课程数量
 * @param idlist
 * @param ifIn
 * @return
 */
long getNumOfLessonTypesHasPractice(List<String> idlist, boolean ifIn, String businessId);

/**
 * 获取是否（由ifIn确定）包含在idlist中的有练习的课程
 * @param idlist 课程id集合
 * @param firstSize 起始行数
 * @param pageSize 每页行数
 * @param ifIn 是否在课程id集合中
 * @return
 */
List<LessonType> findLessonTypesHasPractice(List<String> idlist, int firstSize, int pageSize, boolean ifIn, String businessId);

List<LessonType> findLessonTypesByQueryStr(String queryStr, int firstSize, int pageSize);

long getLessonTypesNum(String queryStr,String businessId);

/**
 * 
 * @param orderStr 排序字段名称(null为不排序)
 * @param ifenabled 是否上架("true"是，"false"否，null为全查)
 * @param firstSize 起始行数
 * @param pageSize 每页行数
 * @param ifDesc 是否倒序排列
 * @param idlist 课程id集合
 * @param ifIn 是否在idlist集合中
 * @param queryStr 查询条件
 * @return
 */
List<LessonType> findLessonTypesByOrder(String orderStr, String businessId, String ifenabled, int firstSize, int pageSize, boolean ifDesc,
		List<String> idlist, boolean ifIn,String queryStr);

List<LessonType> findLessonTypesByOrder2(String orderStr, String ifenabled, int firstSize, int pageSize, boolean ifDesc,
		List<String> idlist, boolean ifIn, String queryStr);

List<LessonType> findLessonTypesByOrder3(String orderStr, String businessId, String ifenabled, int firstSize, int pageSize, boolean ifDesc,
		List<String> idlist, boolean ifIn, String queryStr,BbsUser user);

List<LessonType> findOnApprovalPendingLessonTypesByCourseType(List<String> idlist);

List<LessonType> findOnApprovalInLessonTypesByCourseType(List<String> idlist);

List<LessonType> findOnApprovalLessonTypesByCourseType(List<String> idlist, String opt);

/**
 * 查询审批课程列表
 * @param teacherId 讲师id
 * @param teacherType 讲师类型（0创建人，1审批人）
 * @param approvalStatus 审批状态（0免审批状态 1初始状态 2待审批 3审批中 4通过 5驳回）
 * @param enable 是否上架（0下架 1上架）
 * @param firstSize 分页起始行数
 * @param pageSize 分页每页行数
 * @return
 */
List<LessonType> findLessonTypesByBeApprover(String teacherId, int teacherType, int approvalStatus, int enable,
		int firstSize, int pageSize);

int deleteLessonTypes(List<String> idlist);

long findLessonTypeNumByBeApprover(String teacherId, int teacherType, int approvalStatus, int enable);

long getNumByOrder(String ifenabled, List<String> idlist, boolean ifIn);

List<LessonType> findLessonTypesByCondition(String whereSql, Map<String, Object> filterMap, int firstSize, int pageSize,
		String field, int type);

long getNumByCondition(String whereSql, Map<String, Object> filterMap);

}
