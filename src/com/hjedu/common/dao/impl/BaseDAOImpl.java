package com.hjedu.common.dao.impl;

import java.lang.reflect.ParameterizedType;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import com.hjedu.common.dao.BaseDAO;
import com.huajie.app.util.StringUtil;

public class BaseDAOImpl<T> implements BaseDAO<T> {
	
	private Class<T> entityClass;  

    @PersistenceContext
    private EntityManager entityManager;
    

    public EntityManager getEntityManager() {
        return this.entityManager;
    }

    public void setEntityManager(EntityManager entityManager) {
        this.entityManager = entityManager;
    }
    
    @SuppressWarnings("unchecked")  
    public BaseDAOImpl() {  
        //通过反射获取泛型传过来的类的类对象  
        this.entityClass = (Class<T>) ((ParameterizedType) this.getClass().getGenericSuperclass()).getActualTypeArguments()[0];  
    } 

	@Override
	public void add(T entity) {
		entityManager.persist(entity);
	}
	
	@Override
	public void delete(String id) {
		T entity = findById(id);
		if(entity!=null){
			entityManager.remove(entity);
		}
	}

	@Override
	public void update(T entity) {
		entityManager.merge(entity);
	}

	@Override
	public T findById(String id) {
		return (T)entityManager.find(entityClass, id);
	}

	@Override
	public long getAmount() {
		String sql = "select count(t) from "+ this.entityClass.getName() +" t ";  
        long count =  Long.parseLong(entityManager.createQuery(sql).getResultList().get(0).toString());
        return count;  
	}

	@Override
	public List<T> getScrollData(int firstindex, int maxresult, String wheresql,
			Object[] queryParams, Map<String, String> orderby) {
        Query query = entityManager.createQuery("select o from "+this.entityClass.getName()+" o " + (StringUtil.isEmpty(wheresql) ? "" : " where " + wheresql) + buildOrderby(orderby));  
        setQueryParams(query,queryParams);  
        if(maxresult!=0){
        	query.setFirstResult(firstindex).setMaxResults(maxresult);  
        }
        @SuppressWarnings("unchecked")
		List<T> list = query.getResultList();
        return list;  
	}
	
	@Override
	public long getCountByQueryParams(String wheresql,Object[] queryParams){
		Query query2 = entityManager.createQuery("select count(o) from "+this.entityClass.getName()+" o " + (StringUtil.isEmpty(wheresql) ? "" : " where " + wheresql));
        setQueryParams(query2,queryParams); 
        long count =  Long.parseLong(query2.getResultList().get(0).toString());
        return count;
	}
	
	protected void setQueryParams(Query query,Object[] queryParams) {  
        if(null != queryParams && 0 < queryParams.length) {  
            for (int i = 0; i < queryParams.length; i++) {  
                query.setParameter(i+1, queryParams[i]);  
            }  
        }  
    } 
	
	protected String buildOrderby(Map<String, String> orderby) {  
        StringBuffer sb = new StringBuffer();  
        if(null != orderby && 0 != orderby.size()) {  
            sb.append(" order by ");  
            for (String key : orderby.keySet()) {  
                sb.append("o.").append(key).append(" ").append(orderby.get(key)).append(",");  
            }  
            sb.deleteCharAt(sb.length() - 1);  
        }  
        return sb.toString();  
    }

	@Override
	public Map<String, Object> getScrollData(Class<T> entityClass, int firstindex, int maxresult, String wheresql,
			Object[] queryParams, LinkedHashMap<String, String> orderby) {
		Map<String, Object> qr = new HashMap<>();  
		List<T> list = getScrollData(firstindex, maxresult, wheresql, queryParams, orderby);
		long num = getCountByQueryParams(wheresql, queryParams);
		qr.put("list", list);
		qr.put("num", num);
		return qr;
	}  
	
	@Override
	public int ececuteQuery(String sql,Object[] queryParams,int type){
		Query qu = null;
		if(type==0){
			qu = this.getEntityManager().createNativeQuery(sql);
		}else if(type==1){
			qu = this.getEntityManager().createQuery(sql);
			setQueryParams(qu,queryParams);
		}
		int i = qu.executeUpdate();
		return i;
	}
}
