package com.hjedu.examination.dao.impl;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import com.hjedu.customer.controller.AdminSessionBean;
import com.hjedu.customer.entity.AdminInfo;
import com.hjedu.examination.dao.IDictionaryDAO;
import com.hjedu.examination.entity.DictionaryModel;
import com.huajie.util.CookieUtils;
import com.huajie.util.ExternalUserUtil;
import com.huajie.util.JsfHelper;
import com.huajie.util.SpringHelper;

public class DictionaryDAO implements IDictionaryDAO, Serializable {
    
    @PersistenceContext
    private EntityManager entityManager;
    
    public EntityManager getEntityManager() {
        return this.entityManager;
    }
    
    @Override
    public void addDictionaryModel(DictionaryModel p) {
        entityManager.persist(p);
    }
    
    @Override
    public void deleteDictionaryModel(String id) {
        DictionaryModel dm = findDictionaryModel(id);
        entityManager.remove(dm);
    }
    
    @Override
    public void deleteDictionaryModelAndAllDescendants(String id,String bussinessId) {
        List<DictionaryModel> sons = new ArrayList();
        this.loadAllDescendants(id, sons);
        this.deleteDictionaryModel(id);
        for (DictionaryModel s : sons) {
            this.deleteDictionaryModel(s.getId());
        }
    }
    
    @Override
    public void deleteAllDictionary(String bussinessId) {
        List<DictionaryModel> ms = this.findAllDictionaryModel(bussinessId);
        for (DictionaryModel e : ms) {
            if (!e.getId().equals(bussinessId)) {
                this.deleteDictionaryModel(e.getId());
            }
        }
    }
    
    @Override
    public List<DictionaryModel> loadAllDescendants(String fid) {
        List<DictionaryModel> sons = new ArrayList();
        this.loadAllDescendants(fid, sons);
        return sons;
    }
    
    @Override
    public void loadAllDescendants(String fid, List<DictionaryModel> sons) {
        List<DictionaryModel> ls = this.findAllSonDictionaryModel(fid);
        if (ls.isEmpty()) {
            return;
        } else {
            for (DictionaryModel d : ls) {
                sons.add(d);
                loadAllDescendants(d.getId(), sons);
            }
        }
    }
    
    @Override
    public List<DictionaryModel> findAllDictionaryModel(String businessId) {
        String q = "Select dm from DictionaryModel dm where dm.businessId=:businessId order by dm.ord";
        Query qu = entityManager.createQuery(q);
        qu.setParameter("businessId", businessId);
        List<DictionaryModel> ys = qu.getResultList();
        return ys;
    }

    /**
     * 获得所有部门
     *
     * @return
     */
    @Override
    public List<DictionaryModel> findAllDefaultDepartments(String businessId) {
        String q = "Select dm from DictionaryModel dm where dm.asDefault = true and dm.businessId = :businessId order by dm.ord";
        Query qu = entityManager.createQuery(q);
        qu.setParameter("businessId", businessId);
        List<DictionaryModel> ys = qu.getResultList();
        return ys;
    }

    /**
     * 获得所有的默认部门的ID串
     *
     * @return
     */
    @Override
    public String findAllDefaultDepartmentStr(String businessId) {
        StringBuilder sb = new StringBuilder();
        List<DictionaryModel> ys = this.findAllDefaultDepartments(businessId);
        for (DictionaryModel dm : ys) {
            sb.append(dm.getId());
            sb.append(";");
        }
        return sb.toString();
    }
    
    @Override
    public List<DictionaryModel> findDictionaryModelByUrlType(String type) {
        String q = "Select dm from DictionaryModel dm where dm.urlType=:type order by dm.ord";
        Query qu = this.entityManager.createQuery(q);
        qu.setParameter("type", type);
        List<DictionaryModel> ys = qu.getResultList();
        return ys;
    }
    
    @Override
    public DictionaryModel findDictionaryModelByName(String name,String businessId) {
        String q = "Select dm from DictionaryModel dm where dm.name=:name and dm.businessId = :businessId order by dm.ord";
        Query qu = this.entityManager.createQuery(q);
        qu.setParameter("name", name);
        qu.setParameter("businessId", businessId);
        List<DictionaryModel> ys = qu.getResultList();
        if (ys.isEmpty()) {
            return null;
        } else {
            return ys.get(0);
        }
    }
    
    @Override
    public List<DictionaryModel> findAllSonDictionaryModel(String fatherID) {
    	AdminInfo ai = ExternalUserUtil.getAdminBySession();
    	AdminSessionBean asb = JsfHelper.getBean("adminSessionBean");
    	String q = "Select dm from DictionaryModel dm where dm.fatherId=:id and :ai member of dm.admins order by dm.ord";
    	if(asb.isIfSuper()){
    		q = "Select dm from DictionaryModel dm where dm.fatherId=:id order by dm.ord";
    	}
        Query qu = this.entityManager.createQuery(q);
        qu.setParameter("id", fatherID);
        if(!asb.isIfSuper()){
        	qu.setParameter("ai", ai);
        }
        List<DictionaryModel> ys = qu.getResultList();
        return ys;
    }
    
    @Override
    public List<DictionaryModel> findDictionaryModelByFatherID(String fatherID) {
        String q = "Select dm from DictionaryModel dm where dm.fatherId=:id order by dm.ord";
        Query qu = this.entityManager.createQuery(q);
        qu.setParameter("id", fatherID);
        List<DictionaryModel> ys = qu.getResultList();
        return ys;
    }
    
    @Override
    public DictionaryModel findDictionaryModel(String id) {
    	DictionaryModel dm = null;
    	String q = "Select dm from DictionaryModel dm where dm.id=:id";
        Query qu = this.entityManager.createQuery(q);
        qu.setParameter("id", id);
        List<DictionaryModel> dml = qu.getResultList();
        if(dml.size()>0){
        	dm=dml.get(0);
        }
        return dm;
    }
    
    @Override
    public void updateDictionaryModel(DictionaryModel p) {
        entityManager.merge(p);
    }
    
    /**
     * 获取所有叶节点部门
     */
    @Override
    public List<DictionaryModel> findLeafDictionaryModel(String businessId) {
        String q = "Select dm from DictionaryModel dm where not exists(Select a from DictionaryModel a "
        		+ "where a.fatherId=dm.id and a.businessId=:businessId1) and dm.businessId=:businessId2";
        Query qu = this.entityManager.createQuery(q);
        qu.setParameter("businessId1", businessId);
        qu.setParameter("businessId2", businessId);
        List<DictionaryModel> ys = qu.getResultList();
        return ys;
    }
}
