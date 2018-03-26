package com.hjedu.course.dao.impl;

import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import com.hjedu.course.dao.IStudyPlanChangeLogDAO;
import com.hjedu.course.entity.StudyPlanChangeLog;

public class StudyPlanChangeLogDAO implements IStudyPlanChangeLogDAO{

    @PersistenceContext
    private EntityManager entityManager;

    public EntityManager getEntityManager() {
        return this.entityManager;
    }

    public void setEntityManager(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Override
    public void addStudyPlanChangeLog(StudyPlanChangeLog partnerModel) {
        entityManager.persist(partnerModel);
    }

    @Override
    public void deleteStudyPlanChangeLog(String id) {
        StudyPlanChangeLog cm = entityManager.find(StudyPlanChangeLog.class, id);
        entityManager.remove(cm);
    }

    @Override
    public List<StudyPlanChangeLog> findAllStudyPlanChangeLog() {
        String q = "Select cms from StudyPlanChangeLog cms order by cms.createTime desc";
        Query query = entityManager.createQuery(q);
        List<StudyPlanChangeLog> as = query.getResultList();
        return as;
    }
    
    @Override
    public StudyPlanChangeLog findStudyPlanChangeLog(String id) {
        return entityManager.find(StudyPlanChangeLog.class, id);

    }

    @Override
    public void updateStudyPlanChangeLog(StudyPlanChangeLog comModel) {
        entityManager.merge(comModel);
    }

    @Override
    public void deleteAll() {
        String q = "delete from StudyPlanChangeLog cms";
        entityManager.createQuery(q).executeUpdate();
    }

	@Override
	public void deleteLogByUser(String uid) {
		String q = "delete from StudyPlanChangeLog cms where cms.studyPlanLog.user.id = :id";
		entityManager.createQuery(q).setParameter("id", uid).executeUpdate();
	}

	@Override
	public List<StudyPlanChangeLog> findStudyPlanChangeLogByUsr(String uid) {
		String q = "Select cms from StudyPlanChangeLog cms where cms.studyPlanLog.user.id=:id order by cms.createTime desc";
        Query query = entityManager.createQuery(q).setParameter("id", uid);
        List<StudyPlanChangeLog> as = query.getResultList();
        return as;
	}

	@Override
	public void deleteLogByStudyPlanLog(String studyPlanLogId) {
		String q = "delete from StudyPlanChangeLog cms where cms.studyPlanLog.id = :id";
		entityManager.createQuery(q).setParameter("id", studyPlanLogId).executeUpdate();
	}

	@Override
	public List<StudyPlanChangeLog> findStudyPlanChangeLogBySPLAndUsr(String studyPlanLogId, String uid) {
		String q = "Select cms from StudyPlanChangeLog cms where cms.studyPlanLog.user.id=:uid and cms.studyPlanLog.id=:id order by cms.createTime desc";
        Query query = entityManager.createQuery(q).setParameter("id", studyPlanLogId).setParameter("uid", uid);
        List<StudyPlanChangeLog> as = query.getResultList();
        return as;
	}
	
	@Override
	public List<StudyPlanChangeLog> findStudyPlanChangeLogByPlanAndUsr(String studyPlanId, String uid) {
		String q = "Select cms from StudyPlanChangeLog cms where cms.studyPlanLog.user.id=:uid and cms.studyPlanLog.studyPlan.id=:id order by cms.createTime desc";
        Query query = entityManager.createQuery(q).setParameter("id", studyPlanId).setParameter("uid", uid);
        List<StudyPlanChangeLog> as = query.getResultList();
        return as;
	}

	@Override
	public List<StudyPlanChangeLog> findStudyPlanChangeLogByStudyPlanLog(String studyPlanLogId) {
		String q = "Select cms from StudyPlanChangeLog cms where cms.studyPlanLog.id=:id order by cms.createTime desc";
        Query query = entityManager.createQuery(q).setParameter("id", studyPlanLogId);
        List<StudyPlanChangeLog> as = query.getResultList();
        return as;
	}

	@Override
	public List<StudyPlanChangeLog> findStudyPlanChangeLogByExamAndUsr(String examId, String userId) {
		String q = "Select cms from StudyPlanChangeLog cms where cms.exam.id=:examId and cms.studyPlanLog.user.id=:uid order by cms.createTime desc";
        Query query = entityManager.createQuery(q).setParameter("examId", examId).setParameter("uid", userId);
        List<StudyPlanChangeLog> as = query.getResultList();
        return as;
	}

	@Override
	public List<StudyPlanChangeLog> findStudyPlanChangeLogBySPLAndUsrAndType(String studyPlanLogId, String userId,
			String type) {
		String q = "Select cms from StudyPlanChangeLog cms where cms.studyPlanLog.user.id=:uid and cms.studyPlanLog.id=:id and cms.logType=:type order by cms.createTime desc";
        Query query = entityManager.createQuery(q).setParameter("id", studyPlanLogId).setParameter("uid", userId).setParameter("type", type);
        List<StudyPlanChangeLog> as = query.getResultList();
        return as;
	}

}
