package com.hjedu.examination.entity.lazy;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import org.primefaces.model.LazyDataModel;
import org.primefaces.model.SortOrder;

import com.hjedu.platform.dao.IOperationLogDAO;
import com.hjedu.platform.entity.OperationLog;
import com.huajie.util.CookieUtils;
import com.huajie.util.JsfHelper;
import com.huajie.util.SpringHelper;

public class LazyOperationLog extends LazyDataModel<OperationLog> {

    IOperationLogDAO operationLogDAO = SpringHelper.getSpringBean("OperationLogDAO");
    

    @Override
    public List<OperationLog> load(int first, int pageSize, String sortField, SortOrder sortOrder, Map<String, Object> filters) {
    	Long begin = new Date().getTime();
    	boolean ifFilter = false;
        List<OperationLog> models = new LinkedList<>();
        List<OperationLog> data = new ArrayList<>();
        String businessId = CookieUtils.getBusinessId(JsfHelper.getRequest());
        for (String filterProperty : filters.keySet()) {
            String filterValue = filters.get(filterProperty).toString();
            if (filterValue != null) {
                ifFilter = true;
                break;
            }
        }

        if (!ifFilter && sortField == null) {
            //System.out.println("no filter no sort");
            //rowCount  
            int num = (int) this.operationLogDAO.getLogsNum(businessId);
            this.setRowCount(num);
            //paginate  
            models = operationLogDAO.findOperationLogPaginator(first, pageSize,businessId);
            if(models!=null)
            data.addAll(models);
        } else if (ifFilter && sortField == null) {
            //System.out.println("filter no sort");
            //filter  
            models = operationLogDAO.findOperationLogsByFilter(filters,businessId);
            this.setRowCount(models.size());
            data.addAll(models);
        } //sort  
        else if (!ifFilter&&sortField != null) {
            //System.out.println("no filter  sort");
            int num = (int) this.operationLogDAO.getLogsNum(businessId);
            this.setRowCount(num);
            String ot = SortOrder.ASCENDING.equals(sortOrder) ? "asc" : "desc";
            models = operationLogDAO.findOrderedOperationLogs(first, pageSize, sortField, ot,businessId);
            data.addAll(models);
            //Collections.sort(data, new LazySorter<OperationLog>(OperationLog.class, sortField, sortOrder));
        }
        else if (ifFilter&&sortField != null) {
            //System.out.println("filter  sort");
            models = operationLogDAO.findOperationLogsByFilter(filters,businessId);
            this.setRowCount(models.size());
            data.addAll(models);
            Collections.sort(data, new LazySorter<OperationLog>(OperationLog.class, sortField, sortOrder));
        }
        Long end = new Date().getTime();
        System.out.println(end-begin);
        return data;
    }
}
