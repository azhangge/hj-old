/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.hjedu.platform.dao.impl;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import com.hjedu.platform.dao.IUserSessionStateDAO;
import com.hjedu.platform.entity.UserSessionState;
import com.huajie.util.SpringHelper;

public class UserSessionStateDAO implements IUserSessionStateDAO, Serializable {

    @PersistenceContext
    private EntityManager entityManager;

    public EntityManager getEntityManager() {
        return this.entityManager;
    }

    public void setEntityManager(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Override
    public void addUserSessionState(UserSessionState partnerModel) {
        entityManager.persist(partnerModel);
    }

    @Override
    public void deleteUserSessionState(String id) {
        UserSessionState cm = entityManager.find(UserSessionState.class, id);
        entityManager.remove(cm);
    }

    

    @Override
    public List<UserSessionState> findAllUserSessionState() {
        String q = "Select cms from UserSessionState cms order by cms.genTime desc";
        Query query = entityManager.createQuery(q);
        List<UserSessionState> as = query.getResultList();
        return as;
    }

    @Override
    public List<UserSessionState> findUserSessionStateByUsr(final String uid) {
        String q = "Select cms from UserSessionState cms where cms.user.id=:uid order by cms.genTime desc";
        Query query = entityManager.createQuery(q);
        query.setParameter("uid", uid);
        List<UserSessionState> as = query.getResultList();
        return as;
    }

    @Override
    public UserSessionState findUserSessionStateBySessionId(final String sid) {
        String q = "Select us from UserSessionState us where us.sessionId=:sid";
        Query qu = this.entityManager.createQuery(q);
        qu.setParameter("sid", sid);
        List<UserSessionState> as = qu.getResultList();
        if (as.isEmpty()) {
            return null;
        } else {
            UserSessionState ai = (UserSessionState) as.get(0);
            return ai;
        }
    }

    @Override
    public UserSessionState findUserSessionState(String id) {
        return entityManager.find(UserSessionState.class, id);

    }

    @Override
    public void updateUserSessionState(UserSessionState comModel) {
        entityManager.merge(comModel);
    }

    @Override
    public void deleteAll() {
        String q = "delete from UserSessionState cms";
        entityManager.createQuery(q).executeUpdate();
    }

    @Override
    public void deleteAllOld() {
        float hours=SpringHelper.getSpringBean("session_max_hours");
        Date d = new Date(System.currentTimeMillis() - (long)(hours * 3600 * 1000));
        String q = "delete from UserSessionState cms where cms.genTime < :tt and cms.loginTime<:tt and cms.examTime < :tt and cms.endExamTime < :tt and cms.endModuleExamTime < :tt";
        Query qu = entityManager.createQuery(q);
        qu.setParameter("tt", d);
        qu.executeUpdate();
    }
    
    @Override
    public void deleteUserSessionStateBySessionId(String sid) {
        String q = "delete from UserSessionState cms where cms.sessionId=:sid";
        Query qu = entityManager.createQuery(q);
        qu.setParameter("sid", sid);
        qu.executeUpdate();
    }

    public static void main(String args[]) {
        IUserSessionStateDAO dao = SpringHelper.getSpringBean("UserSessionStateDAO");
        //dao.deleteUserSessionStateBySessionId("189082DA388278E48B5957934F4E7B5D");
        dao.deleteAllOld();
    }

}
