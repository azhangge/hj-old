package com.hjedu.customer.dao.impl;

import java.io.Serializable;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import com.hjedu.customer.dao.ITeacherDAO;
import com.hjedu.customer.entity.Teacher;

public class TeacherDAO implements ITeacherDAO, Serializable {

	private static final long serialVersionUID = 1L;
	@PersistenceContext
    private EntityManager entityManager;

    public EntityManager getEntityManager() {
        return this.entityManager;
    }
    public void setEntityManager(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @SuppressWarnings("unchecked")
	@Override
    public List<Teacher> findAllTeacher(String businessId) {
        String q = "Select t from Teacher t where t.businessId=:businessId order by t.ord";
        Query qu = this.entityManager.createQuery(q);
		qu.setParameter("businessId", businessId);
        List<Teacher> tl = qu.getResultList();
        return tl;
    }
    
    @Override
    public Teacher findTeacher(String id) {
        if (id != null) {
            Teacher teacher = this.entityManager.find(Teacher.class, id);
            return teacher;
        } else {
            return null;
        }
    }
    
    @SuppressWarnings("unchecked")
	@Override
    public Teacher findTeacherByUrn(String name,String businessId) {
        String q = "Select t from Teacher t where t.name=:urn and t.businessId=:businessId";
        Query qu = this.entityManager.createQuery(q);
        qu.setParameter("urn", name);
        qu.setParameter("businessId", businessId);
        List<Teacher> tl = qu.getResultList();
        if (tl.isEmpty()) {
            return null;
        } else {
            Teacher t = (Teacher) tl.get(0);
            return t;
        }
    }

    @SuppressWarnings("unchecked")
	@Override
    public List<Teacher> findTeachersLikeUrn(String urn,String businessId) {
        String q = "Select t from Teacher t where t.name like '%:urn % and t.businessId=:businessId'";
        Query qu = this.entityManager.createQuery(q);
        qu.setParameter("urn", urn);
        qu.setParameter("businessId", businessId);
        List<Teacher> tl = qu.getResultList();
        return tl;
    }

    @Override
    public void updateTeacher(Teacher u) {
        this.entityManager.merge(u);
    }

    @Override
    public void deleteTeacherByUrn(String urn,String bussinessId) {
        if (urn != null) {
            Teacher u = this.findTeacherByUrn(urn,bussinessId);
            if (u != null) {
                this.entityManager.remove(u);
            }
        }
    }
	@Override
	public void addTeacher(Teacher teacher) {
		this.entityManager.persist(teacher);
	}
	@Override
	public void deleteTeacher(String id) {
		if (id != null) {
            Teacher u = this.findTeacher(id);
            if (u != null) {
            	this.entityManager.remove(u);
            }
        }
	}

	@Override
	public List<Teacher> findAllShowTeacher(String businessId) {
		String q = "Select t from Teacher t where t.ifShow=1 and t.businessId=:businessId order by t.ord";
		Query qu = this.entityManager.createQuery(q);
		qu.setParameter("businessId", businessId);
        List<Teacher> ais = qu.getResultList();
        return ais;
	}
}
