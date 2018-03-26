package com.hjedu.common.dao;

import java.util.List;

import com.hjedu.common.entity.MasterTable;
import com.hjedu.common.entity.RelTable;

public abstract interface  DataTransferDAO {
	
	public List<MasterTable> findToBeUpdatedMasterTableList();
	
	public List<RelTable> findToBeUpdatedRelTableListByFTableName(String fTable);
	
	public void updateMasterTable(MasterTable mt);
	
	public void updateRelTable(RelTable rt);
	
	public void updateRelTableService(String fTable,String fKey,String newMasterId,String oldId);
	public void updateMTableIDsService(String fTable,String fKey);
	
	public List<String> findATableIdList(String aTable);
	
	public List<String> findRelTableStr(String fTable,String fKey,String oldId);
	
	public void  updateRelTableStrService(String fTable,String fKey,String newId,String oldId);
	
	public void updateMasterTableService(String aTable,String aKey,String newId,String oldId);
}
