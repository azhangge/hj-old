/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.hjedu.platform.dao.impl;

import java.io.Serializable;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import com.hjedu.platform.dao.IOperationLogDAO;
import com.hjedu.platform.entity.OperationLog;
import com.huajie.app.util.QueryUtil;
import com.huajie.seller.model.SaleGoods;

public class OperationLogDAO implements IOperationLogDAO, Serializable {

    @PersistenceContext
    private EntityManager entityManager;

    public EntityManager getEntityManager() {
        return this.entityManager;
    }

    public void setEntityManager(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    public void addOperationLog(OperationLog partnerModel) {
        entityManager.persist(partnerModel);
    }

    public void deleteOperationLog(String id) {
        OperationLog cm = entityManager.find(OperationLog.class, id);
        entityManager.remove(cm);
    }

    public List<OperationLog> findAllOperationLogByBusinessId(String businessId) {
        String q = "Select cms from OperationLog cms where cms.businessId = :businessId order by cms.genTime desc";
        Query query = entityManager.createQuery(q);
        query.setMaxResults(1000);
        query.setParameter("businessId", businessId);
        List<OperationLog> as = query.getResultList();
        
        return removeAdminNull(as);
    }
    public List<OperationLog> removeAdminNull(List<OperationLog> as){
    	for (int i = as.size()-1; i>=0; i--) {
            if (as.get(i).getAdmin()==null) {
                as.remove(i);
            }
	    }
	    return as;
    }

    @Override
    public List<OperationLog> findOperationLogByUsr(final String uid,String businessId) {
        String q = "Select cms from OperationLog cms where cms.uid=:uid and ol.businessId = :businessId order by cms.genTime desc";
        Query query = entityManager.createQuery(q);
        query.setParameter("uid", uid);
        query.setParameter("businessId", businessId);
        query.setMaxResults(1000);
        List<OperationLog> as = query.getResultList();
        return as;
    }

    public OperationLog findOperationLog(String id) {
        return entityManager.find(OperationLog.class, id);

    }

    public void updateOperationLog(OperationLog comModel) {
        entityManager.merge(comModel);
    }

    @Override
    public void deleteAll() {
        String q = "delete from OperationLog cms";
        entityManager.createQuery(q).executeUpdate();
    }

	@SuppressWarnings("unchecked")
	@Override
	public List<OperationLog> findOperationLogPaginator(int firstSize,int pageSize,String businessId) {
		String q = "Select ol from OperationLog ol where ol.businessId = :businessId";
		Query query = entityManager.createQuery(q);
		query.setParameter("businessId", businessId);
		QueryUtil.setQuerySize(query, firstSize, pageSize);
		return query.getResultList();
	}

	@Override
	public long getLogsNum(String businessId) {
		String q = "Select count(ol) from OperationLog ol where ol.businessId = :businessId";
		Query query = entityManager.createQuery(q);
    	query.setParameter("businessId", businessId);
		long l = Long.parseLong(String.valueOf(query.getResultList().get(0)));
		return l;
	}

	@Override
	public List<OperationLog> findOperationLogsByFilter(Map<String, Object> fms,String businessId) {
        String q = "Select ol from OperationLog ol where 1=1 and ol.businessId = :businessId";
        for (Iterator<String> it = fms.keySet().iterator(); it.hasNext();) {
            String filterProperty = it.next();
            String filterValue = fms.get(filterProperty).toString();
            if (filterValue != null) {
                q = q + " and cq." + filterProperty + " like '%" + filterValue + "%' ";
            }
        }
        Query qu = this.entityManager.createQuery(q);
        qu.setParameter("businessId", businessId);
        List<OperationLog> cqs = qu.getResultList();
        return cqs;
    }

	@Override
	public List<OperationLog> findOrderedOperationLogs(int offSet, int num, String field, String type,String businessId) {
        String q = "Select  ol from OperationLog ol where 1=1 and  ol.businessId = :businessId order by ol."+field+" "+type;
        Query qu = this.entityManager.createQuery(q);
        qu.setParameter("businessId", businessId);
        qu.setFirstResult(offSet);
        qu.setMaxResults(num);
        List<OperationLog> cqs = qu.getResultList();
        return cqs;
    
	}
}
