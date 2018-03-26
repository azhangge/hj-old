package com.hjedu.customer.dao.impl;

import java.io.Serializable;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import com.hjedu.customer.dao.IExternalUserDAO;
import com.hjedu.customer.entity.ExternalUser;
import com.huajie.util.MyLogger;

public class ExternalUserDAO implements IExternalUserDAO, Serializable {

    @PersistenceContext
    private EntityManager entityManager;

    public EntityManager getEntityManager() {
        return this.entityManager;
    }

    public void setEntityManager(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    public void addExternalUser(ExternalUser user) {
        this.entityManager.persist(user);
    }

    public void deleteExternalUser(String id) {
        ExternalUser u = this.entityManager.find(ExternalUser.class, id);
        this.entityManager.remove(u);
    }

    public List<ExternalUser> findAllExternalUser() {
        String q = "Select us from ExternalUser us order by  us.regTime desc";
        List<ExternalUser> ais = entityManager.createQuery(q).getResultList();
        return ais;
    }

    public List<ExternalUser> findAllExternalUserOrderByDept() {
        String q = "Select us from ExternalUser us order by us.groupId, us.enabled desc ,us.activated  desc";
        List<ExternalUser> ais = entityManager.createQuery(q).getResultList();
        return ais;
    }

    public ExternalUser findExternalUser(String id) {
        if (id != null) {
            ExternalUser user = this.entityManager.find(ExternalUser.class, id);
            return user;
        } else {
            return null;
        }

    }

    public ExternalUser findExternalUserByNo(final String urn) {
        String q = "Select us from ExternalUser us where us.tellerNo=:urn";
        Query qu = this.entityManager.createQuery(q);
        qu.setParameter("urn", urn);
        List<ExternalUser> as = qu.getResultList();
        if (as.isEmpty()) {
            return null;
        } else {
            ExternalUser ai = (ExternalUser) as.get(0);
            return ai;
        }
    }

    @Override
    public List<ExternalUser> findExternalUsersLikeUrn(final String urn) {
        String q = "Select us from ExternalUser us where us.username like '%" + urn + "%'";
        List<ExternalUser> as = entityManager.createQuery(q).getResultList();
        return as;
    }

    public ExternalUser findExternalUserByEmail(final String email) {
        String q = "Select us from ExternalUser us where us.email=:email";
        Query qu = this.entityManager.createQuery(q);
        qu.setParameter("email", email);
        List<ExternalUser> as = qu.getResultList();
        if (as.isEmpty()) {
            return null;
        } else {
            ExternalUser ai = (ExternalUser) as.get(0);
            return ai;
        }
    }

    @Override
    public ExternalUser findExternalUserByPid(String pid) {
        String q = "Select us from ExternalUser us where us.pid=:pid";
        Query qu = this.entityManager.createQuery(q);
        qu.setParameter("pid", pid);
        List<ExternalUser> as = qu.getResultList();
        if (as.isEmpty()) {
            return null;
        } else {
            ExternalUser ai = (ExternalUser) as.get(0);
            return ai;
        }
    }

    @Override
    public ExternalUser findExternalUserByCid(String cid) {
        String q = "Select us from ExternalUser us where us.cid=:cid";
        Query qu = this.entityManager.createQuery(q);
        qu.setParameter("cid", cid);
        List<ExternalUser> as = qu.getResultList();
        if (as.isEmpty()) {
            return null;
        } else {
            ExternalUser ai = (ExternalUser) as.get(0);
            return ai;
        }
    }

    @Override
    public long getExternalUserNum() {
        String q = "Select count(ms) from ExternalUser ms";
        Query qu = this.entityManager.createQuery(q);
        long num = ((Long) (qu.getResultList().get(0))).longValue();
        return num;
    }

    public void updateExternalUser(ExternalUser u) {
        this.entityManager.merge(u);
    }

}
