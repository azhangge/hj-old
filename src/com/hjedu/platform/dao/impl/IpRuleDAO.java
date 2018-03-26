package com.hjedu.platform.dao.impl;

import java.io.Serializable;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import com.hjedu.platform.dao.IIpRuleDAO;
import com.hjedu.platform.entity.IpRule;

public class IpRuleDAO implements IIpRuleDAO ,Serializable{

    @PersistenceContext
    private EntityManager entityManager;

    public EntityManager getEntityManager() {
        return this.entityManager;
    }

    public void setEntityManager(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    public void updateIpRule(IpRule ca) {
        this.entityManager.merge(ca);
    }

    public IpRule findIpRule(String id) {
        return this.entityManager.find(IpRule.class, id);
    }

    public List<IpRule> findAllIpRule() {
        String q = "Select cs from IpRule cs order by cs.genTime desc";
        List<IpRule> ais = this.entityManager.createQuery(q).getResultList();
        return ais;
    }

    @Override
    public List<IpRule> findAllBlackIpRule() {
        String q = "Select cs from IpRule cs where cs.wallType='black' and cs.ifInUse='true' order by cs.genTime desc";
        List<IpRule> ais = this.entityManager.createQuery(q).getResultList();
        return ais;
    }

    @Override
    public List<IpRule> findAllWhiteIpRule() {
        String q = "Select cs from IpRule cs where cs.wallType='white' and cs.ifInUse='true' order by cs.genTime desc";
        List<IpRule> ais = this.entityManager.createQuery(q).getResultList();
        return ais;
    }

    public void deleteIpRule(String id) {
        IpRule p = this.entityManager.find(IpRule.class, id);
        this.entityManager.remove(p);
    }

    public void addIpRule(IpRule ca) {
        this.entityManager.persist(ca);
    }
}
