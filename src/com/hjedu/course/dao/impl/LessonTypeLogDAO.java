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

import com.hjedu.common.dao.impl.BaseDAOImpl;
import com.hjedu.course.dao.ILessonTypeDAO;
import com.hjedu.course.dao.ILessonTypeLogDAO;
import com.hjedu.course.entity.LessonType;
import com.hjedu.course.entity.LessonTypeLog;
import com.hjedu.course.vo.LessonTypeLogVO;
import com.hjedu.course.vo.LessonVO;
import com.huajie.app.util.QueryUtil;
import com.huajie.app.util.StringUtil;
import com.huajie.util.SpringHelper;

public class LessonTypeLogDAO extends BaseDAOImpl<LessonTypeLog> implements ILessonTypeLogDAO, Serializable {

    @PersistenceContext
    private EntityManager entityManager;

    public EntityManager getEntityManager() {
        return this.entityManager;
    }

    public void setEntityManager(EntityManager entityManager) {
        this.entityManager = entityManager;
    }
    
//    private String simpleHQLPart = "select new LessonTypeLog (l.id,l.name,lt.name,l.classNum,l.ord,l.availableBegain,l.availableEnd) from Lesson l left join LessonType lt on l.lessonType.id=lt.id where 1=1 ";

    @Override
    public void addLessonTypeLog(LessonTypeLog partnerModel) {
        entityManager.persist(partnerModel);
        //课程用户数+1
        ILessonTypeDAO LessonTypeDAO = SpringHelper.getSpringBean("LessonTypeDAO");
        LessonType lt = partnerModel.getLessonType();
        LessonType lt2 = LessonTypeDAO.findById(lt.getId());
        if(lt2!=null && lt!=null){
        	lt2.setUserNum(lt2.getUserNum()+1);
            LessonTypeDAO.updateLessonType(lt2);
        }
    }

    @Override
    public void deleteLessonTypeLog(String id) {
        LessonTypeLog cm = entityManager.find(LessonTypeLog.class, id);
        entityManager.remove(cm);
    }
    
    @Override
    public List<LessonTypeLog> findLessonTypeLogByLessonType(String tid) {
        String q = "Select ms from LessonTypeLog ms where ms.lessonType.id=:tid order by ms.genTime desc";
        Query qu = this.entityManager.createQuery(q);
        qu.setParameter("tid", tid);
        List<LessonTypeLog> as = qu.getResultList();
        return as;
    }

    @Override
    public List<LessonTypeLog> findAllLessonTypeLog(String businessId) {
        String q = "Select cms from LessonTypeLog cms where cms.businessId=:businessId order by cms.genTime desc";
        Query query = entityManager.createQuery(q);
        query.setMaxResults(50);//查最近50条
        query.setParameter("businessId", businessId);
        List<LessonTypeLog> as = query.getResultList();
        return as;
    }
    
    @Override
    public List<LessonTypeLog> findLessonTypeLogByUsr(final String uid) {
        String q = "Select cms from LessonTypeLog cms where cms.user.id=:uid order by cms.genTime desc";
        Query query = entityManager.createQuery(q);
        query.setParameter("uid", uid);
        query.setMaxResults(50);//查最近50条
        List<LessonTypeLog> as = query.getResultList();
        return as;
    }
    
    @Override
    public LessonTypeLog findLessonTypeLogByTypeAndUsr(String typeId,String userId) {
        String q = "Select cms from LessonTypeLog cms where cms.user.id=:uid and cms.lessonType.id=:tid order by cms.genTime desc";
        Query query = entityManager.createQuery(q);
        query.setParameter("uid", userId);
        query.setParameter("tid", typeId);
        List<LessonTypeLog> as = query.getResultList();
        if(as!=null&&as.size()>0){
        	return as.get(0);
        }else{
        	return null;
        }
    }
    
    @Override
    public LessonTypeLog findLessonTypeLog(String id) {
        return entityManager.find(LessonTypeLog.class, id);

    }

    @Override
    public void updateLessonTypeLog(LessonTypeLog comModel) {
        entityManager.merge(comModel);
    }

    @Override
    public void deleteAll() {
        String q = "delete from LessonTypeLog cms";
        entityManager.createQuery(q).executeUpdate();
    }
    
    @Override
    public void deleteLogByLessonType(String lid) {
        String q = "delete from LessonTypeLog cms where cms.lessonType.id=:lid";
        Query qu=entityManager.createQuery(q);
        qu.setParameter("lid", lid);
        qu.executeUpdate();
    }
    
    @Override
    public void deleteLogByLessonTypeAndUser(String lid,String userId) {
        String q = "delete from LessonTypeLog cms where cms.lessonType.id=:lid and cms.user.id=:userId";
        Query qu=entityManager.createQuery(q);
        qu.setParameter("lid", lid);
        qu.setParameter("userId", userId);
        qu.executeUpdate();
    }
    
    @Override
    public void deleteLogByUser(String lid) {
        String q = "delete from LessonTypeLog cms where cms.user.id=:lid";
        Query qu=entityManager.createQuery(q);
        qu.setParameter("lid", lid);
        qu.executeUpdate();
    }
    
    @Override
    public long getTotalLessonTypeLogBbsScore(String uid, Date btime, Date etime) {
        long t = 0;
        String q = "Select cms from LessonTypeLog cms where cms.user.id=:uid and cms.genTime<= :etime and cms.genTime>= :btime  order by cms.genTime desc";
        Query qu = entityManager.createQuery(q);
        qu.setParameter("uid", uid);
        qu.setParameter("btime", btime);
        qu.setParameter("etime", etime);
        List<LessonTypeLog> as = qu.getResultList();
        for (LessonTypeLog ll : as) {
            if (ll.isFinished()) {
//                t += ll.getLessonType().getBbsScore();
            }
        }
        return t;
    }

	@Override
	public List<LessonTypeLog> findFinishedLessonTypeLogsByUsr(String userId) {
		String q = "Select cms from LessonTypeLog cms where cms.user.id=:uid and cms.finished=1 order by cms.genTime desc";
        Query query = entityManager.createQuery(q);
        query.setParameter("uid", userId);
        List<LessonTypeLog> as = query.getResultList();
        return as;
	}
    
	@Override
	public Map<String, Object> findLessonTypeLogsByUsr(String userId,int firstSize,int pageSize) {
//		String q = "Select cms from LessonTypeLog cms where cms.user.id=:uid order by cms.updateTime desc";
//        Query query = entityManager.createQuery(q);
//        query.setParameter("uid", userId);
//        QueryUtil.setQuerySize(query, firstSize, pageSize);
//        List<LessonTypeLog> as = query.getResultList();
//        return as;
		String wheresql = " o.user.id=?1";
		Object[] queryParams = {userId};
		LinkedHashMap<String, String> orderby = new LinkedHashMap<>();
		orderby.put("updateTime", "desc");
		Map<String, Object> map = this.getScrollData(LessonTypeLog.class, firstSize, pageSize, wheresql, queryParams, orderby);
		return map;
	}

	@Override
	public long getNumByCondition(Map<String, Object> fms, String businessId) {
		String q = "select count(l) from LessonTypeLog l where 1=1 and l.businessId=:businessId";
		q += getLessonTypeLogQueryStrByFilter(fms, "l");
        Query qu = this.entityManager.createQuery(q);
        qu.setParameter("businessId", businessId);
        return QueryUtil.getNumByQuery(qu);
	}
	
	private String getLessonTypeLogQueryStrByFilter(Map<String, Object> filterMap,String tableName){
		String q = "";
		if(filterMap!=null){
			for (Entry<String, Object> entry : filterMap.entrySet()) {
				if(entry!=null){
					String filterProperty = entry.getKey();
					String filterValue = entry.getValue().toString();
					if (StringUtil.isNotEmpty(filterValue)) {
						if(filterProperty.equals("courseName")){
							q = q + " and "+tableName+"." + "lessonType.name" + " like '%" + filterValue + "%' ";
						}
						else if(filterProperty.equals("username")){
							q = q + " and "+tableName+"." + "user.username" + " like '%" + filterValue + "%' ";
						}
						else if(filterProperty.equals("postType")){
							q = q + " and "+tableName+"." + "user.postType" + " like '%" + filterValue + "%' ";
						}
						else if(filterProperty.equals("groupCnStr")){
							q = q + " and "+tableName+"." + "user.groupCnStr" + " like '%" + filterValue + "%' ";
						}
						else if(filterProperty.equals("lastTime")){
							q = q + " and "+tableName+"." + "user.lastTime" + " like '%" + filterValue + "%' ";
						}
						else if(filterProperty.equals("totalClassNum")){
							q = q + " and "+tableName+"." + "lessonType.totalClassNum" + " like '%" + filterValue + "%' ";
						}
						else{
							q = q + " and "+tableName+"." + filterProperty + " like '%" + filterValue + "%' ";
						}
					}
				}
			}
		}
		return q;
	}

	@Override
	public List<LessonTypeLogVO> findLessonTypeLogVOsByCondition(Map<String, Object> filterMap, int firstSize,
			int pageSize, String field, int type, String businessId) {
		String q = "select new "+LessonTypeLog.class.toString()+"(ltl.id,l.name,lt.name,ltl.finished,ltl.finishedClassNum,u.username,u.postType,u.groupCnStr,u.lastTime) from LessonTypeLog ltl left join ltl.lessonType lt left join ltl.user u where 1=1 and l.businessId=:businessId ";
        if(filterMap!=null){
        	q += getLessonTypeLogQueryStrByFilter(filterMap, "l");
        }
        if(StringUtil.isNotEmpty(field)){
        	if(field.equals("courseName")){
        		field = field.replace("courseName", "lessonType.name");
        	}
        	q += QueryUtil.getOrderStrByOrder(field, "l", type);
        }
        Query qu = this.entityManager.createQuery(q);
        qu.setParameter("businessId", businessId);
        QueryUtil.setQuerySize(qu, firstSize, pageSize);
        @SuppressWarnings("unchecked")
		List<LessonTypeLogVO> cqs = qu.getResultList();
        return cqs;
	}

	@Override
	public List<LessonTypeLog> findLessonTypeLogByUsrAndMaxResults(String uid, int maxResults) {
		 String q = "Select cms from LessonTypeLog cms where cms.user.id=:uid order by cms.updateTime desc";
	        Query query = entityManager.createQuery(q);
	        query.setParameter("uid", uid);
	        query.setMaxResults(maxResults);
	        List<LessonTypeLog> as = query.getResultList();
	        return as;
	}
}
