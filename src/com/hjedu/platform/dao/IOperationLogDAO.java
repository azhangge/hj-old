/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.hjedu.platform.dao;

import java.util.List;
import java.util.Map;

import com.hjedu.platform.entity.OperationLog;


public interface IOperationLogDAO {

    public void updateOperationLog(OperationLog comModel);

    public void addOperationLog(OperationLog partnerModel);

    public void deleteAll();

    public void deleteOperationLog(String id);

    public List<OperationLog> findAllOperationLogByBusinessId(String businessId);
    
    public List<OperationLog> findOperationLogByUsr(final String uid,String businessId);

    public OperationLog findOperationLog(String id);

	List<OperationLog> findOperationLogPaginator(int offSet, int num,String businessId);

	long getLogsNum(String businessId);

	List<OperationLog> findOperationLogsByFilter(Map<String, Object> fms,String businessId);

	List<OperationLog> findOrderedOperationLogs(int offSet, int num, String field, String type,String businessId);
}
