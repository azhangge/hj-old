package com.hjedu.examination.dao.impl;

import java.io.Serializable;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import com.hjedu.examination.dao.IExamDepartmentDAO;
import com.hjedu.examination.entity.ExamDepartment;
import com.huajie.util.SpringHelper;

public class ExamDepartmentDAO implements IExamDepartmentDAO, Serializable {

    @PersistenceContext
    private EntityManager entityManager;

    public EntityManager getEntityManager() {
        return this.entityManager;
    }

    public void setEntityManager(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Override
    public void addExamDepartment(ExamDepartment m) {
        this.entityManager.persist(m);
    }

    @Override
    public void deleteExamDepartment(String id) {
        ExamDepartment c = this.entityManager.find(ExamDepartment.class, id);
        this.entityManager.remove(c);
    }

    @Override
    public List<ExamDepartment> findAllExamDepartment(String rootId) {
        //System.out.println(root);
        String q = "Select cq from ExamDepartment cq where cq.id!=:id order by cq.ord desc";
        Query qu = this.entityManager.createQuery(q);
        qu.setParameter("id", rootId);
        List<ExamDepartment> cqs = qu.getResultList();
        return cqs;
    }
    
    @Override
    public List<ExamDepartment> findAllShowedExamDepartment(String rootId) {
        //System.out.println(root);
        String q = "Select cq from ExamDepartment cq where cq.id!=:id and cq.frontShow=true order by cq.ord desc";
        Query qu = this.entityManager.createQuery(q);
        qu.setParameter("id", rootId);
        List<ExamDepartment> cqs = qu.getResultList();
        return cqs;
    }

    @Override
    public List<ExamDepartment> findRootExamDepartment(String rootId) {
        String q = "Select cq from ExamDepartment cq where cq.fatherId=:id order by cq.genTime desc";
        Query qu = this.entityManager.createQuery(q);
       
        qu.setParameter("id", rootId);
        List<ExamDepartment> cqs = qu.getResultList();
        return cqs;
    }

    @Override
    public ExamDepartment findExamDepartment(String id) {
        ExamDepartment c = this.entityManager.find(ExamDepartment.class, id);
        return c;
    }

    @Override
    public void updateExamDepartment(ExamDepartment m) {
        this.entityManager.merge(m);
    }

    public static void main(String args[]) {
//        IExamDepartmentDAO deptDAO = SpringHelper.getSpringBean("ExamDepartmentDAO");
//        List<ExamDepartment> depts = deptDAO.findAllExamDepartment();
//        System.out.println(depts.size());
//        for (ExamDepartment d : depts) {
//            System.out.println(d.getName());
//        }
    }
}
