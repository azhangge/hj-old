package com.hjedu.examination.dao.impl;

import java.io.Serializable;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import com.hjedu.examination.dao.IExamRoomDAO;
import com.hjedu.examination.entity.ExamRoom;

public class ExamRoomDAO implements IExamRoomDAO, Serializable {

    @PersistenceContext
    private EntityManager entityManager;

    public EntityManager getEntityManager() {
        return this.entityManager;
    }

    public void setEntityManager(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Override
    public void addExamRoom(ExamRoom m) {
        this.entityManager.persist(m);
    }

    @Override
    public void deleteExamRoom(String id) {
        ExamRoom c = this.entityManager.find(ExamRoom.class, id);
        this.entityManager.remove(c);
    }

    @Override
    public List<ExamRoom> findAllExamRoom() {
        String q = "Select cq from ExamRoom cq order by cq.genTime desc";
        List<ExamRoom> cqs = this.entityManager.createQuery(q).getResultList();
        return cqs;
    }

    @Override
    public ExamRoom findExamRoom(String id) {
        ExamRoom c = this.entityManager.find(ExamRoom.class, id);
        return c;
    }

    @Override
    public void updateExamRoom(ExamRoom m) {
        this.entityManager.merge(m);
    }
}
