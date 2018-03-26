/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.hjedu.platform.dao.impl;

import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import com.hjedu.platform.dao.INetStatDAO;
import com.hjedu.platform.entity.NetStat;

public class NetStatDAO implements INetStatDAO {

    @PersistenceContext
    private EntityManager entityManager;

    public EntityManager getEntityManager() {
        return this.entityManager;
    }

    public void setEntityManager(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Override
    public void addNetStat(NetStat stat) {
        this.getEntityManager().persist(stat);
    }

    @Override
    public NetStat findRecentStat() {
        String q = "Select cs from NetStat cs order by cs.genTime desc";
        Query qu = this.entityManager.createQuery(q);
        List lycs = qu.getResultList();
        if (lycs.isEmpty()) {
            return null;
        } else {
            return (NetStat) lycs.get(0);
        }

    }
}
