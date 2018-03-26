package com.hjedu.course.dao.impl;

import java.io.Serializable;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.springframework.stereotype.Repository;

import com.hjedu.common.dao.BaseDAO;
import com.hjedu.course.dao.ILessonLogDAO;
import com.hjedu.course.entity.Lesson;
import com.hjedu.course.entity.LessonLog;
import com.hjedu.course.entity.LessonType;
import com.hjedu.customer.entity.AdminInfo;
import com.huajie.app.util.QueryUtil;
import com.huajie.app.util.StringUtil;

@Repository("LessonLogDAO")
public class LessonLogDAO implements ILessonLogDAO, Serializable {

    @PersistenceContext
    private EntityManager entityManager;

    public EntityManager getEntityManager() {
        return this.entityManager;
    }

    public void setEntityManager(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Override
    public void addLessonLog(LessonLog partnerModel) {
        entityManager.persist(partnerModel);
    }

    @Override
    public void deleteLessonLog(String id) {
        LessonLog cm = entityManager.find(LessonLog.class, id);
        entityManager.remove(cm);
    }
    
    @Override
    public long getFinishedLogNumByTypeAndUser(String tid,String uid) {
        String q = "Select count(ms) from LessonLog ms where ms.lesson.lessonType.id=:tid and ms.user.id=:uid";
        Query qu = this.entityManager.createQuery(q);
        qu.setParameter("tid", tid);
        qu.setParameter("uid", uid);
        long num = ((Long) (qu.getResultList().get(0))).longValue();
        return num;
    }

    @Override
    public List<LessonLog> findAllLessonLog(String businessId) {
        String q = "Select cms from LessonLog cms where cms.businessId=:businessId order by cms.genTime desc";
        Query query = entityManager.createQuery(q);
        query.setMaxResults(50); //查询最近50条
        query.setParameter("businessId", businessId);
        List<LessonLog> as = query.getResultList();
        return as;
    }
    
    @Override
    public List<LessonLog> findLessonLogByAdmin(AdminInfo admin,String businessId) {
        String q = "select yis from LessonLog yis where yis.businessId=:businessId and :admin member of yis.lesson.lessonType.admins order by yis.genTime desc";
        Query qu=entityManager.createQuery(q);
        qu.setParameter("admin", admin);
        qu.setParameter("businessId", businessId);
        List<LessonLog> ps = qu.getResultList();
        return ps;
    }

    @Override
    public List<LessonLog> findLessonLogByUsr(String id) {
        String q = "Select ll from LessonLog ll where ll.user.id=:id order by ll.genTime desc";
        Query query = entityManager.createQuery(q);
        query.setParameter("id", id);
        List<LessonLog> as = query.getResultList();
        return as;
    }
    
    @Override
    public List<LessonLog> findLessonLogByTypeAndUsr(final String uid,String tid) {
        String q = "Select cms from LessonLog cms where cms.user.id=:uid and cms.lesson.lessonType.id=:tid order by cms.genTime desc";
        Query query = entityManager.createQuery(q);
        query.setParameter("uid", uid);
        query.setParameter("tid", tid);
//        query.setMaxResults(1000);
        List<LessonLog> as = query.getResultList();
        return as;
    }
    
    @Override
    public List<LessonLog> findLessonLogByLesson(final String uid) {
        String q = "Select cms from LessonLog cms where cms.lesson.id=:uid order by cms.genTime desc";
        Query query = entityManager.createQuery(q);
        query.setParameter("uid", uid);
//        query.setMaxResults(1000);
        List<LessonLog> as = query.getResultList();
        return as;
    }
    
    @Override
    public List<LessonLog> findLessonLogByLessonAndUsr(final String lid,final String uid) {
        String q = "Select cms from LessonLog cms where cms.lesson.id=:lid and cms.user.id=:uid order by cms.genTime desc";
        Query query = entityManager.createQuery(q);
        query.setParameter("lid", lid);
        query.setParameter("uid", uid);
//        query.setMaxResults(1000);
        List<LessonLog> as = query.getResultList();
        return as;
    }
    
    @Override
    public LessonLog findLessonLog(String id) {
        return entityManager.find(LessonLog.class, id);

    }

    @Override
    public void updateLessonLog(LessonLog comModel) {
    	comModel.setGenTime(new Date());
        entityManager.merge(comModel);
    }

    @Override
    public void deleteAll() {
        String q = "delete from LessonLog cms";
        entityManager.createQuery(q).executeUpdate();
    }
    
    @Override
    public void deleteLogByLesson(String lid) {
        String q = "delete from LessonLog cms where cms.lesson.id=:lid";
        Query qu=entityManager.createQuery(q);
        qu.setParameter("lid", lid);
        qu.executeUpdate();
    }
    
    @Override
    public void deleteLogByUser(String lid) {
        String q = "delete from LessonLog cms where cms.user.id=:lid";
        Query qu=entityManager.createQuery(q);
        qu.setParameter("lid", lid);
        qu.executeUpdate();
    }
    
    @Override
    public void deleteLogByUserAndType(String userId,String typeId) {
        String q = "delete from LessonLog cms where cms.user.id=:userId and cms.lesson.lessonType.id=:typeId";
        Query qu=entityManager.createQuery(q);
        qu.setParameter("userId", userId);
        qu.setParameter("typeId", typeId);
        qu.executeUpdate();
    }
    
    @Override
    public void deleteLogByType(String typeId) {
        String q = "delete from LessonLog cms where cms.lesson.lessonType.id=:typeId";
        Query qu=entityManager.createQuery(q);
        qu.setParameter("typeId", typeId);
        qu.executeUpdate();
    }
    
    @Override
    public long getTotalLessonLogBbsScore(String uid, Date btime, Date etime) {
        long t = 0;
        String q = "Select cms from LessonLog cms where cms.user.id=:uid and cms.genTime<= :etime and cms.genTime>= :btime  order by cms.genTime desc";
        Query qu = entityManager.createQuery(q);
        qu.setParameter("uid", uid);
        qu.setParameter("btime", btime);
        qu.setParameter("etime", etime);
        List<LessonLog> as = qu.getResultList();
        for (LessonLog ll : as) {
            if (ll.isFinished()) {
                t += ll.getLesson().getBbsScore();
            }
        }
        return t;
    }

	@Override
	public List<LessonLog> findFinishedLessonLogsByUsr(String userId) {
		String q = "Select cms from LessonLog cms where cms.user.id=:uid and cms.finished=1 order by cms.genTime desc";
        Query query = entityManager.createQuery(q);
        query.setParameter("uid", userId);
        List<LessonLog> as = query.getResultList();
        return as;
	}

	/**
	 * 分页查询学习资料列表
	 * @param whereSql 条件
	 * @param filterMap 
	 * @param firstSize 分页起始行数
	 * @param pageSize 分页每页行数
	 * @param field 排序字段
	 * @param type 排序方式（1倒序，0正序）
	 * @return
	 */
	public List<LessonLog> findLessonsByCondition(String whereSql, Map<String, Object> filterMap, int firstSize, int pageSize,
			String field, int type){
		String q = "select ll.id,ll.lesson.name,ll.lesson.lessonType.name,ll.user.name,ll.lesson.leastTime,ll.timeFinished,ll.finished from LessonLog ll where "+whereSql+" ";
		if(filterMap!=null){
        	q += getQueryStrByFilter(filterMap, "l");
        }
//        if(StringUtil.isNotEmpty(field)){
//        	if(field.equals("LessonName")){
//        		field = field.replace("LessonName", "ll.lesson.lessontype.name");
//        	}
//        	q += QueryUtil.getOrderStrByOrder(field, "l", type);
//        }
        Query qu = this.entityManager.createQuery(q);
        QueryUtil.setQuerySize(qu, firstSize, pageSize);
		List<LessonLog> lgs = qu.getResultList();
        return lgs;
	}
	
	private String getQueryStrByFilter(Map<String, Object> filterMap,String tableName){
		String q = "";
		if(filterMap!=null){
			for (Entry<String, Object> entry : filterMap.entrySet()) {
				if(entry!=null){
					String filterProperty = entry.getKey();
					String filterValue = entry.getValue().toString();
					if (StringUtil.isNotEmpty(filterValue)) {
						q = q + " and "+tableName+"." + filterProperty + " like '%" + filterValue + "%' ";
					}
				}
			}
		}
		return q;
	}

	public long getNumByCondition(Map<String, Object> filterMap) {
        String q = "select count(ll) from LessonLog ll where 1=1 ";
        q += getQueryStrByFilter(filterMap, "ll");
        Query qu = this.entityManager.createQuery(q);
        return QueryUtil.getNumByQuery(qu);
    }
}
