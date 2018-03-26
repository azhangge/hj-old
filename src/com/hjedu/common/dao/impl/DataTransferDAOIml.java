package com.hjedu.common.dao.impl;

import java.io.Serializable;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import com.hjedu.common.dao.DataTransferDAO;
import com.hjedu.common.entity.MasterTable;
import com.hjedu.common.entity.RelTable;


public class DataTransferDAOIml implements DataTransferDAO, Serializable {
	private static final long serialVersionUID = 1L;
	
	@PersistenceContext
    private EntityManager entityManager;
	
	/**
	 * 查询待更新主表信息
	 * */
	public List<MasterTable> findToBeUpdatedMasterTableList(){
		String q = "select mt from MasterTable mt where mt.status!=1 order by mt.aTable asc";
        List<MasterTable> mtl = entityManager.createQuery(q).getResultList();
		return mtl;
	}
	
	/**
	* 根据 主表名称 查询待更新 从表信息列表
	* */
	public List<RelTable> findToBeUpdatedRelTableListByFTableName(String fTable){
		String q = "select rt from RelTable rt where  rt.fTable=:fTable order by rt.aTable asc";
        List<RelTable> rtl = entityManager.createQuery(q).setParameter("fTable", 	fTable).getResultList();
		return rtl;
	}
	
	/**
	 * 更新主表信息
	 **/
	public void updateMasterTable(MasterTable mt) {
		entityManager.merge(mt);
	}
	
	/**
	 * 更新从表信息
	 **/
	public void updateRelTable(RelTable rt) {
		entityManager.merge(rt);
	}
	
	/**
	 * 更新主表id业务
	 **/
	public void updateMasterTableService(String aTable,String aKey,String newId,String oldId){
		String q ="update "+aTable+" set "+aKey+"='"+newId+"' where "+aKey+"='"+oldId+"'";
		Query query = entityManager.createNativeQuery(q);
		query.executeUpdate();
	}
	
	/**
	 * 更新从表id业务
	 **/
	public void updateRelTableService(String fTable,String fKey,String newId,String oldId){
		String q = "update "+fTable+" set "+fKey+"='"+newId+"' where "+fKey+"='"+oldId+"'";
		Query query = entityManager.createNativeQuery(q);
		query.executeUpdate();
	}
	
	/**
	 * 批量更新主表id
	 **/
	public void updateMTableIDsService(String fTable,String fKey){
		String q = "update "+fTable+" set "+fKey+"=substr(LOWER(sys_guid()),1,8)||'-'||substr(LOWER(sys_guid()),9,4)||'-'||substr(LOWER(sys_guid()),13,4)||'-'||substr(LOWER(sys_guid()),17,4)||'-'||substr(LOWER(sys_guid()),21,12) ";
		Query query = entityManager.createNativeQuery(q);
		query.executeUpdate();
	}
	
	/**
	 * 查询主表id
	 **/
	public List<String> findATableIdList(String aTable){
		String q = "select id from "+aTable;
		Query query = entityManager.createNativeQuery(q);
		List<String> idl = query.getResultList();
		return idl;
	}
	
	/**
	 * 查询从表str字段
	 **/
	public List<String> findRelTableStr(String fTable,String fKey,String oldId){
		String q = "select "+fKey+" from "+fTable+" where instr("+fKey+",'"+oldId+"')>0";
		Query query = entityManager.createNativeQuery(q);
		List<String> str = query.getResultList();
		return str;
	}
	
	/**
	 * 更新从表str字段
	 **/
	public void  updateRelTableStrService(String fTable,String fKey,String newId,String oldId){
		String q = "update "+fTable+" set "+fKey+"=replace("+ fKey+",'"+oldId+"','"+newId+"')"
				 + " where instr("+fKey+",'"+oldId+"')>0";	
		Query query = entityManager.createNativeQuery(q);
		query.executeUpdate();
	}
	
}
