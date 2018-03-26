package com.hjedu.examination.dao.impl;

import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import com.hjedu.examination.dao.IIntensiveExamAndUserDAO;
import com.hjedu.examination.entity.IntensiveExamAndUser;

public class IntensiveExamAndUserDAO implements IIntensiveExamAndUserDAO {
	
	@PersistenceContext
    private EntityManager entityManager;
	
	public EntityManager getEntityManager() {
		return entityManager;
	}

	public void setEntityManager(EntityManager entityManager) {
		this.entityManager = entityManager;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<IntensiveExamAndUser> getIntensiveExamAndUserByExamId(String examId) {
		String q = "select iea from IntensiveExamAndUser iea where iea.exam.id = :examId";
		return entityManager.createQuery(q).setParameter("examId", examId).getResultList();
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<IntensiveExamAndUser> getIntensiveExamAndUserByUserId(String userId) {
		String q = "select iea from IntensiveExamAndUser iea where iea.user.id = :userId";
		return entityManager.createQuery(q).setParameter("userId", userId).getResultList();
	}
	
	@Override
    public void addIntensiveExamAndUser(IntensiveExamAndUser u) {
        this.entityManager.persist(u);
    }
	
	@Override
    public void updateIntensiveExamAndUser(IntensiveExamAndUser u) {
        this.entityManager.merge(u);
    }

	@Override
	public IntensiveExamAndUser getIntensiveExamAndUserByUserExamId(String userId, String examId) {
		String q = "select iea from IntensiveExamAndUser iea where iea.user.id = :userId and iea.exam.id= :examId";
		Query qu = this.entityManager.createQuery(q);
	    qu.setParameter("examId", examId);
	    qu.setParameter("userId", userId);
	    List<IntensiveExamAndUser> llist = qu.getResultList();
	    if(llist.size()>0){
	    	return llist.get(0);
	    }else{
	    	return null;
	    }
	}
}
