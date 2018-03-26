package com.hjedu.course.dao.impl;

import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import com.hjedu.course.dao.IStudyPlanLogDAO;
import com.hjedu.course.entity.StudyPlanLog;

public class StudyPlanLogDAO implements IStudyPlanLogDAO{

    @PersistenceContext
    private EntityManager entityManager;

    public EntityManager getEntityManager() {
        return this.entityManager;
    }

    public void setEntityManager(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Override
    public void addStudyPlanLog(StudyPlanLog partnerModel) {
        entityManager.persist(partnerModel);
    }

    @Override
    public void deleteStudyPlanLog(String id) {
        StudyPlanLog cm = entityManager.find(StudyPlanLog.class, id);
        entityManager.remove(cm);
    }

    @Override
    public List<StudyPlanLog> findAllStudyPlanLog() {
        String q = "Select cms from StudyPlanLog cms order by cms.createTime desc";
        Query query = entityManager.createQuery(q);
        List<StudyPlanLog> as = query.getResultList();
        return as;
    }
    
    @Override
    public StudyPlanLog findStudyPlanLog(String id) {
        return entityManager.find(StudyPlanLog.class, id);

    }

    @Override
    public void updateStudyPlanLog(StudyPlanLog comModel) {
        entityManager.merge(comModel);
    }

    @Override
    public void deleteAll() {
        String q = "delete from StudyPlanLog cms";
        entityManager.createQuery(q).executeUpdate();
    }

	@Override
	public List<StudyPlanLog> findStudyPlanLogByUserId(String uid) {
		String q = "Select cms from StudyPlanLog cms where cms.user.id=:id order by cms.createTime desc";
        Query query = entityManager.createQuery(q).setParameter("id", uid);
        List<StudyPlanLog> as = query.getResultList();
        return as;
	}

	@Override
	public List<StudyPlanLog> findStudyPlanLogBySPId(String studyPlanId) {
		String q = "Select cms from StudyPlanLog cms where cms.studyPlan.id=:id order by cms.createTime desc";
        Query query = entityManager.createQuery(q).setParameter("id", studyPlanId);
        List<StudyPlanLog> as = query.getResultList();
        return as;
	}

	@Override
	public StudyPlanLog findStudyPlanLogBySPAndUsr(String studyPlanId, String userid) {
		String q = "Select cms from StudyPlanLog cms where cms.studyPlan.id=:id and cms.user.id=:uid order by cms.createTime desc";
        Query query = entityManager.createQuery(q).setParameter("id", studyPlanId).setParameter("uid", userid);
        List<StudyPlanLog> as = query.getResultList();
        if(as!=null&&as.size()>0){
        	return as.get(0);
        }else{
        	return null;
        }
	}

	@Override
	public int findFinishedStudyPlanLogNum(String studyPlanId,String logId) {
		String q = "Select count(cms) from StudyPlanLog cms where cms.ifFinished=1 and cms.id!=:logId and cms.studyPlan.id=:planId";
        Query query = entityManager.createQuery(q).setParameter("logId", logId).setParameter("planId", studyPlanId);
        List<StudyPlanLog> as = query.getResultList();
        if(as!=null){
        	return Integer.valueOf(String.valueOf(as.get(0)));
        }else{
        	return 0;
        }
	}

	@Override
	public void deleteStudyPlanLogByStudyPlanId(String id) {
		List<StudyPlanLog> spls = findStudyPlanLogBySPId(id);
		for(StudyPlanLog spl : spls){
			entityManager.remove(spl);
		}
	}
    
}
