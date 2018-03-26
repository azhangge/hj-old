package com.hjedu.common.dao;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
public abstract interface BaseDAO<T> {
	void add(T entity);  
	
	void delete(String id);  
	
    void update(T entity);  
    
    T findById(String id);
    
    /* 
     * 得到总数 
     */  
    long getAmount();
    
    /** 
     * 带排序功能的分页实现 
     * @param <T> 
     * @param entityClass 
     * @param firstindex 
     * @param maxresult 
     * @param wheresql 添加限制条件 
     * @param orderby 
     * @return 
     */  
    public Map<String, Object> getScrollData(Class<T> entityClass,int firstindex,int maxresult,String wheresql,Object[] queryParams,LinkedHashMap<String, String> orderby);
    
    /**
     * 带排序功能的分页实现
     * @param firstindex （第一条数据为0）起始数据坐标
     * @param maxresult 每页数据条数(如果为0全查)
     * @param wheresql 查询条件（不带where,例如 : o.id=?1 ）
     * @param queryParams Object[]参数数组，按照wheresql中参数的顺序
     * @param orderby LinkedHashMap<String, String>(排序字段，desc/asc)
     * @return
     */
    List<T> getScrollData(int firstindex, int maxresult, String wheresql, Object[] queryParams,Map<String, String> orderby);
	
	/**
	 * 根据查询条件获取数据总数
	 * @param wheresql
	 * @param queryParams
	 * @return
	 */
	long getCountByQueryParams(String wheresql, Object[] queryParams);

	/**
	 * 执行sql语句
	 * @param sql
	 * @param queryParams
	 * @param type 0:原生sql；1：jpa语句
	 * @return 改动记录数
	 */
	int ececuteQuery(String sql, Object[] queryParams, int type); 
}
