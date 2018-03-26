package com.hjedu.customer.dao.impl;

import java.io.Serializable;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import com.hjedu.customer.dao.IBbsUserGradeDAO;
import com.hjedu.platform.entity.BbsUserGrade;

public class BbsUserGradeDAO implements IBbsUserGradeDAO,Serializable {

    @PersistenceContext
    private EntityManager entityManager;

    public EntityManager getEntityManager() {
        return this.entityManager;
    }

    public void setEntityManager(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Override
    public void addUserGrade(BbsUserGrade grade) {
        entityManager.persist(grade);
    }

    @Override
    public void updateUserGrade(BbsUserGrade grade) {
        this.entityManager.merge(grade);
    }

    @Override
    public void deleteUserGrade(String id) {
        BbsUserGrade entity = entityManager.find(BbsUserGrade.class, id);
        this.entityManager.remove(entity);
    }

    @Override
    public BbsUserGrade findUserGrade(String id) {
        BbsUserGrade instance = entityManager.find(BbsUserGrade.class, id);
        return instance;
    }

    @Override
    public List<BbsUserGrade> findAllUserGrade() {
        String q = "Select ass from BbsUserGrade ass order by ass.begainScore";
        List<BbsUserGrade> ys = entityManager.createQuery(q).getResultList();
        return ys;
    }
}
